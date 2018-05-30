package ru.d_novikov.bluetoothapp.screens.stub

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.d_novikov.bluetoothapp.R

class StubFragment: Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_stub, container, false)
        return view
    }

    companion object {

        fun getInstance(): StubFragment {
            return StubFragment()
        }
    }
}