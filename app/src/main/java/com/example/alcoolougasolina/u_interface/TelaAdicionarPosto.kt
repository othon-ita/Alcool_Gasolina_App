package com.example.alcoolougasolina.u_interface

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.location.LocationManagerCompat
import androidx.navigation.NavHostController
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.format.DateTimeFormatter
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

        var temPermissaoLocalizacao by rememberSaveable {
            mutableStateOf(
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            )
        }

        var latitude by rememberSaveable { mutableStateOf<Double?>(null) }
        var longitude by rememberSaveable { mutableStateOf<Double?>(null) }
        var endereco by rememberSaveable { mutableStateOf<String?>(null) }
        var textoEndereco by rememberSaveable { mutableStateOf("") }
        var recarregarLocalizacao by remember { mutableIntStateOf(0) }

        LaunchedEffect(temPermissaoLocalizacao, recarregarLocalizacao) {
            if (temPermissaoLocalizacao) {
                try {
                    val loc = getCurrentLocation(context)
                    if (loc != null) {
                        latitude = loc.latitude
                        longitude = loc.longitude
                        val end = obterEndereco(context, loc.latitude, loc.longitude)
                        if (!end.isNullOrEmpty()) {
                            endereco = end
                            textoEndereco = end
                        }
                    }
                } catch (_: Exception) {
                }
            }
        }

        val launcher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestMultiplePermissions()
        ) { permissoes ->
            temPermissaoLocalizacao = permissoes[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                    permissoes[Manifest.permission.ACCESS_COARSE_LOCATION] == true
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
            OutlinedTextField(
                value = dataFormatada,
                onValueChange = { },
                label = { Text("Data") },
                readOnly = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 14.dp)
            )

            if (temPermissaoLocalizacao) {
                if (!isGpsAtivado(context)) {
                    LaunchedEffect(Unit) {
                        mostrarDialogGps = true
                    }
                }

                if (mostrarDialogGps) {
                    DialogAtivarGps(
                        onDismiss = {
                            mostrarDialogGps = false
                        },
                        onGpsAtivado = {
                            mostrarDialogGps = false
                            recarregarLocalizacao++
                        }
                    )
                }

                OutlinedTextField(
                    value = textoEndereco,
                    onValueChange = { },
                    readOnly = true,
                    label = { Text("Endereço") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                )

                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(32.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary,
                        contentColor = MaterialTheme.colorScheme.onSurface
                    ),
                    onClick = {
                        val valAlcool = novoTextoAlcool.toDoubleOrNull()
                        val valGasolina = novoTextoGasolina.toDoubleOrNull()

                        erroAlcool = (valAlcool == null)
                        erroGasolina = (valGasolina == null)

                        if (valAlcool != null && valGasolina != null) {
                            viewModel.adicionar(
                                valorAlcool = valAlcool,
                                valorGasolina = valGasolina,
                                nomePosto = novoTextoPosto,
                                dataCadastro = dataFormatada,
                                endereco = textoEndereco,
                                latitude = latitude ?: 0.0,
                                longitude = longitude ?: 0.0
                            )
                            navController.navigate("telapostos")
                        }
                    }
                ) {
                    Text(
                        text = "Confirmar e adicionar"
                    )
                }
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .height(28.dp),
                    contentAlignment = Alignment.BottomStart
                ) {
                    Text("Permissão de localização necessária:")
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Button(
                        modifier = Modifier
                            .width(180.dp)
                            .height(40.dp)
                            .align(Alignment.CenterStart),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.secondary,
                            contentColor = MaterialTheme.colorScheme.onSurface
                        ),
                        onClick = {
                            launcher.launch(
                                arrayOf(
                                    Manifest.permission.ACCESS_FINE_LOCATION,
                                    Manifest.permission.ACCESS_COARSE_LOCATION
                                )
                            )
                        }
                    ) {
                        Text("Solicitar Permissão")
                    }
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
        return null
    }

    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    val cancellationTokenSource = CancellationTokenSource()

    return try {
        val priority = if (hasFineLocation) Priority.PRIORITY_HIGH_ACCURACY else Priority.PRIORITY_BALANCED_POWER_ACCURACY
        fusedLocationClient.getCurrentLocation(
            priority,
            cancellationTokenSource.token
        ).await()
    } catch (_: Exception) {
        null
    } finally {
        cancellationTokenSource.cancel()
    }
}

fun isGpsAtivado(context: Context): Boolean {
    val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as? LocationManager
    return locationManager?.let { LocationManagerCompat.isLocationEnabled(it) } ?: false
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
            val enderecos = geocoder.getFromLocation(latitude, longitude, 1)
            enderecos?.firstOrNull()?.let { endereco ->
                buildString {
                    endereco.thoroughfare?.let { append(it) }
                    if (!endereco.subThoroughfare.isNullOrEmpty()) append(", ${endereco.subThoroughfare}")
                    if (!endereco.subLocality.isNullOrEmpty()) append(" - ${endereco.subLocality}")
                    if (!endereco.locality.isNullOrEmpty()) append(" - ${endereco.locality}")
                    if (!endereco.adminArea.isNullOrEmpty()) append(" - ${endereco.adminArea}")
                }.ifBlank { null }
            }
        } catch (_: Exception) {
            null
        }
    }
