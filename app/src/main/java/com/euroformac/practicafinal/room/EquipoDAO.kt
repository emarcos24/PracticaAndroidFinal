package com.euroformac.practicafinal.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface EquipoDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarEquipo(equipo: Equipo)

    @Update
    suspend fun actualizarEquipo(equipo: Equipo)

    @Query("SELECT * FROM equipos")
    suspend fun obtenerEquipos(): List<Equipo>

    @Query("SELECT * FROM equipos WHERE id = :equipoId LIMIT 1")
    suspend fun obtenerEquipoPorId(equipoId: Int): Equipo?

    @Delete
    suspend fun eliminarEquipo(equipo: Equipo)
}
