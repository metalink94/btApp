package ru.d_novikov.bluetoothapp.screens.openScreen

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Intent
import android.os.Message
import io.realm.Realm
import io.realm.kotlin.createObject
import io.realm.kotlin.where
import ru.d_novikov.bluetoothapp.models.BdModel
import ru.d_novikov.bluetoothapp.mvp.ViewPresenter
import java.util.*


class OpenScreenPresenter : ViewPresenter<OpenScreenView>() {

    lateinit var bluetoothAdapter: BluetoothAdapter
    val BLUETOOTH_DEVICE_NAME = "H-C-2010-06-01"
    var isServiceStart: Boolean = false
    val realm = Realm.getDefaultInstance()

    fun onCreate(bluetoothAdapter: BluetoothAdapter?) {
        if (bluetoothAdapter == null) return
        this.bluetoothAdapter = bluetoothAdapter
        realm.executeTransaction { realm ->
            realm.deleteAll()
        }
        getView()?.setTimer()
    }

    private fun getPairedDevices() {
        val pairedDevices = bluetoothAdapter.bondedDevices
        if (pairedDevices.isNotEmpty()) {
            for (device in pairedDevices) {
                onReceive(device)
            }
        } else {
            startScan()
        }
    }

    private fun startScan() {
        getView()?.scanDevices(bluetoothAdapter)
    }

    fun onButtonClick() {
        if (!isServiceStart) {
            getView()?.setButtonStart()
            getView()?.startTimer()
            isServiceStart = true
            startSearchDevices()
        } else {
            getView()?.setButtonStop()
            getView()?.stopTimer()
            getView()?.stopListener()
            isServiceStart = false
        }
    }

    private fun startSearchDevices() {
        if (!bluetoothAdapter.isEnabled) {
            getView()?.onBluetooth()
        } else {
            getPairedDevices()
        }
    }

    private fun stopScan() {
        getView()?.stopScanDevices()
        bluetoothAdapter.cancelDiscovery()
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 0 && resultCode == Activity.RESULT_OK) {
            getPairedDevices()
        }
    }

    fun onReceive(device: BluetoothDevice) {
        if (device.name != null && device.name == BLUETOOTH_DEVICE_NAME) {
            getView()?.connect(device)
            stopScan()
        }
    }

    fun onHandleMessage(msg: Message) {
        when (msg.what) {
            OpenScreenFragment.MESSAGE_WRITE -> {
                // something if we will send
            }
            OpenScreenFragment.MESSAGE_READ -> {
                val readBuf = msg.obj as ByteArray
                // construct a string from the valid bytes in the buffer
                val readMessage = String(readBuf, 0, msg.arg1)
                val str = readMessage.replace("[^\\d.]", "")
                if (str.matches(Regex("[0-9]+"))) {
                    addToRealm(str)
                    getView()?.onDataReceived(str)
                }
            }
            OpenScreenFragment.MESSAGE_DEVICE_NAME -> {
                // save the connected device's name
                val mConnectedDeviceName = msg.data.getString(OpenScreenFragment.DEVICE_NAME)
                getView()?.showToast("Connected to $mConnectedDeviceName")

            }
            OpenScreenFragment.MESSAGE_TOAST -> getView()?.showToast(msg.data.getString(OpenScreenFragment.TOAST))
        }
    }

    private fun addToRealm(readMessage: String) {
        basicCRUD(realm, readMessage)
    }

    private fun basicCRUD(realm: Realm, readMessage: String) {
        val results = realm.where<BdModel>().findAll()
        realm.executeTransaction({
            val bdModel = it.createObject<BdModel>(results.size)
            bdModel.valueX = Calendar.getInstance().time
            val int = readMessage.toIntOrNull() ?: 0
            bdModel.valueY = int
        })
    }

    fun onDestroy() {
        realm.close()
    }
}