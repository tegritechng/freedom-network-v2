package com.threelineng.freedomnetwork.transactions

import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.threelineng.freedomnetwork.R
import com.threelineng.freedomnetwork.common.domain.TransactionStatus
import com.threelineng.freedomnetwork.common.utils.color
import com.threelineng.freedomnetwork.databinding.ContentTransactionItemBinding
import com.threelineng.freedomnetwork.transactions.domain.TransactionItemModel

class TransactionsListAdapter(
    private val onClick: (TransactionItemModel) -> Unit
) : ListAdapter<TransactionItemModel, TransactionsListAdapter.ItemViewHolder>(
    ITEM_COMPARATOR
) {
    companion object {
        private val TAG = this::class.java.simpleName

        private val ITEM_COMPARATOR = object : DiffUtil.ItemCallback<TransactionItemModel>() {
            override fun areItemsTheSame(
                oldItem: TransactionItemModel, newItem: TransactionItemModel
            ): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(
                oldItem: TransactionItemModel, newItem: TransactionItemModel
            ): Boolean {
                return oldItem == newItem
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder =
        ItemViewHolder(
            parent.context, ContentTransactionItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item: TransactionItemModel = getItem(position)
        holder.bind(item)
        holder.itemView.setOnClickListener { onClick(item) }
    }

    inner class ItemViewHolder(
        private val context: Context, private val view: ContentTransactionItemBinding
    ) : RecyclerView.ViewHolder(view.root) {
        fun bind(item: TransactionItemModel) {
            with(item) {
                view.title.text = title
                view.amount.text = context.getString(R.string.amount_format, amount.toString())
                view.status.apply {
                    text = transactionStatus.typeName
                    setTextColor(context.color(if (transactionStatus == TransactionStatus.APPROVED) R.color.success else if (transactionStatus == TransactionStatus.FAILED) R.color.failed else R.color.pending))
                    backgroundTintList = ColorStateList.valueOf(
                        context.getColor(if (transactionStatus == TransactionStatus.APPROVED) R.color.success_bg else if (transactionStatus == TransactionStatus.FAILED) R.color.failed_bg else R.color.pending_bg)
                    )
                }
                view.dateTime.text = trxDateTime
            }
        }
    }
}