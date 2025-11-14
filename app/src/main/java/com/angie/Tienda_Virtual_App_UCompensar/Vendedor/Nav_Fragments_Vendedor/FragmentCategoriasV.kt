package com.angie.Tienda_Virtual_App_UCompensar.Vendedor.Nav_Fragments_Vendedor

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.angie.Tienda_Virtual_App_UCompensar.Adaptadores.AdaptadorCategoriaV
import com.angie.Tienda_Virtual_App_UCompensar.Modelos.ModeloCategoria
import com.angie.Tienda_Virtual_App_UCompensar.R
import com.angie.Tienda_Virtual_App_UCompensar.databinding.FragmentCategoriasVBinding
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

import com.google.firebase.storage.FirebaseStorage
import okhttp3.Call
import java.io.IOException

class FragmentCategoriasV : Fragment() {

    private lateinit var binding : FragmentCategoriasVBinding
    private lateinit var mContext : Context
    private lateinit var progressDialog : ProgressDialog
    private var imageUri : Uri?=null

    private lateinit var categoriasArrayList : ArrayList<ModeloCategoria>
    private lateinit var adaptadorCategoriaV : AdaptadorCategoriaV

    override fun onAttach(context: Context) {
        mContext = context
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentCategoriasVBinding.inflate(inflater, container, false)

        progressDialog = ProgressDialog(context)
        progressDialog.setTitle("Espere por favor")
        progressDialog.setCanceledOnTouchOutside(false)

        binding.imgCategorias.setOnClickListener {
            seleccionarImg()
        }

        binding.btnAgregarCat.setOnClickListener {
            validarInfo()
        }

        listarCategorias()

        return binding.root
    }

    private fun listarCategorias() {
        categoriasArrayList = ArrayList()
        val ref = FirebaseDatabase.getInstance().getReference("Categorias").orderByChild("categoria")
        ref.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                categoriasArrayList.clear()
                for (ds in snapshot.children){
                    val modelo = ds.getValue(ModeloCategoria::class.java)
                    categoriasArrayList.add(modelo!!)
                }
                adaptadorCategoriaV = AdaptadorCategoriaV(mContext, categoriasArrayList)
                binding.rvCategorias.adapter = adaptadorCategoriaV
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun seleccionarImg() {
        ImagePicker.with(requireActivity())
            .crop()
            .compress(1024)
            .maxResultSize(1080,1080)
            .createIntent {intent->
                resultadoImg.launch(intent)
            }
    }

    private val resultadoImg =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()){resultado->
            if (resultado.resultCode == Activity.RESULT_OK){
                val data = resultado.data
                imageUri = data!!.data
                binding.imgCategorias.setImageURI(imageUri)
            }else{
                Toast.makeText(mContext, "Acción cancelada",Toast.LENGTH_SHORT).show()
            }
        }

    private var categoria = ""
    private fun validarInfo() {
        categoria = binding.etCategoria.text.toString().trim()
        if (categoria.isEmpty()){
            Toast.makeText(context, "Ingrese una categoria",Toast.LENGTH_SHORT).show()
        }else if (imageUri == null){
            Toast.makeText(context, "Seleccione una imagen",Toast.LENGTH_SHORT).show()
        }
        else{
            agregarCatBD()
        }
    }

    private fun agregarCatBD() {
        progressDialog.setMessage("Agregando categoría")
        progressDialog.show()

        val ref = FirebaseDatabase.getInstance().getReference("Categorias")
        val keyId = ref.push().key

        val hashMap = HashMap<String, Any>()
        hashMap["id"] = "${keyId}"
        hashMap["categoria"] = "${categoria}"

        ref.child(keyId!!)
            .setValue(hashMap)
            .addOnSuccessListener {
                //progressDialog.dismiss()
                //Toast.makeText(context, "Se agregó la categoría con éxito",Toast.LENGTH_SHORT).show()
                //binding.etCategoria.setText("")
                subirImgStorage(keyId)
            }
            .addOnFailureListener {e->
                progressDialog.dismiss()
                Toast.makeText(context, "${e.message}",Toast.LENGTH_SHORT).show()

            }

    }

    private fun subirImgStorage(keyId: String) {
            progressDialog.setMessage("Subiendo imagen...")
            progressDialog.show()

            // 1. Convertir imagen a Base64
            val inputStream = requireActivity().contentResolver.openInputStream(imageUri!!)
            val bytes = inputStream!!.readBytes()
            val encodedImage = android.util.Base64.encodeToString(bytes, android.util.Base64.DEFAULT)

            // 2. API Key ImgBB
            val apiKey = "3053985189e13866643b1c4e053a1837"

            // 3. Petición HTTP con OkHttp
            val client = okhttp3.OkHttpClient()
            val requestBody = okhttp3.FormBody.Builder()
                .add("key", apiKey)
                .add("image", encodedImage)
                .build()

            val request = okhttp3.Request.Builder()
                .url("https://api.imgbb.com/1/upload")
                .post(requestBody)
                .build()

            client.newCall(request).enqueue(object : okhttp3.Callback {
                override fun onFailure(call: Call, e: IOException) {
                    requireActivity().runOnUiThread {
                        progressDialog.dismiss()
                        Toast.makeText(mContext, "Error al subir la imagen: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                    val responseData = response.body?.string()

                    try {
                        val json = org.json.JSONObject(responseData!!)
                        val imageUrl = json.getJSONObject("data").getString("url")

                        // 4. Guardar URL en Firebase
                        val hashMap = HashMap<String, Any>()
                        hashMap["imagenUrl"] = imageUrl

                        val ref = FirebaseDatabase.getInstance().getReference("Categorias")
                        ref.child(keyId).updateChildren(hashMap)

                        requireActivity().runOnUiThread {
                            progressDialog.dismiss()
                            Toast.makeText(mContext, "Categoría agregada con éxito", Toast.LENGTH_SHORT).show()

                            // limpiar UI
                            binding.etCategoria.text.clear()
                            imageUri = null
                            binding.imgCategorias.setImageResource(R.drawable.categorias)
                        }

                    } catch (e: Exception) {
                        requireActivity().runOnUiThread {
                            progressDialog.dismiss()
                            Toast.makeText(mContext, "Error al procesar la respuesta", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            })
        }

    }

