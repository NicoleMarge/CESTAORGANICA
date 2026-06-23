# Cesta Orgánica 🥦🍎

Aplicación móvil Android para la venta de productos orgánicos (frutas, verduras y orgánicos), desarrollada en **Kotlin** con **Jetpack Compose**. Permite a los usuarios explorar un catálogo, comprar como invitado o registrado, gestionar un carrito, dejar reseñas de productos comprados, y revisar su historial de pedidos. Incluye un panel de administración para gestión de stock y pedidos.

## 📱 Características principales

- **Catálogo de productos** filtrable por categoría (Frutas, Verduras, Orgánicos, Todos), con buscador.
- **Modo invitado**: permite navegar el catálogo y comprar sin necesidad de registrarse, solicitando datos de contacto (nombre, correo, teléfono, dirección) al momento de pagar.
- **Autenticación de usuarios** con registro y login, contraseñas protegidas con **hash bcrypt** (vía `pgcrypto` en Supabase).
- **Carrito de compras** persistente en el dispositivo (Room), con cálculo automático de subtotales.
- **Historial de pedidos** ordenado por fecha real (más reciente primero), con vista de detalle completo por orden (productos, dirección de entrega, datos de contacto, estado).
- **Sistema de reseñas** con calificación de 1 a 5 estrellas y comentario, restringido a usuarios que efectivamente compraron el producto.
- **Modo invitado vs. usuario registrado**: navegación y permisos diferenciados (ej. favoritos e historial solo para usuarios registrados).

## 🧪 Estado del proyecto

Proyecto desarrollado como parte de la práctica profesional / taller aplicado de programación. Actualmente migrado de Firebase a Supabase como backend principal, con foco en:

- Corrección de estabilidad (manejo de errores de red sin crashear la app).
- Persistencia y sincronización de catálogo entre dispositivos.
- Seguridad de credenciales de usuario.
## 📄 Licencia

Proyecto académico, de uso educativo.
