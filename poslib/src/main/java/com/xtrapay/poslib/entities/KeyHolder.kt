package com.xtrapay.poslib.entities

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.xtrapay.poslib.utils.TripleDES
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity
data class KeyHolder
/**
 *
 * @param masterKey
 * @param sessionKey
 * @param pinKey
 * @param track2Key
 * @param bdk
 * @param isTest
 */
(var masterKey: String = "", var sessionKey: String = "", var pinKey: String = "", var track2Key: String = "",
 var bdk: String = ""): Parcelable {

    @PrimaryKey
    var id = 1
}


val KeyHolder.clearSessionKey: String
    get() = TripleDES.decrypt(this.sessionKey, masterKey)

val KeyHolder.clearPinKey: String
    get() = TripleDES.decrypt(this.pinKey, masterKey)
