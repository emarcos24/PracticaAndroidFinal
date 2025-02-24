package com.euroformac.practicafinal.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [Equipo::class, Jugador::class, Partido::class, TablaLiga::class, Jornada::class],
    version = 4
)
abstract class AppDatabase : RoomDatabase() {

    abstract val equipoDao: EquipoDAO
    abstract val jugadorDao: JugadorDAO
    abstract val partidoDao: PartidoDAO
    abstract val tablaLigaDao: TablaLigaDAO
    abstract val jornadaDao: JornadaDAO

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "liga_database"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }

    }


}