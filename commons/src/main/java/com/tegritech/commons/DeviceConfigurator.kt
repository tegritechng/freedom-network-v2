package com.xtrapay.commons

import android.content.Context
import com.xtrapay.poslib.entities.ConfigData
import com.xtrapay.poslib.entities.KeyHolder
import com.xtrapay.poslib.entities.NibssAID
import com.xtrapay.poslib.entities.NibssCA

interface DeviceConfigurator {
    fun processKeys(context: Context, keyHolder: KeyHolder): Boolean
    fun processConfigData(context: Context, terminalID: String, configData: ConfigData): Boolean
    fun processCAs(context: Context, caList: List<NibssCA>): Boolean
    fun processAIDs(context: Context, aidList: List<NibssAID>): Boolean
}