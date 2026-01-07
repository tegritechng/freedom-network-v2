package com.threelineng.freedomnetwork.bills.ui.airtime.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.threelineng.freedomnetwork.R
import com.threelineng.freedomnetwork.common.utils.prependNairaSign

class AmountAdapter(
    private val amounts: List<String> = listOf(
        "50", "100", "500",
        "1,000", "1,500", "2,000",
        "5,000", "10,000", "20,000"
    ),
    private val onClick: (String) -> Unit
) : RecyclerView.Adapter<AmountAdapter.AmountViewHolder>() {

    inner class AmountViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val amountText: TextView = view.findViewById(R.id.tvAmount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AmountViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.content_item_amount, parent, false)
        return AmountViewHolder(view)
    }

    override fun onBindViewHolder(holder: AmountViewHolder, position: Int) {
        val amount = amounts[position]
        holder.amountText.text = amount.prependNairaSign()
        holder.itemView.setOnClickListener { onClick(amount) }
    }

    override fun getItemCount() = amounts.size
}
