package com.example.huertohogardefinitiveedition.data.repository

import com.example.huertohogardefinitiveedition.data.model.Credential
import com.example.huertohogardefinitiveedition.supabase.SupabaseConfig
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.runBlocking

object UserRepository {

    // =========================
    // UTILIDADES
    // =========================

    private fun norm(s: String) = s.trim().lowercase()

    private fun isStrongPassword(s: String): Boolean {
        if (s.length < 8) return false
        val hasLetter = s.any { it.isLetter() }
        val hasDigit = s.any { it.isDigit() }
        return hasLetter && hasDigit
    }

    private fun isValidEmail(s: String): Boolean {
        val emailRegex = Regex("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")
        return s.matches(emailRegex)
    }

    private fun isPhoneCL9(s: String): Boolean {
        val digits = s.filter { it.isDigit() }
        return digits.length == 9
    }

    // =========================
    // REGISTRO EN SUPABASE
    // =========================

    fun register(user: Credential): Result<Credential> = runBlocking {

        return@runBlocking try {

            val correo = user.correo.trim().lowercase()
            val usuario = user.usuario.trim().lowercase()

            // VALIDACIONES
            if (!isValidEmail(correo)) {
                return@runBlocking Result.failure(
                    IllegalArgumentException("Correo inválido")
                )
            }

            if (!isPhoneCL9(user.telefono)) {
                return@runBlocking Result.failure(
                    IllegalArgumentException("El teléfono debe tener 9 dígitos")
                )
            }

            if (user.nombre.isBlank() || user.direccion.isBlank()) {
                return@runBlocking Result.failure(
                    IllegalArgumentException("Nombre y dirección son obligatorios")
                )
            }

            if (!isStrongPassword(user.password)) {
                return@runBlocking Result.failure(
                    IllegalArgumentException("La contraseña no cumple los requisitos")
                )
            }

            // BUSCAR SI EXISTE USUARIO
            val usuariosExistentes = SupabaseConfig.client
                .from("usuarios")
                .select()
                .decodeList<Credential>()

            val existeUsuario = usuariosExistentes.any {
                norm(it.usuario) == usuario
            }

            val existeCorreo = usuariosExistentes.any {
                norm(it.correo) == correo
            }

            if (existeUsuario) {
                return@runBlocking Result.failure(
                    IllegalArgumentException("El usuario ya existe")
                )
            }

            if (existeCorreo) {
                return@runBlocking Result.failure(
                    IllegalArgumentException("Ya existe una cuenta con ese correo")
                )
            }

            // NUEVO USUARIO
            val nuevoUsuario = user.copy(
                usuario = usuario,
                correo = correo
            )

            // INSERTAR EN SUPABASE
            SupabaseConfig.client
                .from("usuarios")
                .insert(nuevoUsuario)

            Result.success(nuevoUsuario)

        } catch (e: Exception) {

            Result.failure(e)

        }
    }

    // =========================
    // LOGIN DESDE SUPABASE
    // =========================

    fun login(
        usernameOrEmail: String,
        password: String
    ): Result<Credential> = runBlocking {

        return@runBlocking try {

            val q = usernameOrEmail.trim().lowercase()

            val usuarios = SupabaseConfig.client
                .from("usuarios")
                .select()
                .decodeList<Credential>()

            val usuario = usuarios.firstOrNull {

                (
                        norm(it.usuario) == q ||
                                norm(it.correo) == q
                        ) &&
                        it.password == password
            }

            if (usuario != null) {
                Result.success(usuario)
            } else {
                Result.failure(
                    IllegalArgumentException("Credenciales inválidas")
                )
            }

        } catch (e: Exception) {

            Result.failure(e)

        }
    }

    // =========================
    // RECUPERAR CONTRASEÑA
    // =========================

