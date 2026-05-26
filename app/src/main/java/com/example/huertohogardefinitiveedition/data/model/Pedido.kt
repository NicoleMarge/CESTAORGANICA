package com.example.huertohogardefinitiveedition.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Pedido(

    val producto: String,

    val cantidad: Int,

    val total: Int,

    val direccion: String,

    val fecha_entrega: String,

    val usuario: String
)