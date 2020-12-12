/*
 * Copyright (c) 2020
 * Created by Suszter Dominik on 2020. 12. 12
 * Copyright © 2020. RR. All rights reserved.
 */

package hu.bme.aut.android.conference.Adapter

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import hu.bme.aut.android.conference.R
import hu.bme.aut.android.conference.model.Lecture
import kotlinx.android.synthetic.main.activity_section_detail.view.*
import kotlinx.android.synthetic.main.item_lecture.view.*
import kotlinx.android.synthetic.main.item_section.view.*
import kotlinx.android.synthetic.main.item_section.view.SectionNameItemTextView

class LectureAdapter(private val listener: OnLectureSelectedListener) :
    RecyclerView.Adapter<LectureAdapter.LectureViewHolder>() {

    private var lectures: MutableList<Lecture> = ArrayList()
    private lateinit var res: Resources

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): LectureAdapter.LectureViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_lecture, parent, false)
        return LectureViewHolder(view)
    }

    override fun onBindViewHolder(holder: LectureViewHolder, position: Int) {
        res = holder.itemView.context.resources
        val item = lectures[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = lectures.size

    fun addLecture(lecture: Lecture) {
        lectures.add(lecture)
        notifyItemInserted(lectures.size - 1)
    }

    fun removeAllLecture() {
        lectures = ArrayList()
    }

    fun removeLecture(position: Int) {
        lectures.removeAt(position)
        notifyItemRangeChanged(position, itemCount)
        notifyItemRemoved(position)
    }

    fun getLectureId(lecture: Lecture?): Int {
        for (i in 0..itemCount) {
            if (lectures[i] == lecture) {
                return i
            }
        }
        return itemCount + 1
    }

    inner class LectureViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var item: Lecture? = null

        init {
            itemView.setOnClickListener {
                item?.let { it1 -> listener.onLectureSelected(it1) }
            }
            itemView.setOnLongClickListener {
                item?.let { it1 -> listener.onLongLectureListener(it1) }
                return@setOnLongClickListener false
            }
        }
        fun bind(lecture: Lecture?) {
            item = lecture
            itemView.SectionNameItemTextView.text = item?.name ?: ""
        }
    }

    interface OnLectureSelectedListener {
        fun onLectureSelected(lecture: Lecture)
        fun onLongLectureListener(lecture: Lecture)
    }
}
