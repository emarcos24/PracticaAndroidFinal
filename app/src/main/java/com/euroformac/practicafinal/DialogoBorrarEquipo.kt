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

class DialogoBorrarEquipo(private val listener: OnEquipoBorradoListener) : DialogFragment() {

    interface OnEquipoBorradoListener {
        fun onEquipoBorrado()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())
        val roomDB = AppDatabase.getDatabase(requireContext())
        val equipoDao = roomDB.equipoDao

        val equipos = mutableListOf<String>()
        val idsEquipos = mutableListOf<Int>()

        val dialog = builder.setTitle("Selecciona un equipo para borrar")
            .setNegativeButton("Cancelar", null)
            .create()

        lifecycleScope.launch(Dispatchers.IO) {
            val listaEquipos = equipoDao.obtenerEquipos()

            if (listaEquipos.isEmpty()) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "No hay equipos para borrar", Toast.LENGTH_SHORT).show()
                }
                return@launch
            }

            listaEquipos.forEach { equipo ->
                equipos.add(equipo.nombre)
                idsEquipos.add(equipo.id)
            }

            withContext(Dispatchers.Main) {
                val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_single_choice, equipos)

                builder.setSingleChoiceItems(adapter, -1) { dialogInterface, which ->
                    val equipoSeleccionado = idsEquipos[which]

                    AlertDialog.Builder(requireContext())
                        .setTitle("Confirmar eliminación")
                        .setMessage("¿Estás seguro de que quieres borrar el equipo '${equipos[which]}'?")
                        .setPositiveButton("Sí") { _, _ ->
                            lifecycleScope.launch(Dispatchers.IO) {
                                equipoDao.borrarEquipoPorId(equipoSeleccionado)

                                withContext(Dispatchers.Main) {
                                    listener.onEquipoBorrado()
                                    Toast.makeText(context, "Equipo eliminado", Toast.LENGTH_SHORT).show()
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
