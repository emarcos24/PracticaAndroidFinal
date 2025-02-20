package com.euroformac.practicafinal.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "equipos")
data class Equipo(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "nombre") val nombre: String,
    @ColumnInfo(name = "logo") val logo: String,
    @ColumnInfo(name = "manager") val manager: String
)