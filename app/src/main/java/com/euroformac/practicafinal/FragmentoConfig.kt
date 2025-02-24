package com.euroformac.practicafinal

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
import com.euroformac.practicafinal.room.AppDatabase
import com.euroformac.practicafinal.room.Equipo
import com.euroformac.practicafinal.room.Jornada
import com.euroformac.practicafinal.room.Jugador
import com.euroformac.practicafinal.room.Partido
import kotlinx.coroutines.launch

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FragmentoConfig.newInstance] factory method to
 * create an instance of this fragment.
 */
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
        val botonMiEquipo = view.findViewById<Button>(R.id.button6)
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
            val dialogoBorrar = DialogoBorrarEquipo(object : DialogoBorrarEquipo.OnEquipoBorradoListener {
                override fun onEquipoBorrado() {
                }
            })
            dialogoBorrar.show(parentFragmentManager, "DialogoBorrarEquipo")
        }

        botonAnadirPartido.setOnClickListener {
            lifecycleScope.launch {
                val equipos = obtenerEquiposDesdeBD()
                val dialogo = DialogoPartido(equipos, object : DialogoPartido.OnPartidoListener {
                    override fun onPartidoCreado(equipoLocal: String, equipoVisitante: String, fecha: String, jornadaId: Int) {
                        guardarPartidoEnRoom(equipoLocal, equipoVisitante, fecha, jornadaId)
                    }
                })
                dialogo.show(parentFragmentManager, "DialogoPartido")
            }
        }

        botonBorrarPartido.setOnClickListener {
            // Acción para borrar partido
        }

        botonMiEquipo.setOnClickListener {
            // Acción para ver mi equipo
        }

        botonIdioma.setOnClickListener {
            // Acción para cambiar idioma
        }

    }

    private fun abrirDialogoJugadores(nombre: String, manager: String, logo: String) {
        val dialogo = DialogoJugadores(nombre, manager, logo, object : DialogoJugadores.OnJugadoresListener {
            override fun onJugadoresCompletados(nombreEquipo: String, manager: String, logo: String, jugadores: List<Jugador>) {
                guardarEquipoEnRoom(nombreEquipo, manager, logo, jugadores)
            }
        })
        dialogo.show(parentFragmentManager, "DialogoJugadores")
    }

    private fun guardarEquipoEnRoom(nombre: String, manager: String, logo: String, jugadores: List<Jugador>) {
        lifecycleScope.launch {
            val equipo = Equipo(nombre = nombre, manager = manager, logo = logo)
            val equipoId = roomDB.equipoDao.insertarEquipo(equipo).toInt()

            val jugadoresConEquipo = jugadores.map { it.copy(equipoId = equipoId) }
            roomDB.equipoDao.insertarJugadores(jugadoresConEquipo)

            Toast.makeText(requireContext(), "Equipo guardado con éxito", Toast.LENGTH_SHORT).show()
        }
    }

    private fun guardarPartidoEnRoom(equipoLocal: String, equipoVisitante: String, fecha: String, jornadaId: Int) {
        lifecycleScope.launch {
            val equipoDao = roomDB.equipoDao
            val jornadaDao = roomDB.jornadaDao

            val local = equipoDao.obtenerEquipoPorNombre(equipoLocal)
            val visitante = equipoDao.obtenerEquipoPorNombre(equipoVisitante)

            // Verificar si la jornada ya existe
            var jornada = jornadaDao.getJornadaById(jornadaId)
            if (jornada == null) {
                // Si no existe, la creamos y guardamos
                jornada = Jornada(id = jornadaId, numeroJornada = jornadaId)
                jornadaDao.insertarJornada(jornada)
                Log.d("RoomDebug", "Jornada creada con ID: $jornadaId")
            }

            if (local == null || visitante == null) {
                Log.e("RoomError", "Error: Clave foránea no encontrada (localId=${local?.id}, visitanteId=${visitante?.id}, jornadaId=${jornada.id})")
                return@launch
            }

            val partido = Partido(
                localId = local.id,
                visitanteId = visitante.id,
                fecha = fecha,
                jornadaId = jornada.id,
                marcadorLocal = 0,
                marcadorVisitante = 0
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




}