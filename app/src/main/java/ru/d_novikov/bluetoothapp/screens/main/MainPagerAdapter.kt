package ru.d_novikov.bluetoothapp.screens.main

import android.bluetooth.BluetoothAdapter
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import ru.d_novikov.bluetoothapp.screens.chart.ChartFragment
import ru.d_novikov.bluetoothapp.screens.map.MapFragment
import ru.d_novikov.bluetoothapp.screens.openScreen.OpenScreenFragment
import ru.d_novikov.bluetoothapp.screens.pulse.PulseFragment
import ru.d_novikov.bluetoothapp.screens.stub.StubFragment

class MainPagerAdapter(val fm: FragmentManager,
                       val bluetoothAdapter: BluetoothAdapter?) : FragmentPagerAdapter(fm) {

    var data: String = ""
    val chartFragment = ChartFragment.getInstance()
    val openScreenFragment = OpenScreenFragment.getInstance(bluetoothAdapter)

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> openScreenFragment
            1 -> chartFragment
            2 -> MapFragment()
            3 -> /*PulseFragment.getInstance()*/ StubFragment.getInstance()
            4 -> StubFragment.getInstance()
            else -> {
                OpenScreenFragment.getInstance(bluetoothAdapter)
            }
        }
    }

    override fun getCount(): Int {
        return 5
    }

    fun updateChart(position: Int) {
        if (position == 1) {
            chartFragment.updateChart()
        }
    }

    fun updateStatus(state: Int) {
        if (openScreenFragment != null) {
            openScreenFragment.setState(state)
        }
    }
}