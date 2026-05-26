package com.example.huertohogardefinitiveedition.data.database

import com.example.huertohogardefinitiveedition.data.model.Producto

object PedidoHistorial {

    private val listaPedidos =
        mutableListOf<Producto>()

    fun agregar(producto: Producto) {

        listaPedidos.add(producto)
    }

    fun obtenerPedidos(): List<Producto> {

        return listaPedidos
    }
}

