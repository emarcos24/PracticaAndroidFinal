package com.euroformac.practicafinal

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.euroformac.practicafinal.room.Partido
import com.euroformac.practicafinal.room.PartidoConEquipos

class PartidosAdapter(private val partidos: List<PartidoConEquipos>) : RecyclerView.Adapter<PartidosAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtEquipoLocal: TextView = view.findViewById(R.id.textViewNombreLocal)
        val txtPartido: TextView = view.findViewById(R.id.textViewResultado)
        val txtEquipoVisitante: TextView = view.findViewById(R.id.textViewNombreVisitante)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_partido, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val partido = partidos[position]
        holder.txtEquipoLocal.text = partido.equipoLocal.nombre
        holder.txtPartido.text = partido.partido.resultadoPartido
        holder.txtEquipoVisitante.text = partido.equipoVisitante.nombre
    }

    override fun getItemCount() = partidos.size
}
