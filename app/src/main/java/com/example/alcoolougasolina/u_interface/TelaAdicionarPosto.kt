package com.example.alcoolougasolina.u_interface

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import android.net.Uri
import androidx.compose.runtime.remember
import androidx.core.content.ContextCompat
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import android.Manifest
import android.location.Location
import com.google.android.gms.location.Priority
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.tasks.await
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import androidx.compose.foundation.layout.Spacer
import android.location.LocationManager
import androidx.core.location.LocationManagerCompat
import androidx.compose.ui.platform.LocalContext
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import android.provider.Settings
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.withContext
import kotlinx.coroutines.Dispatchers
import android.location.Geocoder
import java.util.Locale

@Composable
fun TelaAdicionarPosto(
    navController: NavHostController,
    viewModel: PostoViewModel,
    context: Context,
    textoAlcool: String,
    textoGasolina: String,
    textoPosto: String
) {
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.Center),
        color = MaterialTheme.colorScheme.background
    ) {
        val formato = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        val dataFormatada: String = LocalDate.now().format(formato)

        val proibidos = setOf(',', '-', ' ')
        var erroAlcool by rememberSaveable { mutableStateOf(false) }
        var erroGasolina by rememberSaveable { mutableStateOf(false) }

        var novoTextoAlcool by rememberSaveable { mutableStateOf(textoAlcool) }
        var novoTextoGasolina by rememberSaveable { mutableStateOf(textoGasolina) }
        var novoTextoPosto by rememberSaveable { mutableStateOf(textoPosto) }

        var temPermissaoLocalizacao by remember {
            mutableStateOf(
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            )
        }
        val scope = rememberCoroutineScope()
        var localizacao by remember { mutableStateOf<Location?>(null) }
        var latitude by rememberSaveable { mutableStateOf<Double?>(null) }
        var longitude by rememberSaveable { mutableStateOf<Double?>(null) }
        var endereco by rememberSaveable { mutableStateOf<String?>(null) }

        val launcher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestMultiplePermissions()
        ) { permissoes ->
            temPermissaoLocalizacao =
                permissoes[Manifest.permission.ACCESS_FINE_LOCATION] == true
        }

        var mostrarDialogGps by remember { mutableStateOf(false) }

        Column(
            modifier = Modifier,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = novoTextoAlcool,
                onValueChange = { novoTexto ->
                    if (novoTexto.none { caractere -> caractere in proibidos }) {
                        novoTextoAlcool = novoTexto
                    }
                },
                label = { Text("Preço do Álcool (R$0.00)") },
                isError = erroAlcool && novoTextoAlcool.isBlank(),
                supportingText = {
                    if (erroAlcool && novoTextoAlcool.isBlank()) Text("Preencha o campo!")
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = MaterialTheme.colorScheme.onSurface,
                    unfocusedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                    errorBorderColor = MaterialTheme.colorScheme.error,
                    cursorColor = MaterialTheme.colorScheme.primary
                )
            )
            OutlinedTextField(
                value = novoTextoGasolina,
                onValueChange = { novoTexto ->
                    if (novoTexto.none { caractere -> caractere in proibidos }) {
                        novoTextoGasolina = novoTexto
                    }
                },
                label = { Text("Preço da Gasolina (R$0.00)") },
                isError = erroGasolina && novoTextoGasolina.isBlank(),
                supportingText = {
                    if (erroGasolina && novoTextoGasolina.isBlank()) Text("Preencha o campo!")
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp)
            )
            OutlinedTextField(
                value = novoTextoPosto,
                onValueChange = { novoTexto -> novoTextoPosto = novoTexto },
                label = { Text("Nome do Posto (Opcional)") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp)
            )

            if (temPermissaoLocalizacao) {
                if (!isGpsAtivado(context)) {
                    LaunchedEffect(Unit) {
                        mostrarDialogGps = true
                    }
                }

                if (mostrarDialogGps) {
                    DialogAtivarGps(
                        onDismiss = { navController.navigate(
                            "telaadicionarposto/$textoAlcool/$textoGasolina/$textoPosto"
                        ) },
                        onGpsAtivado = {
                            mostrarDialogGps = false
                        }
                    )
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                        .height(56.dp),
                    ) {
                    Text("Permissão de localização concedida!")
                    }

                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(40.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.secondary,
                            contentColor = MaterialTheme.colorScheme.onSurface
                        ),
                        onClick = {
                            scope.launch {
                                try {
                                    localizacao = getCurrentLocation(context)
                                    if (localizacao != null) {
                                        latitude = localizacao!!.latitude
                                        longitude = localizacao!!.longitude
                                        endereco = obterEndereco(context, latitude!!, longitude!!)
                                    }
                                } catch (e: Exception) {
                                    // Tratar erro (ex: mostrar um Toast)
                                }
                            }
                    }) {
                        Text("Pegar Localização Atual")
                    }

                endereco?.let {
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = "📍 $it",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp)
                            .height(56.dp),
                    ) {
                        Text("Permissão de localização necessária.")
                    }
                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(40.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.secondary,
                            contentColor = MaterialTheme.colorScheme.onSurface
                        ),
                        onClick = {
                            launcher.launch(
                                arrayOf(
                                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                                )
                            )
                        }) {
                        Text("Solicitar Permissão")
                    }
                }
            }
    }
}

@Suppress("MissingPermission")
suspend fun getCurrentLocation(context: Context): Location? {
    val hasFineLocation = ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED

    val hasCoarseLocation = ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.ACCESS_COARSE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED

    if (!hasFineLocation && !hasCoarseLocation) {
        throw SecurityException("Permissão de localização não concedida")
    }

    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    val cancellationTokenSource = CancellationTokenSource()

    return try {
        fusedLocationClient.getCurrentLocation(
            Priority.PRIORITY_HIGH_ACCURACY,
            cancellationTokenSource.token
        ).await()
    } finally {
        cancellationTokenSource.cancel()
    }
}

fun isGpsAtivado(context: Context): Boolean {
    val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    return LocationManagerCompat.isLocationEnabled(locationManager)
}

@Composable
fun DialogAtivarGps(
    onDismiss: () -> Unit,
    onGpsAtivado: () -> Unit
) {
    val context = LocalContext.current
    val launcherConfig = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) {
        if (isGpsAtivado(context)) {
            onGpsAtivado()
        } else {
            onDismiss()
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Ativar Localização") },
        text = { Text("Para continuar, você precisa ativar o GPS nas configurações do seu dispositivo.") },
        confirmButton = {
            TextButton(onClick = {
                // Abre a tela de configurações de localização
                launcherConfig.launch(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
            }) {
                Text("Abrir Configurações")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}

suspend fun obterEndereco(context: Context, latitude: Double, longitude: Double): String? =
    withContext(Dispatchers.IO) {
        try {
            val geocoder = Geocoder(context, Locale.getDefault())
            @Suppress("DEPRECATION")
            val enderecos = geocoder.getFromLocation(latitude, longitude, 5)
            enderecos?.firstOrNull()?.let { endereco ->
                buildString {
                    endereco.thoroughfare?.let { append(it) }
                    endereco.subThoroughfare?.let { append(", $it") }
                    endereco.subLocality?.let { append(" - $it") }
                    endereco.locality?.let { append(" - $it") }
                    endereco.adminArea?.let { append(" - $it") }
                }
            }
        } catch (e: Exception) {
            null
        }
    }