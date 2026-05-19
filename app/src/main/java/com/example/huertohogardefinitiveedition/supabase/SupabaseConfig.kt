package com.example.huertohogardefinitiveedition.supabase

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.gotrue.GoTrue
import io.github.jan.supabase.postgrest.Postgrest

object SupabaseConfig {

    // REEMPLAZAR CON TUS DATOS DE SUPABASE
    private const val SUPABASE_URL = "https://TU-PROYECTO.supabase.co"
    private const val SUPABASE_KEY = "TU_ANON_KEY"

    val client: SupabaseClient = createSupabaseClient(
        supabaseUrl = SUPABASE_URL,
        supabaseKey = SUPABASE_KEY
    ) {
        install(Postgrest)
        install(GoTrue)
    }
}