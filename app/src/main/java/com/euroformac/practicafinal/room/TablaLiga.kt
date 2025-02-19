package com.euroformac.practicafinal.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tabla_liga")
data class TablaLiga(
    @PrimaryKey val equipoId: Int,
    @ColumnInfo(name = "posicion") val posicion: Int,
    @ColumnInfo(name = "puntos") val puntos: Int,
    @ColumnInfo(name = "diferencia_puntos") val diferenciaPuntos: Int,
    @ColumnInfo(name = "partidos_jugados") val partidosJugados: Int,
    @ColumnInfo(name = "partidos_ganados") val partidosGanados: Int,
    @ColumnInfo(name = "partidos_perdidos") val partidosPerdidos: Int
)