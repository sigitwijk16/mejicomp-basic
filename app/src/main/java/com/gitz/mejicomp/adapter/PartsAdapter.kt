package com.gitz.mejicomp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gitz.mejicomp.R
import com.gitz.mejicomp.model.Parts

class ListPartsAdapter(private val listParts: ArrayList<Parts>) :
    RecyclerView.Adapter<ListPartsAdapter.ListViewHolder>() {

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_row_parts, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val (title, description, imageResourceID) = listParts[position]

        Glide.with(holder.itemView.context)
            .load(imageResourceID)
            .into(holder.imgParts)

        holder.tvTitle.text = title
        holder.tvDescription.text = description
        holder.itemView.setOnClickListener { onItemClickCallback.onItemClicked(listParts[position]) }
    }


    override fun getItemCount(): Int = listParts.size

    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgParts: ImageView = itemView.findViewById(R.id.img_item_photo)
        val tvTitle: TextView = itemView.findViewById(R.id.tv_item_name)
        val tvDescription: TextView = itemView.findViewById(R.id.tv_item_description)
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: Parts)
    }
}
