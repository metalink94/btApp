package ru.d_novikov.bluetoothapp.screens.openScreen

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import butterknife.BindView
import butterknife.OnClick
import ru.d_novikov.bluetoothapp.R


class OpenScreenFragment : Fragment(), OpenScreenView {

    var openScreenPresenter = OpenScreenPresenter()


    @BindView(R.id.button)
    lateinit var button: TextView

    @BindView(R.id.icon_state)
    lateinit var icon: ImageView

    @BindView(R.id.person_state)
    lateinit var personState: TextView

    @BindView(R.id.timer)
    lateinit var timer: TextView

    companion object {

        var bluetoothAdapter: BluetoothAdapter? = null

        @JvmStatic
        fun getInstance(bluetoothAdapter: BluetoothAdapter?): OpenScreenFragment {
            this.bluetoothAdapter = bluetoothAdapter
            return OpenScreenFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_open_screen, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        openScreenPresenter.setView(this)
        openScreenPresenter.onCreate(bluetoothAdapter)
    }

    override fun onBluetooth() {
        val turnOn = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
        startActivityForResult(turnOn, 0)
        Toast.makeText(activity, "Turned on", Toast.LENGTH_LONG).show()
    }

    override fun setButtonStart() {
        button.text = getString(R.string.go)
    }

    override fun setButtonStop() {
        button.text = getString(R.string.stop)
    }

    @OnClick(R.id.button)
    fun onButtonClick(view: View) {
        openScreenPresenter.onButtonClick()
    }

    override fun scanDevices(bluetoothAdapter: BluetoothAdapter) {
        bluetoothAdapter.startDiscovery()
        val filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
        activity?.registerReceiver(bReciever, filter)
    }

    override fun stopScanDevices() {
        activity?.unregisterReceiver(bReciever)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        openScreenPresenter.onActivityResult(requestCode, resultCode, data)
    }

    private val bReciever = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            if (BluetoothDevice.ACTION_FOUND == action) {
                val device = intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
                // Create a new device item
//                val newDevice = DeviceItem(device.name, device.address, false)
                Log.d("BroadcastReceiver", "find device = " + device.name + " macAddress " + device.address + " device Type " + device.type)
            }
        }
    }
}