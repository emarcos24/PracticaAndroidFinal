package com.euroformac.practicafinal.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface JornadaDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarJornada(jornada: Jornada)

    @Query("SELECT * FROM jornadas ORDER BY numero_jornada ASC")
    suspend fun obtenerJornadas(): List<Jornada>

    @Delete
    suspend fun eliminarJornada(jornada: Jornada)

    @Query("SELECT id FROM jornadas WHERE numero_jornada = :numero")
    suspend fun obtenerIdPorNumero(numero: Int): Int?

    @Query("SELECT * FROM jornadas WHERE id = :jornadaId LIMIT 1")
    suspend fun getJornadaById(jornadaId: Int): Jornada
}
