package com.example.huertohogardefinitiveedition.view

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

import com.example.huertohogardefinitiveedition.data.database.PedidoHistorial
import com.example.huertohogardefinitiveedition.data.model.Pedido
import com.example.huertohogardefinitiveedition.data.model.Producto
import com.example.huertohogardefinitiveedition.data.repository.CarritoRepository
import com.example.huertohogardefinitiveedition.data.repository.ResenaRepository
import com.example.huertohogardefinitiveedition.data.session.SessionManager
import com.example.huertohogardefinitiveedition.viewmodel.DrawerMenuViewModel

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
        usuarioActual?.direccion ?: "Sin dirección"

    var cantidad by remember { mutableStateOf("1") }
    var fechaEntrega by remember { mutableStateOf("") }

    // 🌟 Estados para controlar el Formulario de la Nueva Opinión
    var nuevoComentario by remember { mutableStateOf("") }
    var nuevaCalificacion by remember { mutableStateOf(5) }

    var mostrarDialogoProducto by remember { mutableStateOf(false) }
    var mostrarDialogoFecha by remember { mutableStateOf(false) }

    val estadoFecha = rememberDatePickerState()

    val precioBase = precio.toIntOrNull() ?: 0
    val cantidadNum = cantidad.toIntOrNull() ?: 0
    val total = precioBase * cantidadNum

    val formatoMoneda = remember {
        NumberFormat.getCurrencyInstance(Locale("es", "CL"))
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        item {

            //------------------------------------------------
            // PRODUCTO
            //------------------------------------------------

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = nombre,
                    style = MaterialTheme.typography.headlineMedium
                )

                Spacer(Modifier.height(8.dp))

                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {

                    Text(
                        text = "Precio: ${formatoMoneda.format(precioBase)}"
                    )

                    Spacer(Modifier.width(12.dp))

                    Text(
                        text = "Stock: $stock"
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            Text(text = descripcion)

            Spacer(Modifier.height(20.dp))

            //------------------------------------------------
            // CANTIDAD
            //------------------------------------------------

            OutlinedTextField(
                value = cantidad,
                onValueChange = {
                    cantidad = it.filter { c -> c.isDigit() }
                },
                label = { Text("Cantidad") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(12.dp))

            //------------------------------------------------
            // FECHA
            //------------------------------------------------

            OutlinedTextField(
                value = fechaEntrega,
                onValueChange = {},
                readOnly = true,
                label = { Text("Fecha entrega") },
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = null,
                        modifier = Modifier.clickable {
                            mostrarDialogoFecha = true
                        }
                    )
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(20.dp))

            //------------------------------------------------
            // TOTAL
            //------------------------------------------------

            Text(
                text = "TOTAL",
                fontWeight = FontWeight.Bold
            )

            Text(
                text = formatoMoneda.format(total),
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(Modifier.height(20.dp))

            //------------------------------------------------
            // AGREGAR CARRITO
            //------------------------------------------------

            Button(
                onClick = {

                    if (cantidadNum <= 0 || fechaEntrega.isBlank()) {
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
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF2E7D32),
                    contentColor = Color.White
                )
            ) {
                Text("Agregar al carrito")
            }

            Spacer(Modifier.height(24.dp))

            Divider()

            Spacer(Modifier.height(16.dp))

            //------------------------------------------------
            // RESEÑAS EXISTENTES
            //------------------------------------------------

            Text(
                text = "Reseñas de Usuarios",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(Modifier.height(12.dp))

            val resenasProducto =
                ResenaRepository.obtenerResenasPorProducto(nombre)

            if (resenasProducto.isEmpty()) {
                Text(
                    text = "Este producto aún no tiene reseñas. ¡Sé el primero!",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            } else {
                resenasProducto.forEach { resena ->
                    CardResena(resena = resena)
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Divider()
            Spacer(modifier = Modifier.height(16.dp))

            //------------------------------------------------
            // ✍️ FORMULARIO NUEVA RESEÑA (INTERACTIVO)
            //------------------------------------------------
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F1F1))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Deja tu opinión",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Selector interactivo de Estrellas
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(text = "Calificación: ", style = MaterialTheme.typography.bodyMedium)
                        repeat(5) { index ->
                            val estrellaNum = index + 1
                            val icon = if (estrellaNum <= nuevaCalificacion) Icons.Default.Star else Icons.Default.StarBorder
                            val tint = if (estrellaNum <= nuevaCalificacion) Color(0xFFFFC107) else Color.Gray

                            Icon(
                                imageVector = icon,
                                contentDescription = null,
                                tint = tint,
                                modifier = Modifier
                                    .size(28.dp)
                                    .clickable { nuevaCalificacion = estrellaNum }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Input para escribir el comentario
                    OutlinedTextField(
                        value = nuevoComentario,
                        onValueChange = { nuevoComentario = it },
                        label = { Text("Escribe aquí tu experiencia...") },
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 3
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Botón Publicar Reseña Corregido sin errores de tipos
                    Button(
                        onClick = {
                            if (nuevoComentario.isBlank()) {
                                Toast.makeText(context, "El comentario no puede estar vacío", Toast.LENGTH_SHORT).show()
                                return@Button
                            }

                            coroutineScope.launch {
                                try {
                                    withContext(Dispatchers.IO) {
                                        // 🌟 Inserción directa de parámetros según ResenaRepository
                                        ResenaRepository.agregarResena(
                                            nombreProducto = nombre,
                                            idUsuario = usuarioActual?.idUsuario ?: 0,
                                            nombreUsuario = usuarioActual?.nombre ?: "Invitado",
                                            calificacion = nuevaCalificacion,
                                            comentario = nuevoComentario
                                        )
                                    }

                                    Toast.makeText(context, "¡Reseña agregada!", Toast.LENGTH_SHORT).show()
                                    nuevoComentario = ""
                                    nuevaCalificacion = 5

                                    // Forzar recarga automática de la vista
                                    navController.popBackStack()
                                    navController.navigate("productoForm/$nombre/$precio/$descripcion/$stock")

                                } catch (e: Exception) {
                                    e.printStackTrace()
                                    Toast.makeText(context, "Error al guardar reseña", Toast.LENGTH_SHORT).show()
                                }
                            }
                        },
                        modifier = Modifier.align(Alignment.End),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32))
                    ) {
                        Text("Publicar")
                    }
                }
            }

            //------------------------------------------------
            // BOTÓN VOLVER ABAJO
            //------------------------------------------------

            Spacer(Modifier.height(24.dp))

            Button(
                onClick = {
                    navController.popBackStack()
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.LightGray
                )
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                    contentDescription = null,
                    tint = Color.Black
                )

                Spacer(modifier = Modifier.width(6.dp))

                Text(
                    text = "VOLVER",
                    color = Color.Black
                )
            }

            Spacer(Modifier.height(40.dp))
        }
    }

    //------------------------------------------------
    // DIALOG FECHA
    //------------------------------------------------

    if (mostrarDialogoFecha) {

        DatePickerDialog(
            onDismissRequest = {
                mostrarDialogoFecha = false
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        val millis = estadoFecha.selectedDateMillis
                        if (millis != null) {
                            fechaEntrega = SimpleDateFormat(
                                "dd/MM/yyyy",
                                Locale.getDefault()
                            ).format(Date(millis))
                        }
                        mostrarDialogoFecha = false
                    }
                ) {
                    Text("Aceptar")
                }
            }
        ) {
            DatePicker(state = estadoFecha)
        }
    }

    //------------------------------------------------
    // DIALOG PRODUCTO
    //------------------------------------------------

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
                    Text("Producto: $nombre")
                    Text("Cantidad: $cantidadNum")
                    Text("Dirección: $direccionPerfil")
                    Text("Entrega: $fechaEntrega")

                    Spacer(Modifier.height(12.dp))

                    Text(
                        text = "Total: ${formatoMoneda.format(total)}",
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        coroutineScope.launch {
                            try {
                                withContext(Dispatchers.IO) {
                                    val pedidoLocal = Pedido(
                                        producto = nombre,
                                        cantidad = cantidadNum,
                                        total = total,
                                        direccion = direccionPerfil,
                                        fecha_entrega = fechaEntrega,
                                        usuario = usuarioActual?.nombre ?: "Invitado"
                                    )

                                    CarritoRepository.agregarProducto(pedidoLocal)

                                    val productoHistorial = Producto(
                                        nombre = nombre,
                                        precio = formatoMoneda.format(total),
                                        stock = 0,
                                        cantidad = cantidadNum.toString(),
                                        direccion = direccionPerfil
                                    )

                                    PedidoHistorial.agregar(productoHistorial)
                                }

                                Toast.makeText(
                                    context,
                                    "Producto agregado al carrito",
                                    Toast.LENGTH_SHORT
                                ).show()

                                mostrarDialogoProducto = false
                                navController.navigate("carrito")

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