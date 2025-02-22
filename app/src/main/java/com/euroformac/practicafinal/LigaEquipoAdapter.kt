package com.euroformac.practicafinal

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.euroformac.practicafinal.room.Equipo

class LigaEquipoAdapter(
    private val equipos: List<Equipo>,
    private val onItemClick: (Equipo) -> Unit
) : RecyclerView.Adapter<LigaEquipoAdapter.EquipoViewHolder>() {

    inner class EquipoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val imagenEquipo: ImageView = view.findViewById(R.id.imgLogoEquipo)

        fun bind(equipo: Equipo) {
            val contexto = itemView.context
            val resId = contexto.resources.getIdentifier(equipo.logo, "drawable", contexto.packageName)
            if (resId != 0) {
                imagenEquipo.setImageResource(resId)
            } else {
                imagenEquipo.setImageResource(R.drawable.logodefault )
            }
            itemView.setOnClickListener { onItemClick(equipo) }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EquipoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_equipo_liga, parent, false)
        return EquipoViewHolder(view)
    }

    override fun onBindViewHolder(holder: EquipoViewHolder, position: Int) {
        holder.bind(equipos[position])
    }

    override fun getItemCount(): Int = equipos.size
}
