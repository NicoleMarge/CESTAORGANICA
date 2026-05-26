package com.example.huertohogardefinitiveedition.view

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

import com.example.huertohogardefinitiveedition.data.repository.CarritoRepository
import com.example.huertohogardefinitiveedition.supabase.PedidoRepository
import com.example.huertohogardefinitiveedition.supabase.PedidoSupabase
import com.example.huertohogardefinitiveedition.viewmodel.CarritoViewModel

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun CarritoScreen(

    vm: CarritoViewModel = viewModel()
) {

    val context =
        LocalContext.current

    val coroutineScope =
        rememberCoroutineScope()

    val carrito =
        CarritoRepository.obtenerCarrito()

    Column(

        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        // LISTA PRODUCTOS
        LazyColumn(

            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {

            items(carrito) { pedido ->

                Card(

                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                ) {

                    Column(

                        modifier = Modifier
                            .padding(16.dp)
                    ) {

                        Text(
                            "Producto: ${pedido.producto}"
                        )

                        Text(
                            "Cantidad: ${pedido.cantidad}"
                        )

                        Text(
                            "Total: ${pedido.total}"
                        )

                        Text(
                            "Entrega: ${pedido.fecha_entrega}"
                        )
                    }
                }
            }
        }

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        // BOTON COMPRAR
        Button(

            onClick = {

                if (carrito.isEmpty()) {

                    Toast.makeText(

                        context,

                        "El carrito está vacío",

                        Toast.LENGTH_SHORT

                    ).show()

                    return@Button
                }

                coroutineScope.launch {

                    try {

                        withContext(
                            Dispatchers.IO
                        ) {

                            // GUARDAR CADA PEDIDO EN SUPABASE
                            carrito.forEach { pedido ->

                                val pedidoSupabase =
                                    PedidoSupabase(

                                        producto =
                                            pedido.producto,

                                        cantidad =
                                            pedido.cantidad,

                                        total =
                                            pedido.total,

                                        direccion =
                                            pedido.direccion,

                                        fecha_entrega =
                                            pedido.fecha_entrega
                                    )

                                PedidoRepository
                                    .guardarPedido(
                                        pedidoSupabase
                                    )
                            }

                            // LIMPIAR CARRITO
                            CarritoRepository
                                .vaciarCarrito()
                        }

                        Toast.makeText(

                            context,

                            "Compra realizada",

                            Toast.LENGTH_LONG

                        ).show()

                    } catch (e: Exception) {

                        e.printStackTrace()

                        Toast.makeText(

                            context,

                            "Error: ${e.message}",

                            Toast.LENGTH_LONG

                        ).show()
                    }
                }
            },

            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .align(
                    Alignment.CenterHorizontally
                )
        ) {

            Text("Comprar")
        }
    }
}