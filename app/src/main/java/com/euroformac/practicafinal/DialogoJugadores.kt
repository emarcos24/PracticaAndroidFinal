package com.euroformac.practicafinal


import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.euroformac.practicafinal.R
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
        val editTextPosicion = view.findViewById<EditText>(R.id.editTextJugadorPosicion)
        val editTextDorsal = view.findViewById<EditText>(R.id.editTextJugadorDorsal)
        val buttonAñadir = view.findViewById<Button>(R.id.buttonAñadirJugador)
        val textViewLista = view.findViewById<TextView>(R.id.textViewListaJugadores)

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

        buttonAñadir.setOnClickListener {
            val nombre = editTextNombre.text.toString().trim()
            val posicion = editTextPosicion.text.toString().trim()
            val numeroStr = editTextDorsal.text.toString().trim()

            if (nombre.isNotEmpty() && posicion.isNotEmpty() && numeroStr.isNotEmpty()) {
                val numero = numeroStr.toIntOrNull()
                if (numero == null || numero <= 0) {
                    Toast.makeText(context, "Número inválido", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                if (listaJugadores.size < 5) {
                    val jugador = Jugador(id = 0, equipoId = 0, nombre = nombre, posicion = posicion, numero = numero)
                    listaJugadores.add(jugador)

                    textViewLista.text = listaJugadores.joinToString("\n") { "${it.nombre} - ${it.posicion} - ${it.numero}" }

                    editTextNombre.text.clear()
                    editTextPosicion.text.clear()
                    editTextDorsal.text.clear()
                } else {
                    Toast.makeText(context, "Ya has añadido 5 jugadores", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(context, "Completa todos los campos", Toast.LENGTH_SHORT).show()
            }
        }


        return builder.create()
    }
}