package com.example.huertohogardefinitiveedition.supabase

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest

object SupabaseConfig {

    // REEMPLAZAR CON TUS DATOS DE SUPABASE
    private const val SUPABASE_URL = "https://htseatyrphfyrjmrqtqv.supabase.co/"
    private const val SUPABASE_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Imh0c2VhdHlycGhmeXJqbXJxdHF2Iiwicm9sZSI6ImFub24iLCJpYXQiOjE3NzkxNTc2MjMsImV4cCI6MjA5NDczMzYyM30.-DgP4NM3F7Ckd8HwxREck8X8LOkDTTv1ZMGYEvW0FTs"

    val client: SupabaseClient = createSupabaseClient(
        supabaseUrl = SUPABASE_URL,
        supabaseKey = SUPABASE_KEY
    ) {
        install(Postgrest)
    }
}
