package com.example.fastlate.bluetooth

import android.content.Context
import android.util.Log

class BluetoothService(private val context: Context) {

    fun sendCommand(command: String) {
        // Stub for ESP32 Bluetooth communication
        Log.d("BluetoothService", "Command sent: $command")
    }

    fun sendAudio(audio: ByteArray) {
        // Stream audio to ESP32
    }

    fun receiveAudio(): ByteArray {
        // Receive translated audio
        return ByteArray(0)
    }
}