    fun updatePassword(
        usernameOrEmail: String,
        newPassword: String
    ): Result<Unit> = runBlocking {

        return@runBlocking try {

            val q = usernameOrEmail.trim().lowercase()

            if (!isStrongPassword(newPassword)) {
                return@runBlocking Result.failure(
                    IllegalArgumentException(
                        "La nueva contraseña no cumple los requisitos"
                    )
                )
            }

            val usuarios = SupabaseConfig.client
                .from("usuarios")
                .select()
                .decodeList<Credential>()

            val usuario = usuarios.firstOrNull {
                norm(it.usuario) == q ||
                        norm(it.correo) == q
            }

            if (usuario == null) {
                return@runBlocking Result.failure(
                    IllegalArgumentException("Usuario no encontrado")
                )
            }

            val actualizado = usuario.copy(
                password = newPassword
            )

            SupabaseConfig.client
                .from("usuarios")
                .update(actualizado) {
                    filter {
                        eq("idUsuario", usuario.idUsuario)
                    }
                }

            Result.success(Unit)

        } catch (e: Exception) {

            Result.failure(e)

        }
    }

    // =========================
    // ACTUALIZAR USUARIO
    // =========================

    fun update(user: Credential): Result<Credential> = runBlocking {

        return@runBlocking try {

            if (!isValidEmail(user.correo)) {
                return@runBlocking Result.failure(
                    IllegalArgumentException("Correo inválido")
                )
            }

            if (!isPhoneCL9(user.telefono)) {
                return@runBlocking Result.failure(
                    IllegalArgumentException("El teléfono debe tener 9 dígitos")
                )
            }

            if (user.nombre.isBlank() || user.direccion.isBlank()) {
                return@runBlocking Result.failure(
                    IllegalArgumentException(
                        "Nombre y dirección son obligatorios"
                    )
                )
            }

            if (!isStrongPassword(user.password)) {
                return@runBlocking Result.failure(
                    IllegalArgumentException(
                        "La contraseña no cumple los requisitos"
                    )
                )
            }

            val actualizado = user.copy(
                usuario = user.usuario.trim().lowercase(),
                correo = user.correo.trim().lowercase()
            )

            SupabaseConfig.client
                .from("usuarios")
                .update(actualizado) {
                    filter {
                        eq("idUsuario", user.idUsuario)
                    }
                }

            Result.success(actualizado)

        } catch (e: Exception) {

            Result.failure(e)

        }
    }

    // =========================
    // ACTUALIZAR PERFIL
    // =========================

    fun updateProfile(
        idUsuario: Int,
        nombre: String,
        telefono: String,
        direccion: String
    ): Result<Credential> = runBlocking {

        return@runBlocking try {

            if (nombre.isBlank()) {
                return@runBlocking Result.failure(
                    IllegalArgumentException("El nombre es obligatorio")
                )
            }

            if (direccion.isBlank()) {
                return@runBlocking Result.failure(
                    IllegalArgumentException("La dirección es obligatoria")
                )
            }

            if (!isPhoneCL9(telefono)) {
                return@runBlocking Result.failure(
                    IllegalArgumentException(
                        "El teléfono debe tener 9 dígitos"
                    )
                )
            }

            val usuarios = SupabaseConfig.client
                .from("usuarios")
                .select()
                .decodeList<Credential>()

            val usuario = usuarios.firstOrNull {
                it.idUsuario == idUsuario
            }

            if (usuario == null) {
                return@runBlocking Result.failure(
                    IllegalArgumentException("Usuario no encontrado")
                )
            }

            val actualizado = usuario.copy(
                nombre = nombre,
                telefono = telefono.filter { it.isDigit() }.take(9),
                direccion = direccion
            )

            SupabaseConfig.client
                .from("usuarios")
                .update(actualizado) {
                    filter {
                        eq("idUsuario", idUsuario)
                    }
                }

            Result.success(actualizado)

        } catch (e: Exception) {

            Result.failure(e)

        }
    }

    // =========================
    // OBTENER USUARIO POR ID
    // =========================

    fun getById(id: Int): Credential? = runBlocking {

        try {

            val usuarios = SupabaseConfig.client
                .from("usuarios")
                .select()
                .decodeList<Credential>()

            usuarios.firstOrNull {
                it.idUsuario == id
            }

        } catch (e: Exception) {

            null

        }
    }

    // =========================
    // LISTAR TODOS
    // =========================

    fun all(): List<Credential> = runBlocking {

        try {

            SupabaseConfig.client
                .from("usuarios")
                .select()
                .decodeList<Credential>()

        } catch (e: Exception) {

            emptyList()

        }
    }
}