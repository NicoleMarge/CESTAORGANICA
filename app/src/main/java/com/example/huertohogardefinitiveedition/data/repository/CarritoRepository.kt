package com.example.huertohogardefinitiveedition.data.repository

import androidx.compose.runtime.mutableStateListOf
import com.example.huertohogardefinitiveedition.data.model.Pedido

object CarritoRepository {

    // LISTA REACTIVA
    val carrito = mutableStateListOf<Pedido>()

    // AGREGAR PRODUCTO
    fun agregarProducto(pedido: Pedido) {

        // BUSCAR SI YA EXISTE
        val productoExistente = carrito.firstOrNull {

            it.producto == pedido.producto
        }

        // SI EXISTE -> SUMAR CANTIDAD
        if (productoExistente != null) {

            actualizarCantidad(

                productoExistente,

                productoExistente.cantidad + pedido.cantidad
            )

        } else {

            carrito.add(pedido)
        }
    }

    // OBTENER CARRITO
    fun obtenerCarrito(): List<Pedido> {

        return carrito
    }

    // VACIAR
    fun vaciarCarrito() {

        carrito.clear()
    }

    // LIMPIAR
    fun limpiarCarrito() {

        carrito.clear()
    }

    // ELIMINAR PRODUCTO
    fun eliminarProducto(pedido: Pedido) {

        carrito.remove(pedido)
    }

    // TOTAL
    fun obtenerTotal(): Int {

        return carrito.sumOf {

            it.total
        }
    }

    // ACTUALIZAR CANTIDAD
    fun actualizarCantidad(

        pedido: Pedido,

        nuevaCantidad: Int
    ) {

        val index = carrito.indexOf(pedido)

        if (index != -1) {

            if (nuevaCantidad <= 0) {

                carrito.removeAt(index)

                return
            }

            val precioUnitario =

                pedido.total / pedido.cantidad

            carrito[index] = pedido.copy(

                cantidad = nuevaCantidad,

                total = precioUnitario * nuevaCantidad
            )
        }
    }
}
