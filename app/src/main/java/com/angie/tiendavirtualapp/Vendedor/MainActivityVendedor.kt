package com.angie.tiendavirtualapp.Vendedor

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.angie.tiendavirtualapp.R
import com.angie.tiendavirtualapp.Vendedor.Bottom_Nav_Fragments_Vendedor.FragmentMisProductosV
import com.angie.tiendavirtualapp.Vendedor.Bottom_Nav_Fragments_Vendedor.FragmentOrdenesV
import com.angie.tiendavirtualapp.Vendedor.Nav_Fragments_Vendedor.FragmentInicioV
import com.angie.tiendavirtualapp.Vendedor.Nav_Fragments_Vendedor.FragmentMiTiendaV
import com.angie.tiendavirtualapp.Vendedor.Nav_Fragments_Vendedor.FragmentReseniasV
import com.angie.tiendavirtualapp.databinding.ActivityMainVendedorBinding
import com.google.android.material.navigation.NavigationView

class MainActivityVendedor : AppCompatActivity() , NavigationView.OnNavigationItemSelectedListener{

    private lateinit var binding: ActivityMainVendedorBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainVendedorBinding.inflate(layoutInflater)
        setContentView(binding.root)



        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        binding.navigationView.setNavigationItemSelectedListener(this)

        val toggle = ActionBarDrawerToggle(
            this,
            binding.drawerLayOut,
            toolbar,
            R.string.open_drawer,
            R.string.close_drawer
        )
        binding.drawerLayOut.addDrawerListener(toggle)
        toggle.syncState()

        replaceFragment(FragmentInicioV())
        binding.navigationView.setCheckedItem(R.id.op_inicio_v)



        }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.navFragment,fragment)
            .commit()

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean { //NAVEGAR
        when (item.itemId) {
            R.id.op_inicio_v->{
                replaceFragment(FragmentInicioV())
            }
            R.id.op_mi_tienda_v->{
                replaceFragment(FragmentMiTiendaV())
            }
            R.id.op_resenia_v->{
                replaceFragment(FragmentReseniasV())
            }
            R.id.op_cerrar_sesion_v-> {
                Toast.makeText(applicationContext, "Saliste de la aplicación", Toast.LENGTH_SHORT).show()
            }
            R.id.op_mis_productos_v-> {
                replaceFragment(FragmentMisProductosV())
            }
            R.id.op_mis_ordenes_v-> {
                replaceFragment(FragmentOrdenesV())
            }
        }
        binding.drawerLayOut.closeDrawer(GravityCompat.START)
            return true
    }
}
