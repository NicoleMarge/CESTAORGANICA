package com.example.huertohogardefinitiveedition

import android.Manifest
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.runtime.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import com.example.huertohogardefinitiveedition.viewmodel.QrViewModel
import com.example.huertohogardefinitiveedition.utils.CameraPermissionHelper
import com.example.huertohogardefinitiveedition.navigation.AppNav

class MainActivity : ComponentActivity() {

    // Se inicializa de forma perezosa, pero ojo con lo que hay en su init {}
    private val qrViewModel: QrViewModel by viewModels()

    // Cambiamos el estado a un tipo primitivo para manejarlo de forma segura en Compose
    private var _hasCameraPermission = false

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            // Si necesitas refrescar la UI tras conceder el permiso, lo ideal es delegarlo
            if (isGranted) {
                Toast.makeText(this, "Permiso de cámara concedido", Toast.LENGTH_SHORT).show()
                recreate() // Reacia la actividad rápidamente para refrescar el estado visual de forma segura
            } else {
                Toast.makeText(
                    this,
                    "Se necesita permiso de cámara para escanear códigos QR",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _hasCameraPermission = CameraPermissionHelper.hasCameraPermission(this)

        setContent {
            MaterialTheme {
                Surface {
                    // Convertimos la variable local en un estado de Compose seguro dentro del subárbol
                    val hasPermissionState by remember { mutableStateOf(_hasCameraPermission) }

                    AppNav(
                        hasCameraPermission = hasPermissionState,
                        onRequestPermission = {
                            requestPermissionLauncher.launch(Manifest.permission.CAMERA)
                        }
                    )
                }
            }
        }

        // Observa los resultados del QR de forma segura
        qrViewModel.qrResult.observe(this) { qrResult ->
            qrResult?.let { result ->
                Toast.makeText(this, "Código QR detectado: ${result.content}", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        _hasCameraPermission = CameraPermissionHelper.hasCameraPermission(this)
    }
}