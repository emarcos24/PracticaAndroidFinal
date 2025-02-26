package com.euroformac.practicafinal.room

import androidx.room.Embedded
import androidx.room.Relation

data class PartidoConEquipos(
    @Embedded val partido: Partido,

    @Relation(
        parentColumn = "localId",
        entityColumn = "id"
    )
    val equipoLocal: Equipo,

    @Relation(
        parentColumn = "visitanteId",
        entityColumn = "id"
    )
    val equipoVisitante: Equipo
)
