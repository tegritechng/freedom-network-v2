package com.threelineng.freedomnetwork.home.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.threelineng.freedomnetwork.databinding.ContentHomeGridItemBinding
import com.threelineng.freedomnetwork.home.domain.HomeItemModel

class HomeGridViewAdapter(
    private val context: Context,
    private val items: List<HomeItemModel>,
    private val onClick: (HomeItemModel) -> Unit
) : RecyclerView.Adapter<HomeGridViewAdapter.ItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder =
        ItemViewHolder(
            ContentHomeGridItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(items[position])
        holder.itemView.setOnClickListener {
            //holder.itemView.setBackgroundColor(context.resources.getColor(R.color.primary_yellow))
            onClick(items[position])
        }
    }

    inner class ItemViewHolder(private val view: ContentHomeGridItemBinding) :
        RecyclerView.ViewHolder(view.root) {
        fun bind(item: HomeItemModel) {
            with(item) {
                view.title.text = context.getString(type.title)
                Glide.with(context).load(
                    ContextCompat.getDrawable(
                        context, icon
                    )
                ).into(view.icon)
            }
        }
    }
}