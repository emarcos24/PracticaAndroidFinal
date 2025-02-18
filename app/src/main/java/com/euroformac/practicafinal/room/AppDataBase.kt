package com.euroformac.practicafinal.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [Equipo::class, Jugador::class, Partido::class, TablaLiga::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {

    abstract val equipoDao: EquipoDAO
    abstract val jugadorDao: JugadorDAO
    abstract val partidoDao: PartidoDAO
    abstract val tablaLigaDao: TablaLigaDAO

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "liga_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}