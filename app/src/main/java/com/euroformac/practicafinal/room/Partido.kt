package com.euroformac.practicafinal.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "partidos",
    foreignKeys = [
        ForeignKey(entity = Equipo::class, parentColumns = ["id"], childColumns = ["localId"], onDelete = ForeignKey.CASCADE),
        ForeignKey(entity = Equipo::class, parentColumns = ["id"], childColumns = ["visitanteId"], onDelete = ForeignKey.CASCADE),
        ForeignKey(entity = Jugador::class, parentColumns = ["id"], childColumns = ["mvpId"], onDelete = ForeignKey.SET_NULL)
    ]
)
data class Partido(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "localId") val localId: Int,
    @ColumnInfo(name = "visitanteId") val visitanteId: Int,
    @ColumnInfo(name = "marcadorLocal") val marcadorLocal: Int?,
    @ColumnInfo(name = "marcadorVisitante") val marcadorVisitante: Int?,
    @ColumnInfo(name = "mvpId") val mvpId: Int? // Puede ser nulo si el partido no tiene MVP
)