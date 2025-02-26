package com.euroformac.practicafinal.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "partidos",
    foreignKeys = [
        ForeignKey(entity = Equipo::class, parentColumns = ["id"], childColumns = ["localId"], onDelete = ForeignKey.CASCADE),
        ForeignKey(entity = Equipo::class, parentColumns = ["id"], childColumns = ["visitanteId"], onDelete = ForeignKey.CASCADE),
        ForeignKey(entity = Jornada::class, parentColumns = ["id"], childColumns = ["jornadaId"], onDelete = ForeignKey.CASCADE)
    ],
    indices = [Index(value = ["localId"]), Index(value = ["visitanteId"]), Index(value = ["jornadaId"])]
)
data class Partido(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "localId") val localId: Int,
    @ColumnInfo(name = "visitanteId") val visitanteId: Int,
    @ColumnInfo(name = "fecha") val fecha: String,
    @ColumnInfo(name = "jornadaId") val jornadaId: Int,
    @ColumnInfo(name = "jugado") val jugado: Boolean = false,
    @ColumnInfo(name = "puntosLocal") val puntosLocal: Int? = null,
    @ColumnInfo(name = "puntosVisitante") val puntosVisitante: Int? = null
) {
    val resultadoPartido: String
        get() = if (jugado) {
            "$puntosLocal - $puntosVisitante"
        } else {
            fecha
        }

}
