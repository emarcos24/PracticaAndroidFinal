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


    @Delete
    suspend fun eliminarEquipo(equipo: Equipo)

    @Transaction
    suspend fun insertarEquipoConJugadores(equipo: Equipo, jugadores: List<Jugador>) {
        val equipoId = insertarEquipo(equipo)  // Guarda el ID generado correctamente
        if (equipoId > 0) {  // Solo inserta jugadores si el equipo se crea con éxito
            val jugadoresConEquipo = jugadores.map { it.copy(equipoId = equipoId.toInt()) }
            insertarJugadores(jugadoresConEquipo)  // Ahora sí usa la función correcta
        }
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarEquipo(equipo: Equipo): Long  // Cambia el tipo de retorno a Long para obtener el ID

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarJugadores(jugadores: List<Jugador>)  // Acepta una lista



}
