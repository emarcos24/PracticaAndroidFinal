package com.euroformac.practicafinal

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.euroformac.practicafinal.room.*
import kotlinx.coroutines.launch
import java.util.Locale

class FragmentoConfig : Fragment() {

    private lateinit var roomDB: AppDatabase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_config, container, false)

        roomDB = Room.databaseBuilder(
            requireContext(),
            AppDatabase::class.java, "liga_database"
        ).build()

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val botonAnadirEquipo = view.findViewById<Button>(R.id.button)
        val botonBorrarEquipo = view.findViewById<Button>(R.id.button3)
        val botonAnadirPartido = view.findViewById<Button>(R.id.button4)
        val botonBorrarPartido = view.findViewById<Button>(R.id.button5)
        val botonIdioma = view.findViewById<Button>(R.id.button7)

        botonAnadirEquipo.setOnClickListener {
            val dialogo = DialogoEquipo(object : DialogoEquipo.OnEquipoListener {
                override fun onEquipoIntroducido(nombre: String, manager: String, logo: String) {
                    abrirDialogoJugadores(nombre, manager, logo)
                }
            })
            dialogo.show(parentFragmentManager, "DialogoEquipo")
        }

        botonBorrarEquipo.setOnClickListener {
            val dialogoBorrar =
                DialogoBorrarEquipo(object : DialogoBorrarEquipo.OnEquipoBorradoListener {
                    override fun onEquipoBorrado() {
                    }
                })
            dialogoBorrar.show(parentFragmentManager, "DialogoBorrarEquipo")
        }

        botonAnadirPartido.setOnClickListener {
            lifecycleScope.launch {
                val equipos = obtenerEquiposDesdeBD()
                val dialogo = DialogoPartido(equipos, object : DialogoPartido.OnPartidoListener {
                    override fun onPartidoCreado(
                        equipoLocal: String,
                        equipoVisitante: String,
                        fecha: String,
                        jornadaId: Int,
                        jugado: Boolean,
                        puntosLocal: Int?,
                        puntosVisitante: Int?
                    ) {
                        guardarPartidoEnRoom(equipoLocal, equipoVisitante, fecha, jornadaId, jugado, puntosLocal, puntosVisitante)
                    }
                })
                dialogo.show(parentFragmentManager, "DialogoPartido")
            }
        }

        botonBorrarPartido.setOnClickListener {
            val dialogoBorrarPartido =
                DialogoBorrarPartido(object : DialogoBorrarPartido.OnPartidoBorradoListener {
                    override fun onPartidoBorrado() {
                    }
                })
            dialogoBorrarPartido.show(parentFragmentManager, "DialogoBorrarPartido")
        }

        // Configurar botón de idioma con alternancia
        botonIdioma.setOnClickListener {
            alternarIdioma()
        }
    }

    private fun abrirDialogoJugadores(nombre: String, manager: String, logo: String) {
        val dialogo =
            DialogoJugadores(nombre, manager, logo, object : DialogoJugadores.OnJugadoresListener {
                override fun onJugadoresCompletados(
                    nombreEquipo: String,
                    manager: String,
                    logo: String,
                    jugadores: List<Jugador>
                ) {
                    guardarEquipoEnRoom(nombreEquipo, manager, logo, jugadores)
                }
            })
        dialogo.show(parentFragmentManager, "DialogoJugadores")
    }

    private fun guardarEquipoEnRoom(
        nombre: String,
        manager: String,
        logo: String,
        jugadores: List<Jugador>
    ) {
        lifecycleScope.launch {
            val equipo = Equipo(nombre = nombre, manager = manager, logo = logo)
            val equipoId = roomDB.equipoDao.insertarEquipo(equipo).toInt()

            val jugadoresConEquipo = jugadores.map { it.copy(equipoId = equipoId) }
            roomDB.equipoDao.insertarJugadores(jugadoresConEquipo)

            Toast.makeText(requireContext(), "Equipo guardado con éxito", Toast.LENGTH_SHORT).show()
        }
    }

    private fun guardarPartidoEnRoom(
        equipoLocal: String,
        equipoVisitante: String,
        fecha: String,
        jornadaId: Int,
        jugado: Boolean,
        puntosLocal: Int?,
        puntosVisitante: Int?
    ) {
        lifecycleScope.launch {
            val equipoDao = roomDB.equipoDao
            val jornadaDao = roomDB.jornadaDao

            val local = equipoDao.obtenerEquipoPorNombre(equipoLocal)
            val visitante = equipoDao.obtenerEquipoPorNombre(equipoVisitante)

            // Verificar si la jornada ya existe
            var jornada = jornadaDao.getJornadaById(jornadaId)
            if (jornada == null) {
                jornada = Jornada(id = jornadaId, numeroJornada = jornadaId)
                jornadaDao.insertarJornada(jornada)
                Log.d("RoomDebug", "Jornada creada con ID: $jornadaId")
            }

            if (local == null || visitante == null) {
                Log.e(
                    "RoomError",
                    "Error: Clave foránea no encontrada (localId=${local?.id}, visitanteId=${visitante?.id}, jornadaId=${jornada.id})"
                )
                return@launch
            }

            val partido = Partido(
                localId = local.id,
                visitanteId = visitante.id,
                fecha = fecha,
                jornadaId = jornada.id,
                jugado = jugado,
                puntosLocal = puntosLocal ?: 0,
                puntosVisitante = puntosVisitante ?: 0
            )

            try {
                roomDB.partidoDao.insertarPartido(partido)
                Toast.makeText(requireContext(), "Partido guardado con éxito", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Log.e("RoomError", "Error al guardar el partido", e)
            }
        }
    }

    private suspend fun obtenerEquiposDesdeBD(): List<String> {
        val database = roomDB
        val equipoDao = database.equipoDao
        return equipoDao.obtenerEquipos().map { it.nombre }
    }

    // Función para alternar el idioma entre español e inglés
    private fun alternarIdioma() {
        val sharedPreferences = requireActivity().getSharedPreferences("config", Context.MODE_PRIVATE)
        val idiomaActual = sharedPreferences.getString("idioma", "es") // Español por defecto
        val nuevoIdioma = if (idiomaActual == "es") "en" else "es" // Alternar entre "es" y "en"

        // Guardar el nuevo idioma en SharedPreferences
        sharedPreferences.edit().putString("idioma", nuevoIdioma).apply()

        // Aplicar el cambio de idioma
        cambiarIdioma(nuevoIdioma)
    }

    private fun cambiarIdioma(localeCode: String) {
        val locale = Locale(localeCode)
        Locale.setDefault(locale)

        val config = Configuration(requireContext().resources.configuration)
        config.setLocale(locale)

        requireContext().resources.updateConfiguration(config, requireContext().resources.displayMetrics)

        requireActivity().recreate() // Recargar la actividad para aplicar cambios
    }
}
