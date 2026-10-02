package com.example.alcoolougasolina.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface PostoDao {
    //CREATE
    @Insert
    suspend fun inserir(posto: Posto)

    //READ
    @Query("SELECT * FROM postos ORDER BY id DESC")
    fun listarTodos(): Flow<List<Posto>>

    //UPDATE
    @Update
    suspend fun atualizar(posto: Posto)

    //DELETE
    @Delete
    suspend fun deletar(posto: Posto)
}