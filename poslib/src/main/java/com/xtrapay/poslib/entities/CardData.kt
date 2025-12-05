package com.xtrapay.poslib.entities

import com.tegritech.poslib.extensions.padLeft
import com.xtrapay.poslib.tlv.TLVList

data class CardData(
    var track2Data: String,
    var nibssIccSubset: String,
    var panSequenceNumber: String,
    var posEntryMode: String = "051"
) {

    var pinBlock: String? = null

    var pan = if(track2Data.isNotEmpty()) track2Data.getPan() else ""
    var serviceCode = if(track2Data.isNotEmpty()) track2Data.getServiceCode() else ""
    var expiryDate = if(track2Data.isNotEmpty()) track2Data.getExpiryDate() else ""
    var acquiringInstitutionIdCode = if(track2Data.isNotEmpty()) track2Data.getAcquiringInstitutionIdCode() else ""
    var aid = ""
    var ac = ""
    var tvr = ""
    var tsi = ""
    var random = ""

    companion object {
        val NIBSS_TAGS = arrayOf(
            "9F26",
            "9F27",
            "9F10",
            "9F37",
            "9F36",
            "95",
            "9A",
            "9C",
            "9F02",
            "5F2A",
            "82",
            "9F1A",
            "9F34",
            "9F33",
            "9F35",
            "9F1E",
            "84",
            "9F09",
            "9F03",
            "5F34",
            "8E",
            "4F",
            "9B",
            "9F41",
            "9F7C",
            "50",
            "5F20",
            "9B",
        )


        fun getNibssTags(iccData: String): String {
            val tlvList = TLVList.fromBinary(iccData)
            val nibssIcc = StringBuilder()
            for (tag in NIBSS_TAGS) {
                if (tlvList.contains(tag)) {
                    nibssIcc.append(tlvList.getTLV(tag).toString())
                }
            }

            return nibssIcc.toString()
        }


        fun initCardDataFromTrack(fullIcc: String): CardData {

            val tlvList = TLVList.fromBinary(fullIcc)
            val nibssIcc = StringBuilder()
            for (tag in NIBSS_TAGS) {
                if (tlvList.contains(tag)) {
                    nibssIcc.append(tlvList.getTLV(tag).toString())
                }
            }

            return CardData(
                nibssIccSubset = nibssIcc.toString(),
                track2Data = tlvList.getTLVVL("57"),
                panSequenceNumber = tlvList.getTLVVL("5F34").padLeft(3, '0'),
                posEntryMode = tlvList.getTLVVL("9F39")
            )
        }

        fun String.getPan(separatingChar: Char = 'D'): String {
            val indexOfToken = this.indexOf(separatingChar, 0, true)

            return this.substring(0, indexOfToken)
        }

        fun String.getExpiryDate(separatingChar: Char = 'D'): String {
            val indexOfToken = this.indexOf(separatingChar, 0, true)
            val indexOfExpiryDate = indexOfToken + 1
            val lengthOfExpiryDate = 4
            return this.substring(indexOfExpiryDate, indexOfExpiryDate + lengthOfExpiryDate)
        }

        fun String.getServiceCode(separatingChar: Char = 'D'): String {
            val indexOfToken = this.indexOf(separatingChar.toString(), 0, true)
            val indexOfServiceCode = indexOfToken + 5
            val lengthOfServiceCode = 3
            return this.substring(
                indexOfServiceCode,
                indexOfServiceCode + lengthOfServiceCode
            )
        }

        fun String.getAcquiringInstitutionIdCode(): String {
            return this.substring(0, 6)
        }

    }
}

fun getCardHolderFromTrack1(track1: String): String {
    val tokens = track1.split("^")

    return tokens[1]
}