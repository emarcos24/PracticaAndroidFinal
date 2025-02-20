package com.euroformac.practicafinal.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface JugadorDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarJugador(jugador: Jugador)

    @Update
    suspend fun actualizarJugador(jugador: Jugador)

    @Query("SELECT * FROM jugadores WHERE equipoId = :equipoId")
    suspend fun obtenerJugadoresPorEquipo(equipoId: Int): List<Jugador>

    @Delete
    suspend fun eliminarJugador(jugador: Jugador)
}