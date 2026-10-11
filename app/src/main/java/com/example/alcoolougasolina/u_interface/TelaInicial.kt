package com.example.alcoolougasolina.u_interface

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.alcoolougasolina.R
import java.time.format.DateTimeFormatter
import java.time.LocalDate
import androidx.compose.material3.Icon
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.size

@Composable
fun TelaInicial(
    navController: NavHostController,
    viewModel: ConfigViewModel,
    context: Context,
    modifier: Modifier = Modifier
        .fillMaxSize()
        .wrapContentSize(Alignment.Center)
        .imePadding()
) {
    var textoAlcool by rememberSaveable { mutableStateOf("") }
    var textoGasolina by rememberSaveable { mutableStateOf("") }
    var textoPosto by rememberSaveable { mutableStateOf("") }
    var ligado:Boolean by rememberSaveable { mutableStateOf(viewModel.modo) }
    val porcentagem = when (ligado) {
        true -> stringResource(R.string.rendimento_75)
        else -> stringResource(R.string.rendimento_70)
    }
    var rende by rememberSaveable { mutableStateOf(false) }
    val proibidos = setOf(',', '-', ' ')
    var erroAlcool by rememberSaveable { mutableStateOf(false) }
    var erroGasolina by rememberSaveable { mutableStateOf(false) }

    if (textoAlcool.isNotBlank() && textoGasolina.isNotBlank()) {
        val valorAlcool = textoAlcool.toDouble()
        val valorGasolina = textoGasolina.toDouble()

        rende = if (ligado) {
            valorGasolina * 0.75 >= valorAlcool
        } else {
            valorGasolina * 0.7 >= valorAlcool
        }
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box (
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
        ) {}
        OutlinedTextField(
            value = textoAlcool,
            onValueChange = { novoTexto ->
                if (novoTexto.none {caractere -> caractere in proibidos }) {
                    textoAlcool = novoTexto
                } },
            label = { Text("Preço do Álcool (R$0.00)") },
            isError = erroAlcool && textoAlcool.isBlank(),
            supportingText = {
                if (erroAlcool && textoAlcool.isBlank()) Text("Preencha o campo!")
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
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
                    textoGasolina = novoTexto
                } },
            label = { Text("Preço da Gasolina (R$0.00)") },
            isError = erroGasolina && textoGasolina.isBlank(),
            supportingText = {
                if (erroGasolina && textoGasolina.isBlank()) Text("Preencha o campo!")
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        )
        OutlinedTextField(
            value = textoPosto,
            onValueChange = { novoTexto -> textoPosto = novoTexto },
            label = { Text("Nome do Posto (Opcional)") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        )

        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Taxa de Rendibilidade"
            )
            Row (
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (ligado) {
                    Text (
                        modifier = Modifier.padding(horizontal = 4.dp),
                        text = porcentagem
                    )
                }
                Switch(
                    checked = ligado,
                    onCheckedChange = { novo ->
                        ligado = novo
                        viewModel.atualizarSwitch()
                    },
                    modifier = Modifier.padding(horizontal = 4.dp),
                    colors = SwitchDefaults.colors(
                        checkedTrackColor = MaterialTheme.colorScheme.primary,
                        checkedThumbColor = MaterialTheme.colorScheme.secondary
                    )
                )
                if (!ligado) {
                    Text (
                        modifier = Modifier.padding(horizontal = 4.dp),
                        text = porcentagem
                    )
                }
            }
        }

        Box (
            modifier = Modifier
                .fillMaxWidth()
                .height(90.dp)
                .padding(horizontal = 40.dp),
            contentAlignment = Alignment.Center
        ) {
            if (textoAlcool.isNotBlank() && textoGasolina.isNotBlank()) {
                if (rende) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(81.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(MaterialTheme.colorScheme.secondary),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                            text = "Álcool rende mais!"
                        )
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(81.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(MaterialTheme.colorScheme.secondary),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                            text = "Gasolina rende mais!"
                        )
                    }
                }
            } else {
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
                        navController.navigate("telapostos")
                    }
                ) {
                    Text(
                        modifier = Modifier,
                        text = "Administre seus postos"
                    )
                }
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp)
                .padding(horizontal = 20.dp),
            contentAlignment = Alignment.CenterEnd
        ) {
            Button(
                modifier = Modifier
                    .width(90.dp)
                    .height(90.dp),
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                onClick = {
                    if (textoAlcool.isNotBlank() && textoGasolina.isNotBlank()) {
                        navController.navigate(
                            "telaadicionarposto/$textoAlcool/$textoGasolina/$textoPosto"
                        )
                    } else {
                        if (textoAlcool.isBlank()) {
                        erroAlcool = true
                        }
                        if (textoGasolina.isBlank()) {
                            erroGasolina = true
                        }
                    }
                }
            ) {
                Icon(
                    modifier = Modifier.size(32.dp),
                    painter = painterResource(R.drawable.mais_preto),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.background
                )
            }
        }
    }
}