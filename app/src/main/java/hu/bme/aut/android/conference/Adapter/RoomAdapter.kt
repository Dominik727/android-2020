/*
 * Copyright (c) 2020
 * Created by Suszter Dominik on 2020. 12. 13
 * Copyright © 2020. RR. All rights reserved.
 */

package hu.bme.aut.android.conference.Adapter

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import hu.bme.aut.android.conference.R
import hu.bme.aut.android.conference.model.Room
import kotlinx.android.synthetic.main.item_section.view.*

class RoomAdapter(private val listener: OnRoomSelectedListener) : RecyclerView.Adapter<RoomAdapter.RoomViewHolder>() {

    private var rooms: MutableList<Room> = ArrayList()
    private lateinit var res: Resources

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RoomAdapter.RoomViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_lecture, parent, false)
        return RoomViewHolder(view)
    }

    override fun onBindViewHolder(holder: RoomAdapter.RoomViewHolder, position: Int) {
        res = holder.itemView.context.resources
        val item = rooms[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = rooms.size

    fun addRoom(room: Room) {
        rooms.add(room)
        notifyItemInserted(rooms.size - 1)
    }

    fun removeAllRoom() {
        rooms = ArrayList()
    }

    fun removeRoom(position: Int) {
        rooms.removeAt(position)
        notifyItemRangeChanged(position, itemCount)
        notifyItemRemoved(position)
    }

    fun getLectureId(room: Room): Int {
        for (i in 0..itemCount) {
            if (rooms[i] == room) {
                return i
            }
        }
        return itemCount + 1
    }

    inner class RoomViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var item: Room? = null

        init {
            itemView.setOnClickListener {
                item?.let { it1 -> listener.onRoomSelected(it1) }
            }
            itemView.setOnLongClickListener {
                item?.let { it1 -> listener.onLongRoomListener(it1) }
                return@setOnLongClickListener false
            }
        }
        fun bind(room: Room?) {
            item = room
            itemView.RoomNameItemTextView.text = item?.name ?: ""
        }
    }

    interface OnRoomSelectedListener {
        fun onRoomSelected(room: Room)
        fun onLongRoomListener(room: Room)
    }
}
