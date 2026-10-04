package com.example.alcoolougasolina.u_interface

import android.content.Context
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.alcoolougasolina.data.AppDatabase
import com.example.alcoolougasolina.data.PostoRepository

@Composable
fun TelaPostos(
    navController: NavHostController,
    viewModel: PostoViewModel,
    context: Context
) {
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.Center),
        color = MaterialTheme.colorScheme.background
    ) {
        val postos by viewModel.postos.collectAsStateWithLifecycle()

        LazyColumn(
            modifier = Modifier
        ) {
            items(postos, key = { it.id }) { posto ->
                Text(text = posto.valorAlcool.toString())
                Text(text = posto.valorGasolina.toString())
                Text(text = posto.nomePosto)
                Text(text = posto.dataCadastro)
                Text(text = posto.localizacao)
            }
        }
        if (postos.isEmpty()) {
            Text(
                modifier = Modifier,
                text = "Adicione algum posto!"
            )
        }
    }
}
