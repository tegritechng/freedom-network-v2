package com.threelineng.freedomnetwork.bills.ui.cabletv

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.threelineng.freedomnetwork.bills.ui.cabletv.domain.CableTvProviderItem
import com.threelineng.freedomnetwork.databinding.ContentCableTvProviderItemBinding

class CableTvProviderListAdapter(
    private val onClick: (CableTvProviderItem) -> Unit
) : ListAdapter<CableTvProviderItem, CableTvProviderListAdapter.ItemViewHolder>(
    ITEM_COMPARATOR
) {
    companion object {
        private val TAG = this::class.java.simpleName

        private val ITEM_COMPARATOR = object : DiffUtil.ItemCallback<CableTvProviderItem>() {
            override fun areItemsTheSame(
                oldItem: CableTvProviderItem, newItem: CableTvProviderItem
            ): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(
                oldItem: CableTvProviderItem, newItem: CableTvProviderItem
            ): Boolean {
                return oldItem == newItem
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder =
        ItemViewHolder(
            parent.context, ContentCableTvProviderItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )


    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item: CableTvProviderItem = getItem(position)
        holder.bind(item)
        holder.itemView.setOnClickListener { onClick(item) }
    }

    inner class ItemViewHolder(
        private val context: Context, private val view: ContentCableTvProviderItemBinding
    ) : RecyclerView.ViewHolder(view.root) {
        fun bind(item: CableTvProviderItem) {
            with(item) {
                view.title.text = name
                view.iconBg.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#FCFCFC"))
                view.icon.setImageDrawable(context.getDrawable(icon))
            }
        }
    }
}