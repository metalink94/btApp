package ru.d_novikov.bluetoothapp.screens.openScreen

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.d_novikov.bluetoothapp.R

class OpenScreenFragment: Fragment() {

    companion object {

        @JvmStatic
        fun getInstance(): OpenScreenFragment {
            return OpenScreenFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_open_screen, container, false)
        return view
    }
}