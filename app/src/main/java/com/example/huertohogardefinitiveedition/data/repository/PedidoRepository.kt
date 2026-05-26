package com.example.huertohogardefinitiveedition.supabase

import io.github.jan.supabase.postgrest.from

object PedidoRepository {

    suspend fun guardarPedido(
        pedido: PedidoSupabase
    ) {

        SupabaseConfig.client
            .from("pedidos")
            .insert(pedido)
    }
}