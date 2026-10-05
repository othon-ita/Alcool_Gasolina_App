package com.example.alcoolougasolina.u_interface

import com.example.alcoolougasolina.data.PostoRepository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.flow.StateFlow
import com.example.alcoolougasolina.data.Posto
import kotlinx.coroutines.flow.stateIn
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.launch

class PostoViewModel(private val repository: PostoRepository) : ViewModel() {
    val postos: StateFlow<List<Posto>> = repository.todosPostos
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

    suspend fun buscaPorId(
        id: Int
    ): Posto? =
        repository.buscaPorId(
            id = id
        )

    fun adicionar(
        valorAlcool: Double,
        valorGasolina: Double,
        nomePosto: String,
        dataCadastro: String,
        endereco: String,
        latitude: Double = 0.0,
        longitude: Double = 0.0
    ) = viewModelScope.launch {
        repository.inserir(Posto(
            valorAlcool = valorAlcool,
            valorGasolina = valorGasolina,
            nomePosto = nomePosto,
            dataCadastro = dataCadastro,
            endereco = endereco,
            latitude = latitude,
            longitude = longitude
        ))
    }

    fun atualizar(
        valorAlcool: Double,
        valorGasolina: Double,
        nomePosto: String,
        dataCadastro: String,
        endereco: String,
        latitude: Double = 0.0,
        longitude: Double = 0.0
    ) = viewModelScope.launch {
        repository.atualizar(Posto(
            valorAlcool = valorAlcool,
            valorGasolina = valorGasolina,
            nomePosto = nomePosto,
            dataCadastro = dataCadastro,
            endereco = endereco,
            latitude = latitude,
            longitude = longitude
        ))
    }

    fun deletar(
        posto: Posto
    ) = viewModelScope.launch {
        repository.deletar(posto)
    }
}

class PostoViewModelFactory(private val repository: PostoRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PostoViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PostoViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
