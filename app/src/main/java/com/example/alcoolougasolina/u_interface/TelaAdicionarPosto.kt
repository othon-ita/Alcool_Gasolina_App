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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import android.net.Uri

@Composable
fun TelaAdicionarPosto(
    navController: NavHostController,
    viewModel: PostoViewModel,
    context: Context,
//    locationString: String?,
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
//        val local = locationString.substringAfter("?q=")

        val proibidos = setOf(',', '-', ' ')
        var erroAlcool by rememberSaveable { mutableStateOf(false) }
        var erroGasolina by rememberSaveable { mutableStateOf(false) }

        var novoTextoAlcool = textoAlcool
        var novoTextoGasolina = textoGasolina
        var novoTextoPosto = textoPosto

        if (textoPosto.isBlank()) {
            novoTextoPosto = "Posto Sem Nome"
        }

        Column(modifier = Modifier,
            horizontalAlignment = Alignment.CenterHorizontally) {
            OutlinedTextField(
                value = textoAlcool,
                onValueChange = { novoTexto ->
                    if (novoTexto.none {caractere -> caractere in proibidos }) {
                        novoTextoAlcool = novoTexto
                    } },
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
                value = textoGasolina,
                onValueChange = { novoTexto ->
                    if (novoTexto.none {caractere -> caractere in proibidos }) {
                        novoTextoGasolina = novoTexto
                    } },
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
                value = textoPosto,
                onValueChange = { novoTexto -> novoTextoPosto = novoTexto },
                label = { Text("Nome do Posto (Opcional)") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp)
            )
            Box(
                modifier = Modifier.fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
                    .height(56.dp),
                contentAlignment = Alignment.BottomCenter
            ) {
                Text(
                    text = "Escolha a localização do posto e compatilhe com o App:"
                )
            }
            //if (locationString.isNullOrEmpty()) {
                Box(
                    modifier = Modifier.fillMaxWidth()
                        .height(64.dp)
                        .padding(horizontal = 25.dp),
                    contentAlignment = Alignment.TopStart
                ) {
                    Button(
                        modifier = Modifier.width(120.dp)
                            .height(40.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.secondary,
                            contentColor = MaterialTheme.colorScheme.onSurface
                        ),
                        onClick = {
                            val intent = Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse("geo:0,0")
                            )
                            context.startActivity(intent)
                        }
                    ) {
                        Text(
                            text = "Abrir Mapa"
                        )
                    }
                }
            /*} else {
                val local = locationString.substringAfter("?q=")

                Text(
                    modifier = Modifier.height(8.dp),
                    text = local
                )
            }*/
        }
        /*viewModel.adicionar(
            textoAlcool.toDouble(),
            textoGasolina.toDouble(),
            novoTextoPosto,
            dataFormatada,
            "00"
        )*/
    }
}