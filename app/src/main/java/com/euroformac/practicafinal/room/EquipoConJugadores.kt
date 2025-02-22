package com.euroformac.practicafinal.room

import androidx.room.Embedded
import androidx.room.Relation

data class EquipoConJugadores(
    @Embedded val equipo: Equipo,
    @Relation(
        parentColumn = "id",
        entityColumn = "equipoId"
    )
    val jugadores: List<Jugador>
)