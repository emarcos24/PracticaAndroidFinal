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

    @Query("SELECT * FROM equipos")
    suspend fun obtenerEquipos(): List<Equipo>

    @Delete
    suspend fun eliminarEquipo(equipo: Equipo)
}