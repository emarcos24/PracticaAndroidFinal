package com.euroformac.practicafinal

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.fragment.app.DialogFragment
import java.util.Calendar
import java.util.Locale

class DialogoPartido(
    private val equipos: List<String>,
    private val listener: OnPartidoListener
) : DialogFragment() {

    interface OnPartidoListener {
        fun onPartidoCreado(
            equipoLocal: String,
            equipoVisitante: String,
            fecha: String,
            jornadaId: Int
        )
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())
        val inflater = requireActivity().layoutInflater
        val view = inflater.inflate(R.layout.dialogo_partido, null)

        val spinnerLocal = view.findViewById<Spinner>(R.id.spinner_local)
        val spinnerVisitante = view.findViewById<Spinner>(R.id.spinner_visitante)
        val editTextFecha = view.findViewById<EditText>(R.id.tv_fecha)
        val editTextJornada = view.findViewById<EditText>(R.id.et_jornada)
        val switchJugado = view.findViewById<Switch>(R.id.switch_jugado)
        val layoutPuntos = view.findViewById<LinearLayout>(R.id.layout_puntos)

        layoutPuntos.visibility = View.GONE

        switchJugado.setOnCheckedChangeListener { _, isChecked ->
            layoutPuntos.visibility = if (isChecked) View.VISIBLE else View.GONE
        }

        val equiposConHint = mutableListOf("Selecciona equipo...").apply { addAll(equipos) }
        val adapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, equiposConHint)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinnerLocal.adapter = adapter
        spinnerVisitante.adapter = adapter

        spinnerLocal.setSelection(0, false)
        spinnerVisitante.setSelection(0, false)

        // Configurar el DatePickerDialog al hacer clic en el EditText de fecha
        editTextFecha.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePicker =
                DatePickerDialog(requireContext(), { _, selectedYear, selectedMonth, selectedDay ->
                    val formattedDate = String.format(
                        Locale("es", "ES"),
                        "%04d-%02d-%02d",
                        selectedYear,
                        selectedMonth + 1,
                        selectedDay
                    )
                    editTextFecha.setText(formattedDate)
                }, year, month, day)

            datePicker.show()
        }

        builder.setView(view)
            .setTitle("Añadir Partido")
            .setPositiveButton("Guardar", null) // Dejar nulo para personalizarlo después
            .setNegativeButton("Cancelar", null)

        val dialog = builder.create()

        dialog.setOnShowListener {
            val button = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            button.setOnClickListener {
                val equipoLocal = spinnerLocal.selectedItem.toString()
                val equipoVisitante = spinnerVisitante.selectedItem.toString()
                val fecha = editTextFecha.text.toString()
                val jornadaStr = editTextJornada.text.toString()

                if (equipoLocal == "Selecciona equipo..." || equipoVisitante == "Selecciona equipo...") {
                    Toast.makeText(context, "Debes seleccionar ambos equipos", Toast.LENGTH_SHORT)
                        .show()
                    return@setOnClickListener
                }

                if (equipoLocal == equipoVisitante) {
                    Toast.makeText(context, "Los equipos no pueden ser iguales", Toast.LENGTH_SHORT)
                        .show()
                    return@setOnClickListener
                }

                if (fecha.isEmpty()) {
                    Toast.makeText(context, "Debes seleccionar una fecha", Toast.LENGTH_SHORT)
                        .show()
                    return@setOnClickListener
                }

                val jornadaId = jornadaStr.toIntOrNull()
                if (jornadaId == null || jornadaId <= 0) {
                    Toast.makeText(context, "Número de jornada inválido", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                // Enviar los datos al listener
                listener.onPartidoCreado(equipoLocal, equipoVisitante, fecha, jornadaId)
                dialog.dismiss()
            }
        }

        return dialog
    }
}
