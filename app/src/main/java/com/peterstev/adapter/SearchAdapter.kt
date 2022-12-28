package com.peterstev.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.peterstev.databinding.ItemSearchBinding
import com.peterstev.domain.model.Movie

class SearchAdapter(
    private val items: MutableList<Movie>,
    private val listener: MovieItemClickListener,
    recyclerView: RecyclerView,
) : RecyclerView.Adapter<SearchViewHolder>() {

    private var totalItems = 0
    private var lastVisibleItemsPosition = -1
    private var itemThreshold = 3
    private var isLoading: Boolean = false

    init {
        val manager = recyclerView.layoutManager as LinearLayoutManager
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                totalItems = manager.itemCount
                lastVisibleItemsPosition = manager.findLastVisibleItemPosition()
                if (!isLoading && totalItems <= (lastVisibleItemsPosition + itemThreshold)) {
                    listener.loadMore()
                    isLoading = true
                }
            }
        })
    }

    fun stopLoading() {
        isLoading = false
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val binding = ItemSearchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchViewHolder(binding, listener)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size

    fun updateList(list: MutableList<Movie>) {
        val size = items.size
        items.addAll(list)
        notifyItemRangeInserted(size, list.size)
    }

    fun reset() {
        val size = items.size
        items.clear()
        notifyItemRangeRemoved(0, size)
    }
}
