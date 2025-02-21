package com.euroformac.practicafinal

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.euroformac.practicafinal.room.AppDatabase
import com.euroformac.practicafinal.room.EquipoDAO
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class FragmentoLiga : Fragment(R.layout.fragment_liga) {

    private lateinit var recyclerView: RecyclerView
    private lateinit var equipoAdapter: EquipoAdapter
    private lateinit var equipoDao: EquipoDAO

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_liga, container, false)

        recyclerView = view.findViewById(R.id.recyclerViewEquipos)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val database = AppDatabase.getDatabase(requireContext())
        equipoDao = database.equipoDao

        CoroutineScope(Dispatchers.IO).launch {
            val equipos = equipoDao.obtenerEquipos()
            withContext(Dispatchers.Main) {
                equipoAdapter = EquipoAdapter(equipos) { equipo ->
                    val fragmentoEquipo = FragmentoEquipo().apply {
                        arguments = Bundle().apply {
                            putInt("EQUIPO_ID", equipo.id)
                        }
                    }
                    requireActivity().supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainerView, fragmentoEquipo) // Usa el ID correcto
                        .addToBackStack(null)
                        .commit()

                }
                recyclerView.adapter = equipoAdapter
            }
        }

        return view
    }
}
