package com.example.huertohogardefinitiveedition.supabase

import kotlinx.serialization.Serializable

@Serializable
data class PedidoSupabase(
    val producto: String,
    val cantidad: Int,
    val total: Int,
    val direccion: String,
    val fecha_entrega: String,
    val usuario_id: Long? = null
)