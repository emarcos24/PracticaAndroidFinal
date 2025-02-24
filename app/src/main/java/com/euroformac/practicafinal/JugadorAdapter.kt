package com.euroformac.practicafinal

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.euroformac.practicafinal.R
import com.euroformac.practicafinal.room.Jugador

class JugadorAdapter(private val jugadores: List<Jugador>) :
    RecyclerView.Adapter<JugadorAdapter.JugadorViewHolder>() {

    class JugadorViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textoNombre: TextView = view.findViewById(R.id.textoNombreJugador)
        val textoPosicion: TextView = view.findViewById(R.id.textoPosicionJugador)
        val textoNumero: TextView = view.findViewById(R.id.textoNumeroJugador)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JugadorViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_jugador, parent, false)
        return JugadorViewHolder(view)
    }

    override fun onBindViewHolder(holder: JugadorViewHolder, position: Int) {
        val jugador = jugadores[position]
        holder.textoNombre.text = jugador.nombre
        holder.textoPosicion.text = "Posición: ${jugador.posicion}"
        holder.textoNumero.text = "Dorsal: ${jugador.numero}"
    }

    override fun getItemCount() = jugadores.size
}