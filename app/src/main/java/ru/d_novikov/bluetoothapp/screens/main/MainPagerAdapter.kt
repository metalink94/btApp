package ru.d_novikov.bluetoothapp.screens.main

import android.bluetooth.BluetoothAdapter
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import ru.d_novikov.bluetoothapp.screens.openScreen.OpenScreenFragment

class MainPagerAdapter(val fm: FragmentManager): FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return when(position) {
            0 -> OpenScreenFragment.getInstance()
            1 -> OpenScreenFragment.getInstance()
            2 -> OpenScreenFragment.getInstance()
            else -> {
                OpenScreenFragment.getInstance()
            }
        }
    }

    override fun getCount(): Int {
        return 3
    }
}