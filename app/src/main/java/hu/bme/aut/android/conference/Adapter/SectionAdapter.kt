package hu.bme.aut.android.conference.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import hu.bme.aut.android.conference.R
import hu.bme.aut.android.conference.model.Section
import kotlinx.android.synthetic.main.item_section.view.*

class SectionAdapter(private val listener: OnFilmSelectedListener) : RecyclerView.Adapter<SectionAdapter.SectionViewHolder>() { // ktlint-disable max-line-length

    private var sections: MutableList<Section> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SectionViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_section, parent, false)
        return SectionViewHolder(view)
    }

    override fun onBindViewHolder(holder: SectionViewHolder, position: Int) {
        val item = sections[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = sections.size

    fun addFilm(newFilm: Section) {

        sections.add(newFilm)
        notifyItemInserted(sections.size - 1)
    }

    fun removeFilm(position: Int) {
        sections.removeAt(position)
        notifyItemRemoved(position)
        if (position < sections.size) {
            notifyItemRangeChanged(position, sections.size - position)
        }
    }

    fun GetFilmID(film: Section?): Int {
        for (i in 0..itemCount) {
            if (sections[i] == film) {
                return i
            }
        }
        return itemCount + 1
    }

    inner class SectionViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var item: Section? = null

        init {
            itemView.setOnClickListener {
                listener.onFilmSelected(item)
            }
        }
        fun bind(newFilm: Section?) {
            item = newFilm
            itemView.FilmItemNameTextView.text = item!!.name
        }
    }
    interface OnFilmSelectedListener {
        fun onFilmSelected(film: Section?)
    }
}
