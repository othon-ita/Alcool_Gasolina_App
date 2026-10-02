package com.example.alcoolougasolina.u_interface

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.alcoolougasolina.ui.theme.AlcoolOuGasolinaTheme
import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.getValue
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import com.example.alcoolougasolina.data.PostoRepository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.compose.ui.platform.LocalContext
import com.example.alcoolougasolina.data.AppDatabase
import androidx.lifecycle.viewmodel.compose.viewModel


class PostosActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            AlcoolOuGasolinaTheme {
                val context = LocalContext.current
                val dao = AppDatabase.getDatabase(context).postoDao()
                val repository = PostoRepository(dao)
                val viewModel: PostoViewModel = viewModel(
                    factory = PostoViewModelFactory(repository)
                )
                TelaPostos(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun TelaPostos(viewModel: PostoViewModel) {
    val postos by viewModel.postos.collectAsStateWithLifecycle()

    LazyColumn {
        items(postos, key = { it.id }) { posto ->
            Text(text = posto.valorAlcool.toString())
            Text(text = posto.valorGasolina.toString())
            Text(text = posto.nomePosto)
            Text(text = posto.dataCadastro)
            Text(text = posto.localizacao)
        }
    }
}

class PostoViewModelFactory(private val repository: PostoRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PostoViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PostoViewModel(repository) as T
        }
        throw IllegalArgumentException("ViewModel desconhecido")
    }
}