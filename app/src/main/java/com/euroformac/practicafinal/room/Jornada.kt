package com.euroformac.practicafinal.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "jornadas")
data class Jornada(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "numero_jornada") val numeroJornada: Int
)
