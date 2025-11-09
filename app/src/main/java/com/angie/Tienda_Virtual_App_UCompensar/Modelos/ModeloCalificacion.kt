package com.angie.Tienda_Virtual_App_UCompensar.Modelos

class ModeloCalificacion {

    var idResenia : String = ""
    var resenia : String = ""
    var uidUsuario : String = ""
    var calificacion : Int = 0

    constructor()

    constructor(idResenia: String, resenia: String, uidUsuario: String, calificacion: Int) {
        this.idResenia = idResenia
        this.resenia = resenia
        this.uidUsuario = uidUsuario
        this.calificacion = calificacion
    }


}