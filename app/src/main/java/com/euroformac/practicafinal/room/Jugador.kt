package com.euroformac.practicafinal.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "jugadores",
    foreignKeys = [ForeignKey(
        entity = Equipo::class,
        parentColumns = ["id"],
        childColumns = ["equipoId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class Jugador(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "equipoId") val equipoId: Int,
    @ColumnInfo(name = "nombre") val nombre: String,
    @ColumnInfo(name = "posicion") val posicion: String,
    @ColumnInfo(name = "numero") val numero: Int
)