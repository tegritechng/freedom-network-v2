package com.threelineng.freedomnetwork.network.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.threelineng.freedomnetwork.databinding.ContentNetworkItemBinding
import com.threelineng.freedomnetwork.network.domain.NetworkItemModel

class NetworkListViewAdapter(
    private val onClick: (NetworkItemModel) -> Unit
) : ListAdapter<NetworkItemModel, NetworkListViewAdapter.ItemViewHolder>(
    ITEM_COMPARATOR
) {
    companion object {
        private val TAG = this::class.java.simpleName

        private val ITEM_COMPARATOR = object : DiffUtil.ItemCallback<NetworkItemModel>() {
            override fun areItemsTheSame(
                oldItem: NetworkItemModel, newItem: NetworkItemModel
            ): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(
                oldItem: NetworkItemModel, newItem: NetworkItemModel
            ): Boolean {
                return oldItem == newItem
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder =
        ItemViewHolder(
            parent.context, ContentNetworkItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )


    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item: NetworkItemModel = getItem(position)
        holder.bind(item)
        holder.itemView.setOnClickListener { onClick(item) }
    }

    inner class ItemViewHolder(
        private val context: Context, private val view: ContentNetworkItemBinding
    ) : RecyclerView.ViewHolder(view.root) {
        fun bind(item: NetworkItemModel) {
            with(item) {

                view.initials.apply {
                    text = initials
//                    backgroundTintList = ColorStateList.valueOf()
                }
                view.title.text = name
                view.rate.apply {
                    text = "$performanceRate%"
                    setTextColor(context.getColor(rateColor))
                }
                view.rateIcon.setImageDrawable(context.getDrawable(rateIcon))
            }
        }
    }
}