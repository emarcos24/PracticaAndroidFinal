package com.euroformac.practicafinal

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.euroformac.practicafinal.room.AppDatabase
import com.euroformac.practicafinal.room.EquipoDAO
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FragmentoLiga : Fragment(R.layout.fragment_liga) {

    private lateinit var recyclerViewEquipos: RecyclerView
    private lateinit var equipoAdapter: LigaEquipoAdapter
    private lateinit var equipoDao: EquipoDAO

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_liga, container, false)

        val database = AppDatabase.getDatabase(requireContext())
        equipoDao = database.equipoDao

        // RecyclerView para equipos en cuadrícula de 3 columnas
        recyclerViewEquipos = view.findViewById(R.id.recyclerViewEquipos)
        recyclerViewEquipos.layoutManager = GridLayoutManager(requireContext(), 3)
        cargarEquipos()

        return view
    }

    private fun cargarEquipos() {
        CoroutineScope(Dispatchers.IO).launch {
            val equipos = equipoDao.obtenerEquipos()
            Log.d("FragmentoLiga", "Equipos obtenidos: $equipos")

            withContext(Dispatchers.Main) {
                equipoAdapter = LigaEquipoAdapter(equipos) { equipo ->
                    abrirFragmentoEquipo(equipo.id)
                }
                recyclerViewEquipos.adapter = equipoAdapter
            }
        }
    }

    private fun abrirFragmentoEquipo(equipoId: Int) {
        val fragmentoEquipo = FragmentoEquipo().apply {
            arguments = Bundle().apply {
                putInt("EQUIPO_ID", equipoId)
            }
        }
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerView, fragmentoEquipo)
            .addToBackStack(null)
            .commit()
    }
}
