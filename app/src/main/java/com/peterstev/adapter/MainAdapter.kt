package com.peterstev.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.peterstev.databinding.ItemMainBinding
import com.peterstev.domain.model.Movie

class MainAdapter(
    private val items: MutableList<Movie>,
    private val listener: MovieItemClickListener,
) : RecyclerView.Adapter<MainViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val binding = ItemMainBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MainViewHolder(binding, listener)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size

    fun updateList(list: MutableList<Movie>) {
        val callback = DiffCallBack(items, list)
        val result = DiffUtil.calculateDiff(callback)
        items.apply {
            clear()
            addAll(list)
        }
        result.dispatchUpdatesTo(this)
    }
}
