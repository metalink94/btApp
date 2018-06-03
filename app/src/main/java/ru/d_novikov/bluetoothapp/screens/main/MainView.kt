package ru.d_novikov.bluetoothapp.screens.main

import android.bluetooth.BluetoothAdapter
import ru.d_novikov.bluetoothapp.models.AlertModel
import ru.d_novikov.bluetoothapp.models.SaveModel
import ru.d_novikov.bluetoothapp.mvp.IView

interface MainView : IView {
    fun showAlertDialog()
    fun initPager(bluetoothAdapter: BluetoothAdapter?)
    fun saveToFile(format: String, dataList: MutableList<SaveModel>)
    fun showAlert(model: AlertModel)
    fun hideAlert()
    fun setStatus(state: Int)

}
