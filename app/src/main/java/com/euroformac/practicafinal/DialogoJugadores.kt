package com.euroformac.practicafinal


import android.app.Dialog
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.euroformac.practicafinal.room.Jugador

class DialogoJugadores(
    private val nombreEquipo: String,
    private val manager: String,
    private val logo: String,
    private val listener: OnJugadoresListener
) : DialogFragment() {

    interface OnJugadoresListener {
        fun onJugadoresCompletados(nombreEquipo: String, manager: String, logo: String, jugadores: List<Jugador>)
    }

    private val listaJugadores = mutableListOf<Jugador>()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())
        val inflater = requireActivity().layoutInflater
        val view = inflater.inflate(R.layout.dialogo_jugadores, null)

        val editTextNombre = view.findViewById<EditText>(R.id.editTextJugadorNombre)
        val spinnerPosicion = view.findViewById<Spinner>(R.id.spinnerPosicion)
        val editTextDorsal = view.findViewById<EditText>(R.id.editTextJugadorDorsal)
        val botonAnadir = view.findViewById<Button>(R.id.botonAnadirJugador)
        val textViewLista = view.findViewById<TextView>(R.id.textViewListaJugadores)

        // Opciones permitidas para la posición
        val posiciones = listOf("Base", "Base-Escolta", "Alero", "Ala-Pivot", "Pivot")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, posiciones)
        spinnerPosicion.adapter = adapter

        builder.setView(view)
            .setTitle("Añadir Jugadores")
            .setPositiveButton("Finalizar") { _, _ ->
                if (listaJugadores.size == 5) {
                    listener.onJugadoresCompletados(nombreEquipo, manager, logo, listaJugadores)
                } else {
                    Toast.makeText(context, "Debes añadir 5 jugadores", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancelar") { _, _ -> }

        botonAnadir.setOnClickListener {
            val nombre = editTextNombre.text.toString().trim()
            val posicion = spinnerPosicion.selectedItem.toString()
            val numeroStr = editTextDorsal.text.toString().trim()

            if (nombre.isEmpty() || numeroStr.isEmpty()) {
                Toast.makeText(context, "Completa todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val numero = numeroStr.toIntOrNull()
            if (numero == null || numero <= 0) {
                Toast.makeText(context, "Número inválido", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (listaJugadores.any { it.numero == numero }) {
                Toast.makeText(context, "El dorsal ya está en uso en este equipo", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (listaJugadores.size < 5) {
                val jugador = Jugador(id = 0, equipoId = 0, nombre = nombre, posicion = posicion, numero = numero)
                listaJugadores.add(jugador)

                textViewLista.text = listaJugadores.joinToString("\n") { "${it.nombre} - ${it.posicion} - ${it.numero}" }

                editTextNombre.text.clear()
                editTextDorsal.text.clear()
            } else {
                Toast.makeText(context, "Ya has añadido 5 jugadores", Toast.LENGTH_SHORT).show()
            }
        }

        return builder.create()
    }

}