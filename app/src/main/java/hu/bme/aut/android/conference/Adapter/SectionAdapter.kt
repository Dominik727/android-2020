/*
 * Copyright (c) 2020
 * Created by Suszter Dominik on 2020. 12. 6
 * Copyright © 2020. RR. All rights reserved.
 */

package hu.bme.aut.android.conference.Adapter

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import hu.bme.aut.android.conference.Base.DateFormatter
import hu.bme.aut.android.conference.Dashboard.HomeDashboard
import hu.bme.aut.android.conference.R
import hu.bme.aut.android.conference.model.Section
import kotlinx.android.synthetic.main.item_section.view.*

class SectionAdapter(private val listener: OnSectionSelectedListener) :
    RecyclerView.Adapter<SectionAdapter.SectionViewHolder>() {

    private var sections: MutableList<Section> = ArrayList()
    private lateinit var res: Resources

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SectionViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_section, parent, false)
        return SectionViewHolder(view)
    }

    override fun onBindViewHolder(holder: SectionViewHolder, position: Int) {
        res = holder.itemView.context.resources
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
                item?.let { it1 -> listener.onLongSectionListener(it1) }
                return@setOnLongClickListener false
            }
        }
        fun bind(section: Section?) {
            item = section
            if (item?.let { HomeDashboard.USER?.sections?.contains(it) } == true) {
                itemView.UserIsSelectedSection.visibility = View.VISIBLE
            }
            itemView.RoomNameItemTextView.text = item?.name ?: ""
            itemView.SectionDateEndItemTextView.text = item?.startTime?.let {
                DateFormatter.shared.formatStringToShow(it)
            }
            itemView.SectionDateStartItemTextView.text = item?.endTime?.let {
                DateFormatter.shared.formatStringToShow(it)
            }
        }
    }

    interface OnSectionSelectedListener {
        fun onSectionSelected(section: Section)
        fun onLongSectionListener(section: Section)
    }
}
