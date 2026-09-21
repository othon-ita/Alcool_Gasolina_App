package com.example.alcoolougasolina

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.alcoolougasolina.ui.theme.AlcoolOuGasolinaTheme
import androidx.compose.material3.TextField
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.OutlinedTextField
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Switch
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.ui.res.stringResource

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AlcoolOuGasolinaTheme {
                TelaUnica()
            }
        }
    }
}

@Composable
fun AlcoolOuGasolinaApp () {
    TelaUnica()
}

@Preview(showBackground = true)
@Composable
fun TelaUnica (modifier: Modifier = Modifier
    .fillMaxSize()
    .wrapContentSize(Alignment.Center)) {
    var textoAlcool by rememberSaveable { mutableStateOf("") }
    var textoGasolina by rememberSaveable { mutableStateOf("") }
    var textoPosto by rememberSaveable { mutableStateOf("") }
    var ligado:Boolean by rememberSaveable { mutableStateOf(true) }
    val porcentagem = when (ligado) {
        true -> stringResource(R.string.rendimento_75)
        else -> stringResource(R.string.rendimento_70)
    }
    var rendimento: Boolean
    var resp = false

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = textoAlcool,
            onValueChange = { novoTexto -> textoAlcool = novoTexto },
            label = { Text("Preço do Álcool (R$)") },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp)
        )
        OutlinedTextField(
            value = textoGasolina,
            onValueChange = { novoTexto -> textoGasolina = novoTexto },
            label = { Text("Preço da Gasolina (R$)") },
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
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (ligado) {
                Text (
                    text = porcentagem
                )
            }
            Switch(
                checked = ligado,
                onCheckedChange = { novoEstado -> ligado = novoEstado },
                modifier = Modifier.padding(horizontal = 4.dp)
            )
            if (!ligado) {
                Text (
                    text = porcentagem
                )
            }
        }

        Button(
            modifier = Modifier.fillMaxWidth().height(64.dp).padding(horizontal = 16.dp, vertical = 8.dp),
            onClick = {
                if (ligado) {
                    rendimento = ((textoGasolina.toFloat() * 0.75).toFloat() == (textoAlcool.toFloat()))
                } else {
                    rendimento =
                        ((textoGasolina.toFloat() * 0.7).toFloat() == (textoAlcool.toFloat()))
                }
                resp = true
            }
        ) {
            Text(stringResource(R.string.calcular))
        }

        Text(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            text = "Vamos Calcular?"
        )
    }
}

