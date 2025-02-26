package com.euroformac.practicafinal

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.euroformac.practicafinal.room.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DialogoBorrarPartido(private val listener: OnPartidoBorradoListener) : DialogFragment() {

    interface OnPartidoBorradoListener {
        fun onPartidoBorrado()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())
        val roomDB = AppDatabase.getDatabase(requireContext())
        val partidoDao = roomDB.partidoDao
        val equipoDao = roomDB.equipoDao
        val jornadaDao = roomDB.jornadaDao

        val partidos = mutableListOf<String>()
        val idsPartidos = mutableListOf<Int>()

        val dialog = builder.setTitle("Selecciona un partido para borrar")
            .setNegativeButton("Cancelar", null)
            .create()

        lifecycleScope.launch(Dispatchers.IO) {
            val listaPartidos = partidoDao.obtenerTodosLosPartidos()

            if (listaPartidos.isEmpty()) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "No hay partidos para borrar", Toast.LENGTH_SHORT).show()
                }
                return@launch
            }

            for (partido in listaPartidos) {
                val local = equipoDao.obtenerEquipoPorId(partido.localId)
                val visitante = equipoDao.obtenerEquipoPorId(partido.visitanteId)
                val jornada = jornadaDao.getJornadaById(partido.jornadaId)

                val partidoTexto = "Jornada ${jornada?.numeroJornada}: ${local?.nombre} vs ${visitante?.nombre} (${partido.fecha})"
                partidos.add(partidoTexto)
                idsPartidos.add(partido.id)
            }

            withContext(Dispatchers.Main) {
                val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_single_choice, partidos)

                builder.setSingleChoiceItems(adapter, -1) { dialogInterface, which ->
                    val partidoSeleccionado = idsPartidos[which]

                    AlertDialog.Builder(requireContext())
                        .setTitle("Confirmar eliminación")
                        .setMessage("¿Estás seguro de que quieres borrar este partido?")
                        .setPositiveButton("Sí") { _, _ ->
                            lifecycleScope.launch(Dispatchers.IO) {
                                partidoDao.borrarPartidoPorId(partidoSeleccionado)

                                withContext(Dispatchers.Main) {
                                    listener.onPartidoBorrado()
                                    Toast.makeText(context, "Partido eliminado", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                        .setNegativeButton("No", null)
                        .show()

                    dialogInterface.dismiss()
                }

                builder.setView(null) // Limpia la vista previa
                val finalDialog = builder.create()
                finalDialog.listView.adapter = adapter
                finalDialog.show()
            }
        }

        return dialog
    }
}
