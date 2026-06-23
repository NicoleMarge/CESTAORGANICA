package com.example.huertohogardefinitiveedition.data.model

import kotlinx.serialization.SerialName // 🌟 IMPORTACIÓN REQUERIDA
import kotlinx.serialization.Serializable

// Representa a un usuario del sistema
@Serializable
data class Credential(

    // 🌟 ENLACE CLAVE: Le dice a Kotlin que busque la columna "id" de Supabase y la guarde en idUsuario
    @SerialName("id")
    val idUsuario: Int = 0,

    // Nombre completo
    val nombre: String,

    // Correo electrónico
    val correo: String,

    // Nombre de usuario único
    val usuario: String,

    // Teléfono (9 dígitos)
    val telefono: String,

    // Dirección de entrega
    val direccion: String,

    // Contraseña
    val password: String

) {

    companion object {

        // Usuario administrador por defecto
        val Admin = Credential(
            idUsuario = 1,
            nombre = "Administrador del Sistema",
            correo = "admin@duoc.cl",
            usuario = "admin",
            telefono = "000000000",
            direccion = "Sede Central DuocUC",
            password = "123"
        )
    }
}