package com.euroformac.practicafinal.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface PartidoDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarPartido(fixture: Partido)

    @Query("SELECT * FROM partidos")
    suspend fun obtenerTodosLosPartidos(): List<Partido>

    @Delete
    suspend fun eliminarPartido(fixture: Partido)
}