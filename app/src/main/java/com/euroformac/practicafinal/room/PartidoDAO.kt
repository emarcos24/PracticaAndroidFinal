package com.euroformac.practicafinal.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface PartidoDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarPartido(partido: Partido)

    @Update
    suspend fun actualizarPartido(partido: Partido)

    @Delete
    suspend fun eliminarPartido(partido: Partido)

    @Query("SELECT * FROM partidos")
    suspend fun obtenerTodosLosPartidos(): List<Partido>

    @Query("DELETE FROM partidos WHERE id = :partidoId")
    suspend fun borrarPartidoPorId(partidoId: Int)

    @Query("SELECT * FROM partidos")
    suspend fun obtenerPartidosConEquipos(): List<PartidoConEquipos>

    @Query("SELECT * FROM partidos ORDER BY fecha ASC")
    fun obtenerPartidos(): LiveData<List<Partido>>

    @Query("SELECT id FROM partidos WHERE localId = :localId AND visitanteId = :visitanteId AND fecha = :fecha LIMIT 1")
    suspend fun obtenerIdPartido(localId: Int, visitanteId: Int, fecha: String): Int?

    @Query("SELECT * FROM partidos WHERE id = :partidoId")
    suspend fun obtenerPartidoPorId(partidoId: Int): Partido?

    @Query("SELECT * FROM partidos WHERE jornadaId = :jornada")
    suspend fun obtenerPartidosPorJornada(jornada: Int): List<PartidoConEquipos>

    @Query("SELECT EXISTS (SELECT 1 FROM jornadas WHERE id = :jornada)")
    suspend fun existeJornada(jornada: Int): Boolean

}