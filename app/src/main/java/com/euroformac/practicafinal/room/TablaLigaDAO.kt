package com.euroformac.practicafinal.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface TablaLigaDAO {
    @Query("SELECT * FROM tabla_liga ORDER BY posicion ASC")
    suspend fun obtenerTablaLiga(): List<TablaLiga>
}