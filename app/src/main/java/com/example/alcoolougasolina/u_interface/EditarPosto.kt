package com.example.alcoolougasolina.u_interface

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.alcoolougasolina.data.Posto
import androidx.compose.material3.ExperimentalMaterial3Api
import java.time.format.DateTimeFormatter
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.TextButton
import java.time.Instant
import java.time.ZoneId
import androidx.compose.material3.DatePicker
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.Box
import java.time.ZoneOffset


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditarPosto(
    navController: NavHostController,
    viewModel: PostoViewModel,
    context: Context,
    id: Int,
    modifier: Modifier = Modifier
        .fillMaxSize()
        .wrapContentSize(Alignment.Center)
        .imePadding()
) {
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.Center),
        color = MaterialTheme.colorScheme.background
    ) {
        var posto by remember { mutableStateOf<Posto?>(null) }

        LaunchedEffect(id) {
            posto = viewModel.buscaPorId(id)
        }

        if (posto != null) {
            var textoAlcool by rememberSaveable { mutableStateOf(posto!!.valorAlcool.toString()) }
            var textoGasolina by rememberSaveable { mutableStateOf(posto!!.valorGasolina.toString()) }
            var textoPosto by rememberSaveable { mutableStateOf(posto!!.nomePosto) }
            var dataCadastro by rememberSaveable { mutableStateOf(posto!!.dataCadastro) }
            var endereco by rememberSaveable { mutableStateOf(posto!!.endereco) }

            val proibidos = setOf(',', '-', ' ')
            var erroAlcool by rememberSaveable { mutableStateOf(false) }
            var erroGasolina by rememberSaveable { mutableStateOf(false) }
            var mostrarDialogo by rememberSaveable { mutableStateOf(false) }
            val formato = remember { DateTimeFormatter.ofPattern("dd/MM/yyyy") }

            Column(
                modifier = modifier,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                OutlinedTextField(
                    value = textoAlcool,
                    onValueChange = { novoTexto ->
                        if (novoTexto.none { caractere -> caractere in proibidos }) {
                            textoAlcool = novoTexto
                        }
                    },
                    label = { Text("Preço do Álcool (R$0.00)") },
                    isError = erroAlcool && textoAlcool.isBlank(),
                    supportingText = {
                        if (erroAlcool && textoAlcool.isBlank()) Text("Preencha o campo!")
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
                    value = textoGasolina,
                    onValueChange = { novoTexto ->
                        if (novoTexto.none { caractere -> caractere in proibidos }) {
                            textoGasolina = novoTexto
                        }
                    },
                    label = { Text("Preço da Gasolina (R$0.00)") },
                    isError = erroGasolina && textoGasolina.isBlank(),
                    supportingText = {
                        if (erroGasolina && textoGasolina.isBlank()) Text("Preencha o campo!")
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp)
                )
                OutlinedTextField(
                    value = textoPosto,
                    onValueChange = { novoTexto -> textoPosto = novoTexto },
                    label = { Text("Nome do Posto (Opcional)") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp)
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 14.dp)
                ) {
                    OutlinedTextField(
                        value = dataCadastro,
                        onValueChange = { },
                        label = { Text("Data") },
                        readOnly = true,
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.Default.DateRange,
                                contentDescription = "Escolher data"
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) { mostrarDialogo = true }
                    )
                }
                if (mostrarDialogo) {
                    val estadoData = rememberDatePickerState()

                    DatePickerDialog(
                        onDismissRequest = { mostrarDialogo = false },
                        confirmButton = {
                            TextButton(onClick = {
                                estadoData.selectedDateMillis?.let { millis ->
                                    val data = Instant.ofEpochMilli(millis)
                                        .atZone(ZoneOffset.UTC)
                                        .toLocalDate()
                                    dataCadastro = data.format(formato)
                                }
                                mostrarDialogo = false
                            }) {
                                Text("OK")
                            }
                        },
                        dismissButton = {
                            TextButton(onClick = { mostrarDialogo = false }) {
                                Text("Cancelar")
                            }
                        }
                    ) {
                        DatePicker(state = estadoData)
                    }
                }
                OutlinedTextField(
                    value = endereco,
                    onValueChange = { novoTexto -> endereco = novoTexto },
                    label = { Text("Endereço") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp)
                )
            }
        }
    }
}