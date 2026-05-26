package com.example.huertohogardefinitiveedition.view

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.huertohogardefinitiveedition.viewmodel.DrawerMenuViewModel
import androidx.compose.ui.platform.LocalContext
import com.example.huertohogardefinitiveedition.data.database.PedidoHistorial
import com.example.huertohogardefinitiveedition.data.model.Pedido
import com.example.huertohogardefinitiveedition.data.model.Producto
import com.example.huertohogardefinitiveedition.data.repository.CarritoRepository
import com.example.huertohogardefinitiveedition.data.session.SessionManager

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductoFormScreen(

    navController: NavController,

    nombre: String,

    precio: String,

    descripcion: String,

    stock: Int,

    viewModel: DrawerMenuViewModel
) {

    val context = LocalContext.current

    val coroutineScope = rememberCoroutineScope()

    val usuarioActual = SessionManager.currentUser

    val direccionPerfil =
        usuarioActual?.direccion
            ?: "Sin dirección"

    var cantidad by remember {
        mutableStateOf("1")
    }

    var fechaEntrega by remember {
        mutableStateOf("")
    }

    var mostrarDialogoProducto by remember {
        mutableStateOf(false)
    }

    var mostrarDialogoFecha by remember {
        mutableStateOf(false)
    }

    val estadoFecha =
        rememberDatePickerState()

    val precioBase =
        precio.toIntOrNull() ?: 0

    val cantidadNum =
        cantidad.toIntOrNull() ?: 0

    val total =
        precioBase * cantidadNum

    val formatoMoneda = remember {

        NumberFormat.getCurrencyInstance(
            Locale("es", "CL")
        )
    }

    LazyColumn(

        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),

        horizontalAlignment =
            Alignment.CenterHorizontally

    ) {

        item {

            Column {

                Text(
                    text = nombre,
                    style =
                        MaterialTheme.typography.headlineMedium
                )

                Spacer(
                    modifier =
                        Modifier.height(8.dp)
                )

                Text(
                    text =
                        "Precio: ${
                            formatoMoneda.format(
                                precioBase
                            )
                        }"
                )

                Text(
                    text = "Stock: $stock"
                )

                Spacer(
                    modifier =
                        Modifier.height(16.dp)
                )

                Text(
                    text = descripcion
                )

                Spacer(
                    modifier =
                        Modifier.height(20.dp)
                )

                OutlinedTextField(

                    value = cantidad,

                    onValueChange = {

                        cantidad = it.filter { c ->
                            c.isDigit()
                        }
                    },

                    label = {
                        Text("Cantidad")
                    },

                    keyboardOptions =
                        KeyboardOptions(
                            keyboardType =
                                KeyboardType.Number
                        ),

                    modifier =
                        Modifier.fillMaxWidth()
                )

                Spacer(
                    modifier =
                        Modifier.height(12.dp)
                )

                OutlinedTextField(

                    value = fechaEntrega,

                    onValueChange = {},

                    readOnly = true,

                    label = {
                        Text("Fecha entrega")
                    },

                    trailingIcon = {

                        Icon(

                            imageVector =
                                Icons.Default.DateRange,

                            contentDescription = null,

                            modifier =
                                Modifier.clickable {

                                    mostrarDialogoFecha = true
                                }
                        )
                    },

                    modifier =
                        Modifier.fillMaxWidth()
                )

                Spacer(
                    modifier =
                        Modifier.height(20.dp)
                )

                Text(

                    text = "TOTAL",

                    fontWeight =
                        FontWeight.Bold
                )

                Text(

                    text =
                        formatoMoneda.format(total),

                    style =
                        MaterialTheme.typography.headlineLarge,

                    color =
                        MaterialTheme.colorScheme.primary
                )

                Spacer(
                    modifier =
                        Modifier.height(20.dp)
                )

                Button(

                    onClick = {

                        if (
                            cantidadNum <= 0 ||
                            fechaEntrega.isBlank()
                        ) {

                            Toast.makeText(
                                context,
                                "Completa todos los campos",
                                Toast.LENGTH_SHORT
                            ).show()

                            return@Button
                        }

                        if (cantidadNum > stock) {

                            Toast.makeText(
                                context,
                                "Stock insuficiente",
                                Toast.LENGTH_SHORT
                            ).show()

                            return@Button
                        }

                        mostrarDialogoProducto = true
                    },

                    modifier =
                        Modifier.fillMaxWidth()

                ) {

                    Text("Agregar al carrito")
                }
            }
        }
    }

    // DIALOGO FECHA
    if (mostrarDialogoFecha) {

        DatePickerDialog(

            onDismissRequest = {

                mostrarDialogoFecha = false
            },

            confirmButton = {

                TextButton(

                    onClick = {

                        val millis =
                            estadoFecha.selectedDateMillis

                        if (millis != null) {

                            fechaEntrega =
                                SimpleDateFormat(
                                    "dd/MM/yyyy",
                                    Locale.getDefault()
                                ).format(
                                    Date(millis)
                                )
                        }

                        mostrarDialogoFecha = false
                    }

                ) {

                    Text("Aceptar")
                }
            }

        ) {

            DatePicker(
                state = estadoFecha
            )
        }
    }

    // DIALOGO PRODUCTO SELECCIONADO
    if (mostrarDialogoProducto) {

        AlertDialog(

            onDismissRequest = {

                mostrarDialogoProducto = false
            },

            title = {

                Text("Producto seleccionado")
            },

            text = {

                Column {

                    Text(
                        "Producto: $nombre"
                    )

                    Text(
                        "Cantidad: $cantidadNum"
                    )

                    Text(
                        "Dirección: $direccionPerfil"
                    )

                    Text(
                        "Entrega: $fechaEntrega"
                    )

                    Spacer(
                        modifier =
                            Modifier.height(12.dp)
                    )

                    Text(

                        text =
                            "Total: ${
                                formatoMoneda.format(
                                    total
                                )
                            }",

                        fontWeight =
                            FontWeight.Bold
                    )
                }
            },

            confirmButton = {

                TextButton(

                    onClick = {

                        coroutineScope.launch {

                            try {

                                withContext(
                                    Dispatchers.IO
                                ) {

                                    // SOLO GUARDAR EN CARRITO
                                    val pedidoLocal =
                                        Pedido(

                                            producto =
                                                nombre,

                                            cantidad =
                                                cantidadNum,

                                            total =
                                                total,

                                            direccion =
                                                direccionPerfil,

                                            fecha_entrega =
                                                fechaEntrega,

                                            usuario =
                                                usuarioActual?.nombre
                                                    ?: "Invitado"
                                        )

                                    CarritoRepository
                                        .agregarProducto(
                                            pedidoLocal
                                        )

                                    // HISTORIAL LOCAL
                                    val productoHistorial =
                                        Producto(

                                            nombre =
                                                nombre,

                                            precio =
                                                formatoMoneda.format(
                                                    total
                                                ),

                                            stock = 0,

                                            cantidad =
                                                cantidadNum.toString(),

                                            direccion =
                                                direccionPerfil
                                        )

                                    PedidoHistorial
                                        .agregar(
                                            productoHistorial
                                        )
                                }

                                Toast.makeText(

                                    context,

                                    "Producto agregado al carrito",

                                    Toast.LENGTH_SHORT

                                ).show()

                                mostrarDialogoProducto =
                                    false

                                navController.navigate(
                                    "carrito"
                                )

                            } catch (e: Exception) {

                                e.printStackTrace()

                                Toast.makeText(

                                    context,

                                    "Error al agregar producto",

                                    Toast.LENGTH_LONG

                                ).show()
                            }
                        }
                    }

                ) {

                    Text("Aceptar")
                }
            },

            dismissButton = {

                TextButton(

                    onClick = {

                        mostrarDialogoProducto = false
                    }

                ) {

                    Text("Cancelar")
                }
            }
        )
    }
}