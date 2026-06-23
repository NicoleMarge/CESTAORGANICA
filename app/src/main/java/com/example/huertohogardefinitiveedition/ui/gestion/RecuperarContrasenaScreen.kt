package com.example.huertohogardefinitiveedition.ui.gestion

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.huertohogardefinitiveedition.data.repository.UserRepository

@Composable
fun RecuperarContrasenaScreen(navController: NavController) {

    val colors = lightColorScheme(
        primary = Color(0xFF4CAF50),
        onPrimary = Color.White,
        secondary = Color(0xFFFF9800),
        onSecondary = Color.White,
        surface = Color(0xFFFFF8F5),
        onSurface = Color(0xFF3A3A3A),
        error = Color(0xFFB00020)
    )

    MaterialTheme(colorScheme = colors) {

        // CAMPOS
        var usuario by remember { mutableStateOf("") }
        var correo by remember { mutableStateOf("") }
        var pass by remember { mutableStateOf("") }
        var confirm by remember { mutableStateOf("") }

        // VISIBILIDAD PASSWORD
        var showPass by remember { mutableStateOf(false) }
        var showConfirm by remember { mutableStateOf(false) }

        // ERRORES
        var generalErr by remember { mutableStateOf<String?>(null) }

        // DIALOGO
        var okDialog by remember { mutableStateOf(false) }

        Scaffold { inner ->

            Column(
                modifier = Modifier
                    .padding(inner)
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = "Recuperar contraseña",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(16.dp))

                Column(
                    modifier = Modifier.fillMaxWidth(0.9f),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {

                    // USUARIO
                    OutlinedTextField(
                        value = usuario,
                        onValueChange = {
                            usuario = it
                            generalErr = null
                        },
                        label = {
                            Text("Usuario")
                        },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    // CORREO
                    OutlinedTextField(
                        value = correo,
                        onValueChange = {
                            correo = it
                            generalErr = null
                        },
                        label = {
                            Text("Correo registrado")
                        },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Email
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    // NUEVA PASSWORD
                    OutlinedTextField(
                        value = pass,
                        onValueChange = {
                            pass = it
                            generalErr = null
                        },
                        label = {
                            Text("Nueva contraseña")
                        },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password
                        ),
                        visualTransformation =
                            if (showPass)
                                VisualTransformation.None
                            else
                                PasswordVisualTransformation(),
                        trailingIcon = {

                            IconButton(
                                onClick = {
                                    showPass = !showPass
                                }
                            ) {

                                Icon(
                                    imageVector =
                                        if (showPass)
                                            Icons.Default.VisibilityOff
                                        else
                                            Icons.Default.Visibility,
                                    contentDescription = null
                                )
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    )

                    // CONFIRMAR PASSWORD
                    OutlinedTextField(
                        value = confirm,
                        onValueChange = {
                            confirm = it
                            generalErr = null
                        },
                        label = {
                            Text("Confirmar contraseña")
                        },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password
                        ),
                        visualTransformation =
                            if (showConfirm)
                                VisualTransformation.None
                            else
                                PasswordVisualTransformation(),
                        trailingIcon = {

                            IconButton(
                                onClick = {
                                    showConfirm = !showConfirm
                                }
                            ) {

                                Icon(
                                    imageVector =
                                        if (showConfirm)
                                            Icons.Default.VisibilityOff
                                        else
                                            Icons.Default.Visibility,
                                    contentDescription = null
                                )
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                // ERROR GENERAL
                if (generalErr != null) {

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = generalErr!!,
                        color = MaterialTheme.colorScheme.error
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // BOTON CAMBIAR PASSWORD
                Button(
                    onClick = {

                        generalErr = null

                        // VALIDAR CAMPOS
                        if (
                            usuario.isBlank() ||
                            correo.isBlank() ||
                            pass.isBlank() ||
                            confirm.isBlank()
                        ) {

                            generalErr =
                                "Complete todos los campos"

                            return@Button
                        }

                        // VALIDAR PASSWORDS
                        if (pass != confirm) {

                            generalErr =
                                "Las contraseñas no coinciden"

                            return@Button
                        }

                        // RECUPERAR PASSWORD
                        val result =
                            UserRepository.recuperarPassword(
                                usuario,
                                correo,
                                pass
                            )

                        result.onSuccess {

                            okDialog = true

                        }.onFailure {

                            generalErr =
                                it.message ?: "Error al actualizar contraseña"
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth(0.7f)
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {

                    Text("Cambiar contraseña")
                }

                Spacer(modifier = Modifier.height(10.dp))

                // BOTON VOLVER
                OutlinedButton(
                    onClick = {
                        navController.popBackStack()
                    },
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.secondary
                    )
                ) {

                    Text("Volver")
                }
            }

            // DIALOGO EXITO
            if (okDialog) {

                AlertDialog(
                    onDismissRequest = {
                        okDialog = false
                    },
                    title = {
                        Text("Éxito")
                    },
                    text = {
                        Text(
                            "La contraseña fue actualizada correctamente"
                        )
                    },
                    confirmButton = {

                        TextButton(
                            onClick = {

                                okDialog = false

                                navController.navigate("login") {

                                    popUpTo("login") {
                                        inclusive = true
                                    }

                                    launchSingleTop = true
                                }
                            }
                        ) {

                            Text("Ir al login")
                        }
                    }
                )
            }
        }
    }
}