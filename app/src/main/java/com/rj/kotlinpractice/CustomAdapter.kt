package com.rj.kotlinpractice

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.*;

class CustomAdapter(var mainActivity: MainActivity, var moviesList: MutableList<MovieContent>) : RecyclerView.Adapter<CustomAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(mainActivity).inflate(R.layout.recyclerview_item,parent,false)
        view.setOnClickListener(mainActivity)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
       return moviesList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val picasso: Picasso = Picasso.get().apply {
            load("https://image.tmdb.org/t/p/w500/"+ moviesList[position].imageURL)
                .into(holder.imageView)
        }
        picasso.snapshot
    }

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        var imageView:ImageView = itemView.findViewById(R.id.api_imageView)
    }
}