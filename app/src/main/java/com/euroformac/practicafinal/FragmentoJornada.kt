package com.euroformac.practicafinal

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.euroformac.practicafinal.room.AppDatabase
import com.euroformac.practicafinal.room.Partido
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FragmentoJornada : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PartidosAdapter
    private lateinit var database: AppDatabase
    private var jornadaActual = 1 // Jornada por defecto

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_jornada, container, false)

        database = AppDatabase.getDatabase(requireContext())

        recyclerView = view.findViewById(R.id.recyclerViewPartidos)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)

        val botonAnterior: ImageButton = view.findViewById(R.id.imageButton)
        val botonSiguiente: ImageButton = view.findViewById(R.id.imageButton2)
        val textViewJornada: TextView = view.findViewById(R.id.textViewJornada)

        // Cargar la jornada inicial
        cargarPartidos(jornadaActual)

        // Botón para ir a la jornada anterior
        botonAnterior.setOnClickListener {
            if (jornadaActual > 1) {
                jornadaActual--
                cargarPartidos(jornadaActual)
                textViewJornada.text = "Jornada $jornadaActual"
            }
        }

        // Botón para ir a la jornada siguiente
        botonSiguiente.setOnClickListener {
            lifecycleScope.launch {
                val existeJornada = database.jornadaDao.existeJornada(jornadaActual + 1)
                if (existeJornada) {
                    jornadaActual++
                    cargarPartidos(jornadaActual)
                    textViewJornada.text = "Jornada $jornadaActual"
                }
            }
        }

        return view
    }

    private fun cargarPartidos(jornada: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            val listaPartidosConEquipos = database.partidoDao.obtenerPartidosPorJornada(jornada)

            withContext(Dispatchers.Main) {
                adapter = PartidosAdapter(listaPartidosConEquipos)
                recyclerView.adapter = adapter
            }
        }
    }
}
