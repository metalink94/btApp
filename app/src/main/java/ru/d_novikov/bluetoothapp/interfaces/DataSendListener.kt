package ru.d_novikov.bluetoothapp.interfaces

interface DataSendListener {

    fun onDataReceived(data: Int, state: Int)
}