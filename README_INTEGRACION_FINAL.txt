
# INTEGRACIÓN COMPLETA SUPABASE - CESTA ORGANICA

## IMPORTANTE
El proyecto ya contiene:
- dependencias
- SupabaseConfig.kt
- SupabaseUserRepository.kt
- SQL Script

## SOLO DEBES:

1. Crear proyecto en Supabase
2. Copiar URL y KEY
3. Reemplazar en SupabaseConfig.kt

---

# PASO FINAL REGISTRO CLOUD

Buscar en:
ui/registro/RegistrarseScreen.kt

Y reemplazar el bloque de registro por:

```kotlin
CoroutineScope(Dispatchers.IO).launch {

    SupabaseUserRepository.registrarUsuario(user)

    withContext(Dispatchers.Main) {
        navController.navigate("login")
    }
}
```

Agregar imports:

```kotlin
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.example.huertohogardefinitiveedition.supabase.SupabaseUserRepository
```

---

# LOGIN CLOUD (OPCIONAL)

Actualmente el login sigue local.

Si quieres login cloud:
- crear función buscarUsuario()
- validar password desde Supabase

Para la evaluación parcial 2 NO es obligatorio.
Lo importante es demostrar persistencia cloud.

---

# ESTRUCTURA FINAL

Android Studio
↓
Supabase Cloud
↓
PostgreSQL

---

# QA Y PROD

QA:
cesta-organica-qa

PROD:
cesta-organica-prod

---

# RESPALDO

Supabase:
Settings -> Database -> Backups

Esto sirve como evidencia para la rúbrica.

