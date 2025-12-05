package com.xtrapay.poslib.entities

import android.os.Parcelable
import com.xtrapay.poslib.entities.ConnectionData
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
data class HostConfig(val terminalId: String,
                      val connectionData: ConnectionData,
                      var keyHolder: @RawValue KeyHolder,
                      val configData: ConfigData
): Parcelable