package com.example.alcoolougasolina.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "postos")
data class Posto(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val valorAlcool: Double,
    val valorGasolina: Double,
    val nomePosto: String,
    val dataCadastro: String,
    val endereco: String,
    val localizacao: String
)