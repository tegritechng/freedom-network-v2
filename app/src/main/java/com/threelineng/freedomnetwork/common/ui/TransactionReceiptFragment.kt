package com.threelineng.freedomnetwork.common.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.tegritech.commons.utils.viewBinding
import com.tegritech.poslib.extensions.formateDate
import com.threelineng.freedomnetwork.R
import com.threelineng.freedomnetwork.databinding.FragmentHomeBinding
import com.threelineng.freedomnetwork.databinding.FragmentTransactionReceiptBinding
import com.xtrapay.poslib.extensions.formatAmount
import com.xtrapay.poslib.extensions.formatCurrencyAmount
import java.util.Date

class TransactionReceiptFragment : Fragment(R.layout.fragment_transaction_receipt) {
    private val binding by viewBinding<FragmentTransactionReceiptBinding>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadDefaultValues(view)
    }

    private fun loadDefaultValues(view: View) {
        binding.tvAmount.text =
            String.format(getString(R.string.amount_format_ngn), 10000.formatAmount())
        binding.tvStatusValue.text = getString(R.string.successful)
        binding.tvReferenceValue.text = "000290811290123"
        binding.tvPaymentMethodValue.text = getString(R.string.card)
        binding.tvDateTimeValue.text = Date().formateDate("dd MMM, yyyy | hh:mm a")

        Glide.with(this)
            .asGif()
            .load(R.drawable.failed_status)
            .into(view.findViewById(R.id.gifImage))
    }
}