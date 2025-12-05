package com.xtrapay.commons

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.xtrapay.poslib.entities.HostConfig
import com.xtrapay.commons.printer.Printer

interface SmartAppInterface {
    fun initializeDevice(context: Context)
    fun destruct(context: Context)
    fun getPrinter(context: Context): Printer

    val device: Device
    val terminalSerial: String

    val deviceConfigurator: DeviceConfigurator

    val beeper: Beeper

    fun getEmvProcessor(activity: AppCompatActivity,
                        hostConfig: HostConfig
    ): EmvProcessor
}

enum class Device {
    NETPOS
}