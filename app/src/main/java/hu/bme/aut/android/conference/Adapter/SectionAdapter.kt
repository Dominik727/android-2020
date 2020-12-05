package hu.bme.aut.android.conference.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import hu.bme.aut.android.conference.R
import hu.bme.aut.android.conference.model.Section
import kotlinx.android.synthetic.main.item_section.view.*
import okhttp3.internal.notifyAll

class SectionAdapter(private val listener: OnSectionSelectedListener) : RecyclerView.Adapter<SectionAdapter.SectionViewHolder>() { // ktlint-disable max-line-length

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

    fun addSection(section: Section) {

        sections.add(section)
        notifyItemInserted(sections.size - 1)
    }

    fun removeAllSection() {
        sections = ArrayList()
    }

    fun removeSection(position: Int) {
        sections.removeAt(position)
        notifyItemRangeChanged(position, itemCount)
        notifyItemRemoved(position)
    }

    fun getSectionId(section: Section?): Int {
        for (i in 0..itemCount) {
            if (sections[i] == section) {
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
                item?.let { it1 -> listener.onSectionSelected(it1) }
            }
            itemView.setOnLongClickListener {
                item?.let { it1 -> listener.OnLongSectionListener(it1) }
                return@setOnLongClickListener false
            }
        }
        fun bind(section: Section?) {
            item = section
            itemView.SectionNameItemTextView.text = item?.name ?: ""
            itemView.SectionDateEndItemTextView.text = item?.startTime?.dropLast(10)
                ?.replace("T", " ")
                ?: ""
            itemView.SectionDateStartItemTextView.text = item?.startTime?.dropLast(10)
                ?.replace("T", " ")
                ?: ""
        }
    }

    interface OnSectionSelectedListener {
        fun onSectionSelected(section: Section)
        fun OnLongSectionListener(section: Section)
    }
}
