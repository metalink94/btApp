package ru.d_novikov.bluetoothapp.screens

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import ru.d_novikov.bluetoothapp.R
import ru.d_novikov.bluetoothapp.screens.main.MainPresenter
import ru.d_novikov.bluetoothapp.screens.main.MainView
import android.widget.Toast
import android.bluetooth.BluetoothAdapter
import android.content.Intent



class MainActivity : AppCompatActivity(), MainView {

    private val mainPresenter: MainPresenter = MainPresenter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mainPresenter.setView(this)
        mainPresenter.onCreate()
    }

    override fun onBluetooth() {
        val turnOn = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
        startActivityForResult(turnOn, 0)
        Toast.makeText(this, "Turned on", Toast.LENGTH_LONG).show()
    }

    override fun searchVisibleDevices() {
        val getVisible = Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE)
        startActivityForResult(getVisible, 0)
    }
}
