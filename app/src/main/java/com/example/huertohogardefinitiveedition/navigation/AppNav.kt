package com.example.huertohogardefinitiveedition.navigation

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

import com.example.huertohogardefinitiveedition.ui.gestion.GestionPerfilScreen
import com.example.huertohogardefinitiveedition.ui.gestion.GestionUsuarioScreen
import com.example.huertohogardefinitiveedition.ui.gestion.RecuperarContrasenaScreen
import com.example.huertohogardefinitiveedition.ui.login.LoginScreen
import com.example.huertohogardefinitiveedition.ui.registro.RegistrarseScreen

import com.example.huertohogardefinitiveedition.view.BlockScreen
import com.example.huertohogardefinitiveedition.view.CarritoScreen
import com.example.huertohogardefinitiveedition.view.DrawerMenu
import com.example.huertohogardefinitiveedition.view.HistorialPedidosScreen
import com.example.huertohogardefinitiveedition.view.ProductoFormScreen
import com.example.huertohogardefinitiveedition.view.QrScannerScreen

import com.example.huertohogardefinitiveedition.viewmodel.DrawerMenuViewModel
import com.example.huertohogardefinitiveedition.viewmodel.QrViewModel

@Composable
fun AppNav(
    hasCameraPermission: Boolean,
    onRequestPermission: () -> Unit
) {

    val navController = rememberNavController()

    val drawerMenuViewModel: DrawerMenuViewModel =
        viewModel()

    NavHost(
        navController = navController,
        startDestination = "login"
    ) {

        //------------------------------------------------
        // LOGIN Y REGISTRO
        //------------------------------------------------

        composable("login") {

            LoginScreen(
                navController = navController
            )
        }

        composable("registrarse") {

            RegistrarseScreen(
                navController = navController
            )
        }

        composable("recuperar_contrasena") {

            RecuperarContrasenaScreen(
                navController = navController
            )
        }

        //------------------------------------------------
        // CATÁLOGO PRINCIPAL
        //------------------------------------------------

        composable(
            route = "DrawerMenu/{username}",

            arguments = listOf(
                navArgument("username") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->

            val usernameArg =
                backStackEntry.arguments
                    ?.getString("username")
                    ?: ""

            DrawerMenu(
                username = usernameArg,
                navController = navController,
                viewModel = drawerMenuViewModel
            )
        }

        //------------------------------------------------
        // PRODUCTOS
        //------------------------------------------------

        composable(
            route =
                "ProductoFormScreen/{nombre}/{precio}/{descripcion}/{stock}",

            arguments = listOf(

                navArgument("nombre") {
                    type = NavType.StringType
                },

                navArgument("precio") {
                    type = NavType.StringType
                },

                navArgument("descripcion") {
                    type = NavType.StringType
                },

                navArgument("stock") {
                    type = NavType.IntType
                }
            )

        ) { backStackEntry ->

            val nombre =
                Uri.decode(
                    backStackEntry.arguments
                        ?.getString("nombre")
                        ?: ""
                )

            val precio =
                backStackEntry.arguments
                    ?.getString("precio")
                    ?: ""

            val descripcion =
                Uri.decode(
                    backStackEntry.arguments
                        ?.getString("descripcion")
                        ?: ""
                )

            val stock =
                backStackEntry.arguments
                    ?.getInt("stock")
                    ?: 0

            ProductoFormScreen(
                navController = navController,
                nombre = nombre,
                precio = precio,
                descripcion = descripcion,
                stock = stock,
                viewModel = drawerMenuViewModel
            )
        }

        //------------------------------------------------
        // QR
        //------------------------------------------------

        composable("QRScannerScreen") {

            val qrViewModel: QrViewModel =
                viewModel()

            QrScannerScreen(
                viewModel = qrViewModel,
                hasCameraPermission = hasCameraPermission,
                onRequestPermission = onRequestPermission
            )
        }

        //------------------------------------------------
        // PERFIL
        //------------------------------------------------

        composable(
            "gestion_perfil"
        ) {

            GestionPerfilScreen(
                navController = navController
            )
        }

        //------------------------------------------------
        // HISTORIAL
        //------------------------------------------------

        composable(
            "historial_pedidos"
        ) {

            HistorialPedidosScreen()
        }

        //------------------------------------------------
        // CARRITO
        //------------------------------------------------

        composable(
            "carrito"
        ) {

            CarritoScreen()
        }

        //------------------------------------------------
        // ADMIN
        //------------------------------------------------

        composable(
            "gestion_usuarios"
        ) {

            GestionUsuarioScreen(
                navController = navController
            )
        }

        //------------------------------------------------
        // BLOCK
        //------------------------------------------------

        composable(
            "block"
        ) {

            BlockScreen()
        }

    }
}