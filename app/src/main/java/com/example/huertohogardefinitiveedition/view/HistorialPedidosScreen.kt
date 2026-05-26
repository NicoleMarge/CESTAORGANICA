package com.example.huertohogardefinitiveedition.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

import com.example.huertohogardefinitiveedition.data.database.PedidoHistorial

@Composable
fun HistorialPedidosScreen() {

    val listaPedidos =
        PedidoHistorial.obtenerPedidos()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        items(listaPedidos) { pedido ->

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
            ) {

                Column(
                    modifier = Modifier.padding(16.dp)
                ) {

                    Text(
                        text = pedido.nombre,
                        style = MaterialTheme.typography.titleMedium
                    )

                    Spacer(
                        modifier = Modifier.height(8.dp)
                    )

                    Text(
                        text = "Cantidad: ${pedido.cantidad}"
                    )

                    Text(
                        text = "Precio: ${pedido.precio}"
                    )

                    Text(
                        text = "Dirección: ${pedido.direccion}"
                    )
                }
            }
        }
    }
}