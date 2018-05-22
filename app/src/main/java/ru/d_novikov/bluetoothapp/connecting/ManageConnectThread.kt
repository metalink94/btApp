package ru.d_novikov.bluetoothapp.connecting

import android.bluetooth.BluetoothSocket

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

class ManageConnectThread : Thread() {

    @Throws(IOException::class)
    fun sendData(socket: BluetoothSocket, data: Int) {
        val output = ByteArrayOutputStream(4)
        output.write(data)
        val outputStream = socket.outputStream
        outputStream.write(output.toByteArray())
    }

    @Throws(IOException::class)
    fun receiveData(socket: BluetoothSocket): Int {
        val buffer = ByteArray(4)
        val input = ByteArrayInputStream(buffer)
        val inputStream = socket.inputStream
        inputStream.read(buffer)
        return input.read()
    }
}