package com.peterstev.adapter

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.peterstev.databinding.ItemSearchBinding
import com.peterstev.domain.model.Movie

class SearchViewHolder(private val item: ItemSearchBinding, private val listener: MovieItemClickListener) : RecyclerView.ViewHolder(item.root) {

    fun bind(movie: Movie) {
        movie.posterPath.isNotEmpty().let {
            if (it) Glide.with(item.root.context).load(movie.posterPath).into(item.itemImaqe)
        }
        item.itemText.text = movie.title
        item.itemRating.text = movie.voteAverage.toString()
        item.root.setOnClickListener { listener.onItemClick(movie) }
    }
}
