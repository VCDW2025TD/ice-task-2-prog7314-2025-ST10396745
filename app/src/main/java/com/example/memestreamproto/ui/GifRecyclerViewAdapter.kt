package com.example.memestreamproto.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.memestreamproto.R
import com.example.memestreamproto.data.Gif

class GifRecyclerViewAdapter(
    private val items: List<Gif>
) : RecyclerView.Adapter<GifRecyclerViewAdapter.ViewHolder>() {

    //Get values from xml
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val gifImageView: ImageView = view.findViewById(R.id.gifImageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_gif, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val gifItem = items[position]

        //https://bumptech.github.io/glide/doc/getting-started.html
        Glide.with(holder.itemView.context)
            .load(gifItem.images.original.url)
            .into(holder.gifImageView)
    }

    override fun getItemCount() = items.size
}
