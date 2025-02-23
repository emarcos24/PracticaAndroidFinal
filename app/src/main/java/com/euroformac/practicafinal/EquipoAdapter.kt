package com.euroformac.practicafinal

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.euroformac.practicafinal.room.Equipo

class EquipoAdapter(private val equipos: List<Equipo>, private val onItemClick: (Equipo) -> Unit) :
    RecyclerView.Adapter<EquipoAdapter.EquipoViewHolder>() {

    inner class EquipoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val imagenEquipo: ImageView = view.findViewById(R.id.imageViewEquipo)
        private val nombreEquipo: TextView = view.findViewById(R.id.txtNombreEquipo)

        fun bind(equipo: Equipo) {
            nombreEquipo.text = equipo.nombre

            itemView.setOnClickListener {
                onItemClick(equipo)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EquipoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_equipo, parent, false)
        return EquipoViewHolder(view)
    }

    override fun onBindViewHolder(holder: EquipoViewHolder, position: Int) {
        holder.bind(equipos[position])
    }

    override fun getItemCount(): Int = equipos.size
}
