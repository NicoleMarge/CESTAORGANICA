package com.example.huertohogardefinitiveedition.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.huertohogardefinitiveedition.data.repository.CarritoRepository
import com.example.huertohogardefinitiveedition.supabase.PedidoRepository
import com.example.huertohogardefinitiveedition.supabase.PedidoSupabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CarritoViewModel : ViewModel() {

    fun enviarPedidoASupabase(
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {

        val productosEnCarrito =
            CarritoRepository.obtenerCarrito()

        if (productosEnCarrito.isEmpty()) {

            onError("El carrito está vacío")
            return
        }



        viewModelScope.launch(Dispatchers.IO) {

            try {

                productosEnCarrito.forEach { pedido ->

                    val pedidoSupabase = PedidoSupabase(

                        producto = pedido.producto,

                        cantidad = pedido.cantidad,

                        total = pedido.total.toInt(),

                        direccion = "Dirección pendiente",

                        fecha_entrega = pedido.fecha_entrega
                    )

                    PedidoRepository.guardarPedido(
                        pedidoSupabase
                    )
                }

                withContext(Dispatchers.Main) {

                    CarritoRepository.limpiarCarrito()

                    onSuccess()
                }

            } catch (e: Exception) {

                withContext(Dispatchers.Main) {

                    onError(
                        e.message
                            ?: "Error al guardar pedido"
                    )
                }
            }
        }
    }
}