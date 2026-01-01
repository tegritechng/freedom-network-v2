package com.threelineng.freedomnetwork.bills.domain

import com.threelineng.freedomnetwork.R

object BillerIconMapper {

    fun resolveIcon(key: String): Int {

        return when (key.lowercase()) {
//            "mtn" -> R.drawable.mtn
//            "airtel" -> R.drawable.airtel
//            "9mobile", "etisalat" -> R.drawable.etisalat
//            "glo" -> R.drawable.glo
            "abuja", "aedc" -> R.drawable.aedc
            "eko" -> R.drawable.ekedc
            "ibadan" -> R.drawable.ibedc
            "kano" -> R.drawable.kedco
            "kaduna" -> R.drawable.kaduna_electric
            "jos" -> R.drawable.jedc
            "benin" -> R.drawable.bedc
            "enugu", "eedc" -> R.drawable.eedc
            "port harcourt", "phedc", "ph" -> R.drawable.phedc
            "yola" -> R.drawable.yedc
            "ikeja" -> R.drawable.ikedc
            "dstv" -> R.drawable.dstv
            "gotv" -> R.drawable.gotv
            "startimes" -> R.drawable.startimes
//            "gtb" -> R.drawable.ic_gtb
//            "access" -> R.drawable.ic_access
            else -> R.drawable.freedom_logo
        }
    }
}