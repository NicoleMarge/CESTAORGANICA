🌱 Cesta Orgánica

Aplicación Android desarrollada en Kotlin con Jetpack Compose para la venta de productos orgánicos. Permite a los usuarios registrarse, iniciar sesión, explorar productos, gestionar un carrito de compras, generar/escanear códigos QR, dejar reseñas y consultar su historial de pedidos.

✨ Características


Autenticación de usuarios: registro, inicio de sesión y recuperación de contraseña.
Gestión de perfil: edición de datos de usuario.
Catálogo de productos: visualización y administración de productos por categoría.
Carrito de compras: agregar, modificar y eliminar productos antes de confirmar un pedido.
Historial de pedidos: consulta de compras anteriores.
Reseñas: los usuarios pueden calificar y comentar productos.
Código QR: generación y escaneo de QR (usando CameraX + ZXing), por ejemplo para identificar pedidos o productos.
Persistencia local: base de datos local con Room.
Persistencia en la nube: integración con Supabase (PostgreSQL) para usuarios y pedidos.


🛠️ Tecnologías utilizadas


Kotlin + Jetpack Compose (UI declarativa)
Navigation Compose para la navegación entre pantallas
Room (room-runtime, room-ktx) para persistencia local
CameraX + ZXing para escaneo de códigos QR
Material 3 y Material Icons Extended
Supabase como backend en la nube (autenticación y pedidos)
Coroutines para operaciones asíncronas


📂 Estructura del proyecto

app/src/main/java/com/example/huertohogardefinitiveedition/
├── data/
│   ├── dao/            # Acceso a datos (Room DAO)
│   ├── database/       # Configuración de la base de datos local
│   ├── model/           # Modelos de datos (Producto, Pedido, Resena, Credential, etc.)
│   ├── repository/      # Repositorios (Auth, Carrito, Pedido, Producto, Resena, User)
│   └── session/         # Manejo de sesión del usuario
├── navigation/          # Definición de rutas y navegación (AppNav)
├── supabase/            # Configuración e integración con Supabase
├── ui/
│   ├── gestion/         # Pantallas de gestión de perfil y usuario
│   ├── home/            # Pantalla principal
│   ├── login/           # Pantalla e UI state de login
│   ├── registro/        # Pantalla de registro
│   └── theme/           # Tema, colores y tipografía
├── utils/               # Utilidades (permisos de cámara, escáner QR)
├── view/                # Pantallas adicionales (carrito, historial, reseñas, formularios)
└── viewmodel/           # ViewModels de cada pantalla/funcionalidad

⚙️ Requisitos


Android Studio (versión reciente recomendada)
JDK 11 o superior
SDK de Android:

minSdk: 24
targetSdk / compileSdk: 36



Cuenta de Supabase para la persistencia en la nube


🚀 Configuración e instalación


Clona el repositorio:


bash   git clone https://github.com/NicoleMarge/CESTAORGANICA.git


Abre el proyecto en Android Studio.
Crea un proyecto en Supabase y ejecuta el script SUPABASE_SCRIPT.sql incluido en este repositorio para crear las tablas necesarias.
Copia la URL y la API Key de tu proyecto de Supabase y reemplázalas en:


   app/src/main/java/com/example/huertohogardefinitiveedition/supabase/SupabaseConfig.kt


Sincroniza el proyecto con Gradle y ejecuta la app en un emulador o dispositivo físico.



📄 Para más detalle sobre la integración con Supabase, revisa GUIA_SUPABASE.txt y README_INTEGRACION_FINAL.txt en la raíz del repositorio.



📌 Notas


El login funciona actualmente de forma local; la persistencia de usuarios y pedidos en la nube (Supabase) está integrada para registro y guardado de pedidos.
El proyecto utiliza permisos de cámara para el escaneo de códigos QR; asegúrate de aceptar los permisos solicitados al ejecutar la app.


👩‍💻 Autoría

Proyecto académico desarrollado por grupo de trabajo duoc 
