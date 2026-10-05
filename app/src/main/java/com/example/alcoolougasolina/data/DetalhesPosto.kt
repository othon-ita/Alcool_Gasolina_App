package com.example.alcoolougasolina.data

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.alcoolougasolina.u_interface.PostoViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.setValue

@Composable
fun DetalhesPosto (
    navController: NavHostController,
    viewModel: PostoViewModel,
    context: Context,
    id: Int
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

        Column(
            modifier = Modifier.padding(horizontal = 21.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
            ) {
                posto?.nomePosto?.uppercase()?.let {
                    Text(
                        modifier = Modifier.align(Alignment.Top),
                        text = it,
                        color = MaterialTheme.colorScheme.onBackground,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth().padding(4.dp)
            ) {
                Text(
                    modifier = Modifier,
                    text = "Preço do Álcool:",
                    color = MaterialTheme.colorScheme.tertiary,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    modifier = Modifier,
                    text = "R$ " + posto?.valorAlcool,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Medium
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth().padding(4.dp)
            ) {
                Text(
                    modifier = Modifier,
                    text = "Preço da Gasolina:",
                    color = MaterialTheme.colorScheme.tertiary,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    modifier = Modifier,
                    text = "R$ " + posto?.valorGasolina,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Medium
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth().padding(4.dp)
            ) {
                Text(
                    modifier = Modifier,
                    text = "Data do Cadastro:",
                    color = MaterialTheme.colorScheme.tertiary,
                    fontWeight = FontWeight.SemiBold
                )
                posto?.dataCadastro?.let {
                    Text(
                        modifier = Modifier,
                        text = it,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth().padding(4.dp)
            ) {
                Text(
                    modifier = Modifier,
                    text = "Endereço:",
                    color = MaterialTheme.colorScheme.tertiary,
                    fontWeight = FontWeight.SemiBold
                )
                posto?.endereco?.let {
                    Text(
                        modifier = Modifier,
                        text = it,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}