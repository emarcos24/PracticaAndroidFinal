package com.euroformac.practicafinal

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import com.euroformac.practicafinal.room.AppDatabase
import com.euroformac.practicafinal.room.EquipoDAO
import com.euroformac.practicafinal.room.JugadorDAO
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FragmentoEquipo.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragmentoEquipo : Fragment() {

    private lateinit var equipoDao: EquipoDAO
    private lateinit var jugadorDao: JugadorDAO
    private var equipoId: Int = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_equipo, container, false)

        equipoId = arguments?.getInt("EQUIPO_ID") ?: -1

        val database = AppDatabase.getDatabase(requireContext())
        equipoDao = database.equipoDao
        jugadorDao = database.jugadorDao

        CoroutineScope(Dispatchers.IO).launch {
            val equipo = equipoDao.obtenerEquipoPorId(equipoId)
            val jugadores = jugadorDao.obtenerJugadoresPorEquipo(equipoId)

            withContext(Dispatchers.Main) {
                view.findViewById<TextView>(R.id.txtNombreEquipo).text = equipo?.nombre

                /*          IMPLEMENTAR EN EL FUTURO
                // Cargar imagen con Glide si usas imágenes en la BD
                // Glide.with(requireContext()).load(equipo?.logo).into(view.findViewById(R.id.imageViewEquipo))
                */
                val listViewJugadores = view.findViewById<ListView>(R.id.listaEquipo)
                val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, jugadores.map { it.nombre })
                listViewJugadores.adapter = adapter
            }
        }

        return view
    }
}
