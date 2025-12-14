package com.threelineng.freedomnetwork.transfer.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.threelineng.freedomnetwork.databinding.ContentBankItemBinding
import com.threelineng.freedomnetwork.transfer.domain.BankItemModel

class BankListViewAdapter(
    private val onClick: (BankItemModel) -> Unit
) : ListAdapter<BankItemModel, BankListViewAdapter.ItemViewHolder>(
    ITEM_COMPARATOR
) {
    companion object {
        private val TAG = this::class.java.simpleName

        private val ITEM_COMPARATOR = object : DiffUtil.ItemCallback<BankItemModel>() {
            override fun areItemsTheSame(
                oldItem: BankItemModel, newItem: BankItemModel
            ): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(
                oldItem: BankItemModel, newItem: BankItemModel
            ): Boolean {
                return oldItem == newItem
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder =
        ItemViewHolder(
            parent.context, ContentBankItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )


    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val bankItem: BankItemModel = getItem(position)
        holder.bind(bankItem)
        holder.itemView.setOnClickListener { onClick(bankItem) }
    }

    inner class ItemViewHolder(
        private val context: Context, private val view: ContentBankItemBinding
    ) : RecyclerView.ViewHolder(view.root) {
        fun bind(item: BankItemModel) {
            with(item) {
                view.bankNameTextview.text = name
//                Glide.with(context).load(
//                    ContextCompat.getDrawable(
//                        context, icon
//                    )
//                ).into(view.bankIcon)
            }
        }
    }
}