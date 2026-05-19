package com.example.huertohogardefinitiveedition.supabase

import com.example.huertohogardefinitiveedition.data.model.Credential
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import kotlinx.serialization.Serializable

@Serializable
data class UsuarioSupabase(
    val nombre: String,
    val correo: String,
    val usuario: String,
    val telefono: String,
    val direccion: String,
    val password: String
)

object SupabaseUserRepository {

    suspend fun registrarUsuario(user: Credential) {
        val nuevoUsuario = UsuarioSupabase(
            nombre = user.nombre,
            correo = user.correo,
            usuario = user.usuario,
            telefono = user.telefono,
            direccion = user.direccion,
            password = user.password
        )

        SupabaseConfig.client
            .from("usuarios")
            .insert(nuevoUsuario)
    }

    suspend fun obtenerUsuarios(): List<UsuarioSupabase> {
        return SupabaseConfig.client
            .from("usuarios")
            .select(columns = Columns.list("*"))
            .decodeList<UsuarioSupabase>()
    }
}