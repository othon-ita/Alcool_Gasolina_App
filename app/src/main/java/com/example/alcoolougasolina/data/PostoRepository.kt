package com.example.alcoolougasolina.data

import kotlinx.coroutines.flow.Flow

class PostoRepository(private val dao: PostoDao) {
    val todosPostos: Flow<List<Posto>> = dao.listarTodos()
    suspend fun inserir(posto: Posto) = dao.inserir(posto)
    suspend fun atualizar(posto: Posto) = dao.atualizar(posto)
    suspend fun deletar(posto: Posto) = dao.deletar(posto)
}