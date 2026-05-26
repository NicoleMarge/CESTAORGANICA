package com.example.huertohogardefinitiveedition.data.repository

import com.example.huertohogardefinitiveedition.data.model.Pedido

object CarritoRepository {

    private val carrito = mutableListOf<Pedido>()

    fun agregarProducto(pedido: Pedido) {
        carrito.add(pedido)
    }

    fun obtenerCarrito(): List<Pedido> {
        return carrito
    }

    fun vaciarCarrito() {

        carrito.clear()
    }

    fun limpiarCarrito() {
        carrito.clear()
    }
}