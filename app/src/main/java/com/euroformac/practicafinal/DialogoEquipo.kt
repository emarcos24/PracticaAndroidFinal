package com.euroformac.practicafinal

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.euroformac.practicafinal.R

class DialogoEquipo(private val listener: OnEquipoListener) : DialogFragment() {

    interface OnEquipoListener {
        fun onEquipoIntroducido(nombre: String, manager: String, logo: String)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())
        val inflater = requireActivity().layoutInflater
        val view = inflater.inflate(R.layout.dialogo_equipo, null)

        val editTextNombre = view.findViewById<EditText>(R.id.editTextNombre)
        val editTextManager = view.findViewById<EditText>(R.id.editTextManager)
        val spinnerLogos = view.findViewById<Spinner>(R.id.spinnerLogos)

        // Obtener nombres de los drawables que empiezan con logo
        val fieldDrawables = R.drawable::class.java.fields
        val logosMap = fieldDrawables.mapNotNull { field ->
            try {
                val resourceName = field.name
                if (resourceName.startsWith("logo")) {
                    val formattedName = resourceName.removePrefix("logo")
                        .replace(Regex("([a-z])([A-Z])"), "$1 $2")
                        .replace("_", " ")
                        .trim()
                        .split(" ")
                        .joinToString(" ") { it.replaceFirstChar(Char::uppercaseChar) }
                    resourceName to formattedName
                } else null
            } catch (e: Exception) {
                null
            }
        }.toMap()

        val defaultLogoKey = "logodefault"  // Nombre real del logo por defecto en drawable
        val defaultLogoName = "Logo Por Defecto"
        val hintLogo = "Selecciona un logo"

        val logoNombres = mutableListOf(hintLogo, defaultLogoName) +
                logosMap.values.filter { it != defaultLogoName }

        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, logoNombres)
        spinnerLogos.adapter = adapter

        builder.setView(view)
            .setTitle("Añadir equipo")
            .setPositiveButton("Siguiente", null) // Dejar el botón sin acción para personalizarlo luego
            .setNegativeButton("Cancelar") { _, _ -> }

        val dialog = builder.create()

        // Botón "Siguiente"
        dialog.setOnShowListener {
            val button = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            button.setOnClickListener {
                val nombre = editTextNombre.text.toString().trim()
                val manager = editTextManager.text.toString().trim()
                val logoSeleccionado = spinnerLogos.selectedItem.toString()

                if (nombre.isEmpty() || manager.isEmpty() || logoSeleccionado == hintLogo) {
                    Toast.makeText(context, "Rellena todos los campos y elige un logo", Toast.LENGTH_SHORT).show()
                } else {
                    val logoRealName = logosMap.entries.find { it.value == logoSeleccionado }?.key ?: defaultLogoKey
                    listener.onEquipoIntroducido(nombre, manager, logoRealName)
                    dialog.dismiss()
                }
            }
        }

        return dialog
    }
}
