package ru.d_novikov.bluetoothapp.screens.main

import android.bluetooth.BluetoothAdapter
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import ru.d_novikov.bluetoothapp.screens.chart.ChartFragment
import ru.d_novikov.bluetoothapp.screens.openScreen.OpenScreenFragment

class MainPagerAdapter(val fm: FragmentManager,
                       val bluetoothAdapter: BluetoothAdapter?) : FragmentPagerAdapter(fm) {

    var data: String = ""
    val chartFragment = ChartFragment.getInstance()

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> OpenScreenFragment.getInstance(bluetoothAdapter)
            1 -> chartFragment
            2 -> ChartFragment.getInstance()
            else -> {
                OpenScreenFragment.getInstance(bluetoothAdapter)
            }
        }
    }

    override fun getCount(): Int {
        return 3
    }

    fun updateChart(position: Int) {
        if (position == 1) {
            chartFragment.updateChart()
        }
    }
}