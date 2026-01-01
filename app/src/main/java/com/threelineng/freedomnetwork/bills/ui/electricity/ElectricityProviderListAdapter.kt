package com.threelineng.freedomnetwork.bills.ui.electricity

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.threelineng.freedomnetwork.bills.ui.electricity.domain.ElectricityTvProviderItem
import com.threelineng.freedomnetwork.databinding.ContentElectricityProviderItemBinding

class ElectricityProviderListAdapter(
    private val onClick: (ElectricityTvProviderItem) -> Unit
) : ListAdapter<ElectricityTvProviderItem, ElectricityProviderListAdapter.ItemViewHolder>(
    ITEM_COMPARATOR
) {
    companion object {
        private val TAG = this::class.java.simpleName

        private val ITEM_COMPARATOR = object : DiffUtil.ItemCallback<ElectricityTvProviderItem>() {
            override fun areItemsTheSame(
                oldItem: ElectricityTvProviderItem, newItem: ElectricityTvProviderItem
            ): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(
                oldItem: ElectricityTvProviderItem, newItem: ElectricityTvProviderItem
            ): Boolean {
                return oldItem == newItem
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder =
        ItemViewHolder(
            parent.context, ContentElectricityProviderItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )


    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item: ElectricityTvProviderItem = getItem(position)
        holder.bind(item)
        holder.itemView.setOnClickListener { onClick(item) }
    }

    inner class ItemViewHolder(
        private val context: Context, private val view: ContentElectricityProviderItemBinding
    ) : RecyclerView.ViewHolder(view.root) {
        fun bind(item: ElectricityTvProviderItem) {
            with(item) {
                view.initials.apply {
                    text = initials
                }
                view.iconBg.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#FCFCFC"))
                view.title.text = name
                view.icon.setImageDrawable(context.getDrawable(icon))
            }
        }
    }
}