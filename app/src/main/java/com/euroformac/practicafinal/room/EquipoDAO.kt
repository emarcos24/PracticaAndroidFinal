package com.euroformac.practicafinal.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update

@Dao
interface EquipoDAO {

    @Update
    suspend fun actualizarEquipo(equipo: Equipo)

    @Query("SELECT * FROM equipos")
    suspend fun obtenerEquipos(): List<Equipo>

    @Query("SELECT * FROM equipos WHERE id = :equipoId LIMIT 1")
    suspend fun obtenerEquipoPorId(equipoId: Int): Equipo?

    @Transaction
    @Query("SELECT * FROM equipos WHERE id = :equipoId")
    suspend fun obtenerEquipoConJugadores(equipoId: Int): EquipoConJugadores

    @Transaction
    suspend fun insertarEquipoConJugadores(equipo: Equipo, jugadores: List<Jugador>) {
        val equipoId = insertarEquipo(equipo)
        if (equipoId > 0) {
            val jugadoresConEquipo = jugadores.map { it.copy(equipoId = equipoId.toInt()) }
            insertarJugadores(jugadoresConEquipo)
        }
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarEquipo(equipo: Equipo): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarJugadores(jugadores: List<Jugador>)

    @Query("DELETE FROM equipos WHERE id = :equipoId")
    suspend fun borrarEquipoPorId(equipoId: Int)

}
