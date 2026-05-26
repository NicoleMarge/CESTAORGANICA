package com.example.huertohogardefinitiveedition.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.huertohogardefinitiveedition.R
import com.example.huertohogardefinitiveedition.data.model.Categoria
import com.example.huertohogardefinitiveedition.data.model.ProductoItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*

private val listaInicial = listOf(

    Categoria(
        "Frutas",
        Icons.Default.Agriculture,

        listOf(

            ProductoItem(
                "Manzanas Fuji",
                "1200",
                "Manzanas Fuji crujientes y dulces...",
                50,
                R.drawable.manzana_fuji
            ),

            ProductoItem(
                "Naranjas Valencia",
                "1000",
                "Jugosas y ricas en vitamina C...",
                30,
                R.drawable.naranja_valencia
            ),

            ProductoItem(
                "Plátanos Cavendish",
                "800",
                "Plátanos maduros y dulces...",
                100,
                R.drawable.platano_cavendish
            )
        )
    ),

    Categoria(
        "Verduras",
        Icons.Default.Grass,

        listOf(

            ProductoItem(
                "Zanahorias Orgánicas",
                "900",
                "Zanahorias crujientes...",
                40,
                R.drawable.zanahorias
            ),

            ProductoItem(
                "Espinacas Frescas",
                "700",
                "Espinacas frescas...",
                25,
                R.drawable.espinaca
            )
        )
    )
)

class DrawerMenuViewModel : ViewModel() {

    var categorias = mutableStateOf(listaInicial)
        private set

    fun actualizarStock(
        nombreProducto: String,
        cantidadComprada: Int
    ) {

        categorias.value = categorias.value.map { categoria ->

            categoria.copy(

                productos = categoria.productos.map { producto ->

                    if (producto.nombre == nombreProducto) {

                        producto.copy(
                            stock = (producto.stock - cantidadComprada)
                                .coerceAtLeast(0)
                        )

                    } else {

                        producto
                    }
                }
            )
        }
    }
}