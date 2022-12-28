package com.peterstev.adapter

import androidx.recyclerview.widget.DiffUtil
import com.peterstev.domain.model.Movie

class DiffCallBack(
    private val oldList: MutableList<Movie>,
    private val newList: MutableList<Movie>,
) : DiffUtil.Callback() {

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldList[oldItemPosition].id == newList[newItemPosition].id

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldList[oldItemPosition].overview == newList[newItemPosition].overview

}
