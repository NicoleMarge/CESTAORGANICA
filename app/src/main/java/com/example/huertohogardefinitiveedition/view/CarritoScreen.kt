package com.example.huertohogardefinitiveedition.view

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.* // Asegura que importa ExperimentalMaterial3Api, TopAppBar, etc.
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

import com.example.huertohogardefinitiveedition.R
import com.example.huertohogardefinitiveedition.data.repository.CarritoRepository
import com.example.huertohogardefinitiveedition.data.session.SessionManager
import com.example.huertohogardefinitiveedition.supabase.PedidoRepository
import com.example.huertohogardefinitiveedition.supabase.PedidoSupabase
import com.example.huertohogardefinitiveedition.viewmodel.CarritoViewModel

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class) // 🌟 Requerido para usar TopAppBar
@Composable
fun CarritoScreen(
    vm: CarritoViewModel = viewModel(),
    volverProductos: () -> Unit
) {

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val carrito = CarritoRepository.obtenerCarrito()
    val totalCarrito = CarritoRepository.obtenerTotal()

    // 🖼 MAPA DE IMÁGENES
    fun obtenerImagen(producto: String): Int {
        val p = producto.lowercase()
        return when {
            p.contains("manzana") -> R.drawable.manzana_fuji
            p.contains("espinaca") -> R.drawable.espinaca
            p.contains("platano") || p.contains("plátano") -> R.drawable.platano_cavendish
            p.contains("naranja") -> R.drawable.naranja_valencia
            else -> R.drawable.logo_cesta
        }
    }

    Scaffold(
        containerColor = Color(0xFFF7F4F4),
        // 🌟 CABECERA VERDE AGREGADA AL CARRITO
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Carrito de Compras",
                        fontWeight = FontWeight.Bold,
                        color = Color.White // Letras blancas
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF4CAF50) // Verde corporativo idéntico a las otras
                )
            )
        }
    ) { padding -> // 🌟 Este 'padding' ahora tiene en cuenta el tamaño de la barra superior

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding) // Evita que los productos queden escondidos detrás de la franja verde
        ) {

            // 🛒 LISTA CARRITO
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                items(carrito) { pedido ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFFEDE8ED)
                        ),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 2.dp
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            // 🖼 IMAGEN
                            Image(
                                painter = painterResource(id = obtenerImagen(pedido.producto)),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(65.dp)
                                    .clip(RoundedCornerShape(10.dp))
                            )

                            Spacer(modifier = Modifier.width(12.dp))

                            // 📦 INFO PRODUCTO
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(end = 6.dp)
                            ) {
                                Text(
                                    text = pedido.producto,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black,
                                    maxLines = 2
                                )

                                Spacer(modifier = Modifier.height(2.dp))

                                Text(
                                    text = "$${pedido.total}",
                                    fontSize = 14.sp,
                                    color = Color.DarkGray
                                )

                                Spacer(modifier = Modifier.height(4.dp))

                                Text(
                                    text = "Entrega:",
                                    fontSize = 11.sp,
                                    color = Color.Gray
                                )

                                Text(
                                    text = pedido.fecha_entrega ?: "Sin fecha",
                                    fontSize = 11.sp,
                                    color = Color.Gray
                                )
                            }

                            // ➖➕🗑 CONTROLES
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    // ➖
                                    IconButton(
                                        onClick = {
                                            if (pedido.cantidad > 1) {
                                                CarritoRepository.actualizarCantidad(
                                                    pedido,
                                                    pedido.cantidad - 1
                                                )
                                            }
                                        }
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Remove,
                                            contentDescription = null,
                                            tint = Color.DarkGray,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }

                                    Text(
                                        text = pedido.cantidad.toString(),
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Medium
                                    )

                                    // ➕
                                    IconButton(
                                        onClick = {
                                            CarritoRepository.actualizarCantidad(
                                                pedido,
                                                pedido.cantidad + 1
                                            )
                                        }
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Add,
                                            contentDescription = null,
                                            tint = Color.DarkGray,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }

                                // 🗑 ELIMINAR
                                IconButton(
                                    onClick = {
                                        CarritoRepository.eliminarProducto(pedido)
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = null,
                                        tint = Color.Red,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // 💵 FOOTER
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFEAF6E9))
                    .padding(18.dp)
            ) {

                // 🔙 BOTÓN VOLVER
                Button(
                    onClick = { volverProductos() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(45.dp),
                    shape = RoundedCornerShape(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.LightGray
                    )
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                        contentDescription = null,
                        tint = Color.Black
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    Text(
                        text = "VOLVER",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // 💰 TOTAL
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Total:",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium
                    )

                    Text(
                        text = "$$totalCarrito",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF4CAF50)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // 💳 PAGAR
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
                                withContext(Dispatchers.IO) {
                                    val currentUser = SessionManager.currentUser

                                    if (currentUser?.usuario == "invitado") {
                                        withContext(Dispatchers.Main) {
                                            Toast.makeText(
                                                context,
                                                "Tiene que ingresar los datos mínimos solicitados",
                                                Toast.LENGTH_LONG
                                            ).show()
                                        }
                                        return@withContext
                                    }

                                    val idDelUsuarioLogueado = if (currentUser != null && currentUser.idUsuario != -1) {
                                        currentUser.idUsuario.toLong()
                                    } else {
                                        null
                                    }

                                    carrito.forEach { pedido ->
                                        val pedidoSupabase = PedidoSupabase(
                                            producto = pedido.producto,
                                            cantidad = pedido.cantidad,
                                            total = pedido.total,
                                            direccion = pedido.direccion,
                                            fecha_entrega = pedido.fecha_entrega,
                                            usuario_id = idDelUsuarioLogueado
                                        )

                                        PedidoRepository.guardarPedido(pedidoSupabase)
                                    }

                                    CarritoRepository.vaciarCarrito()
                                }

                                Toast.makeText(
                                    context,
                                    "Te estamos direccionando al pago...",
                                    Toast.LENGTH_LONG
                                ).show()

                                delay(2000)

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
                        .height(50.dp),
                    shape = RoundedCornerShape(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF4CAF50)
                    )
                ) {
                    Text(
                        text = "PAGAR AHORA",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    }
}