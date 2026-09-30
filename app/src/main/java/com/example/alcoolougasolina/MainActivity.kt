package com.example.alcoolougasolina

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.alcoolougasolina.ui.theme.AlcoolOuGasolinaTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.OutlinedTextField
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Switch
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.ui.res.stringResource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwitchDefaults

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AlcoolOuGasolinaApp()
        }
    }
}

@Composable
fun AlcoolOuGasolinaApp() {
    AlcoolOuGasolinaTheme {
        Surface(
            color = MaterialTheme.colorScheme.background
        ) { TelaUnica() }
    }
}

@Preview(showBackground = true)
@Composable
fun TelaUnicaPreview() {
    AlcoolOuGasolinaTheme {
        Surface(
            color = MaterialTheme.colorScheme.background
        ) {
            TelaUnica()
        }
    }
}

@Composable
fun TelaUnica(
    modifier: Modifier = Modifier
        .fillMaxSize()
        .wrapContentSize(Alignment.Center)
) {
    var textoAlcool by rememberSaveable { mutableStateOf("") }
    var textoGasolina by rememberSaveable { mutableStateOf("") }
    var textoPosto by rememberSaveable { mutableStateOf("") }
    var ligado:Boolean by rememberSaveable { mutableStateOf(true) }
    val porcentagem = when (ligado) {
        true -> stringResource(R.string.rendimento_75)
        else -> stringResource(R.string.rendimento_70)
    }
    var rende by rememberSaveable { mutableStateOf(false) }
    var calculado by rememberSaveable { mutableStateOf(false) }

    var tentouEnviar by rememberSaveable() { mutableStateOf(false) }
    val campoAlcoolInvalido = textoAlcool.isBlank()
    val campoGasolinaInvalido = textoGasolina.isBlank()
    val erroCampoAlcoolVazio = tentouEnviar && campoAlcoolInvalido
    val erroCampoGasolinaVazio = tentouEnviar && campoGasolinaInvalido

    val proibidos = setOf(',', '-', ' ')

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box (
            modifier = Modifier.fillMaxWidth().height(120.dp)
        ) {}
        OutlinedTextField(
            value = textoAlcool,
            onValueChange = { novoTexto ->
                if (novoTexto.none {caractere -> caractere in proibidos }) {
                    textoAlcool = novoTexto
                } },
            label = { Text("Preço do Álcool (R$0.00)") },
            isError = erroCampoAlcoolVazio,
            supportingText = {
                if (erroCampoAlcoolVazio) {
                    Text("Preencha o valor do Álcool")
                }
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp)
        )
        OutlinedTextField(
            value = textoGasolina,
            onValueChange = { novoTexto ->
                if (novoTexto.none {caractere -> caractere in proibidos }) {
                    textoGasolina = novoTexto
                } },
            label = { Text("Preço da Gasolina (R$0.00)") },
            isError = erroCampoGasolinaVazio,
            supportingText = {
                if (erroCampoGasolinaVazio) {
                    Text("Preencha o valor da Gasolina")
                }
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp)
        )
        OutlinedTextField(
            value = textoPosto,
            onValueChange = { novoTexto -> textoPosto = novoTexto },
            label = { Text("Nome do Posto (Opcional)") },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp)
        )

        Row (
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
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
                    onCheckedChange = { novoEstado -> ligado = novoEstado },
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

        Button(
            modifier = Modifier.fillMaxWidth().height(64.dp).padding(horizontal = 16.dp, vertical = 8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            ),
            onClick = {
                val valorAlcool = textoAlcool.toDoubleOrNull()
                val valorGasolina = textoGasolina.toDoubleOrNull()
                tentouEnviar = true

                if (valorGasolina != null && valorAlcool != null) {
                    rende = if (ligado) {
                        valorGasolina * 0.75 >= valorAlcool
                    } else {
                        valorGasolina * 0.7 >= valorAlcool
                    }
                    calculado = true
                }
            }
        ) {
            Text(stringResource(R.string.calcular))
        }

        Text(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            text = "Vamos Calcular?"
        )

        Box (
            modifier = Modifier.fillMaxWidth().height(120.dp).padding(horizontal = 40.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            if (calculado) {
                if (rende) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(81.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(MaterialTheme.colorScheme.primary)
                            .border(2.dp,MaterialTheme.colorScheme.secondary, RoundedCornerShape(16.dp)),
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
                            .background(colorResource(R.color.washed_orange))
                            .border(2.dp, colorResource(R.color.orange), RoundedCornerShape(16.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                            text = "Gasolina rende mais!"
                        )
                    }
                }
            }
        }
    }
}

