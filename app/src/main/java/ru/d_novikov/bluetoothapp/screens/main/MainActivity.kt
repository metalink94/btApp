package ru.d_novikov.bluetoothapp.screens.main

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import butterknife.ButterKnife
import kotlinx.android.synthetic.main.activity_main.*
import ru.d_novikov.bluetoothapp.R


class MainActivity : AppCompatActivity(), MainView, TabLayout.OnTabSelectedListener {

    private val mainPresenter: MainPresenter = MainPresenter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ButterKnife.bind(this)
        mainPresenter.setView(this)
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, Array<String>(1) { Manifest.permission.ACCESS_FINE_LOCATION }, 1)
        } else {
            mainPresenter.onCreate()
        }
    }

    override fun showAlertDialog() {
        AlertDialog.Builder(this)
                .setTitle("Ошибка")
                .setMessage("Ваше устройство не поддерживает Bluetooth")
                .setPositiveButton("Выход", { dialog, which -> System.exit(0) })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show()

    }

    override fun initPager(bluetoothAdapter: BluetoothAdapter?) {
        container.adapter = MainPagerAdapter(supportFragmentManager, bluetoothAdapter)
        tab_layout.setupWithViewPager(container)
        tab_layout.addOnTabSelectedListener(this)
    }

    override fun onTabReselected(tab: TabLayout.Tab?) {

    }

    override fun onTabUnselected(tab: TabLayout.Tab?) {

    }

    override fun onTabSelected(tab: TabLayout.Tab?) {
        val tabFromLayout = tab ?: return
        container.setCurrentItem(tabFromLayout.position, false)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1) {
            if (grantResults.size == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mainPresenter.onCreate()
            }
        }
    }
}
