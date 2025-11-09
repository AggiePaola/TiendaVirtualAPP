package com.angie.Tienda_Virtual_App_UCompensar.data

import com.angie.Tienda_Virtual_App_UCompensar.Modelos.ResponseHttp
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST("crear_preferencia")
    fun enviarOrdenDeCompra(@Body requestBody : RequestBody) : Call<ResponseHttp>

}