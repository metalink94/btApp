package ru.d_novikov.bluetoothapp.screens.main

import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import butterknife.BindView
import butterknife.ButterKnife
import ru.d_novikov.bluetoothapp.R


class MainActivity : AppCompatActivity(), MainView {
    override fun showAlertDialog() {

        
    }

    override fun initPager(bluetoothAdapter: BluetoothAdapter) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private val mainPresenter: MainPresenter = MainPresenter()

    @BindView(R.id.container)
    lateinit var pager: ViewPager

    @BindView(R.id.tab_layout)
    lateinit var tabLayout: TabLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ButterKnife.bind(this)
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
