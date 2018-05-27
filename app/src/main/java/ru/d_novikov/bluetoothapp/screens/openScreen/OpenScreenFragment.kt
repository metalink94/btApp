package ru.d_novikov.bluetoothapp.screens.openScreen

import android.annotation.SuppressLint
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import ru.d_novikov.bluetoothapp.R
import ru.d_novikov.bluetoothapp.connecting.BluetoothChatService
import ru.d_novikov.bluetoothapp.interfaces.DataSendListener


class OpenScreenFragment : Fragment(), OpenScreenView, View.OnClickListener {
    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.button -> openScreenPresenter.onButtonClick()
        }
    }

    var openScreenPresenter = OpenScreenPresenter()
    var bluetoothChatService: BluetoothChatService? = null

    lateinit var button: TextView

    lateinit var icon: ImageView

    lateinit var personState: TextView

    lateinit var timer: TextView

    lateinit var callback: DataSendListener

    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        if (activity is DataSendListener) {
            callback = activity
        } else {
            Log.e("OpenScreenFragment", "activity must implemet DataSendListener")
        }
    }

    companion object {

        var bluetoothAdapter: BluetoothAdapter? = null

        const val MESSAGE_STATE_CHANGE = 1
        const val MESSAGE_READ = 2
        const val MESSAGE_WRITE = 3
        const val MESSAGE_DEVICE_NAME = 4
        const val MESSAGE_TOAST = 5

        // Key names received from the BluetoothChatService Handler
        const val DEVICE_NAME = "device_name"
        const val TOAST = "toast"

        @JvmStatic
        fun getInstance(bluetoothAdapter: BluetoothAdapter?): OpenScreenFragment {
            this.bluetoothAdapter = bluetoothAdapter
            return OpenScreenFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_open_screen, container, false)
        button = view.findViewById(R.id.button)
        icon = view.findViewById(R.id.icon_state)
        personState = view.findViewById(R.id.person_state)
        timer = view.findViewById(R.id.timer)
        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        openScreenPresenter.setView(this)
        openScreenPresenter.onCreate(bluetoothAdapter)
    }

    override fun onBluetooth() {
        val turnOn = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
        startActivityForResult(turnOn, 0)
    }

    override fun setButtonStart() {
        button.text = getString(R.string.go)
    }

    override fun setButtonStop() {
        button.text = getString(R.string.stop)
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
                Log.d(OpenScreenFragment::javaClass.name,
                        "find device = " + device.name + " macAddress " + device.address)
                openScreenPresenter.onReceive(device)
            }
        }
    }

    override fun connect(device: BluetoothDevice) {
        bluetoothChatService = BluetoothChatService(context, handler)
        if (bluetoothChatService?.state == BluetoothChatService.STATE_NONE) {
            bluetoothChatService?.start()
        }
        bluetoothChatService?.connect(device, false)
    }

    private val handler = @SuppressLint("HandlerLeak")
    object : Handler() {
        override fun handleMessage(msg: Message) {
            openScreenPresenter.onHandleMessage(msg)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        bluetoothChatService?.stop()
        bluetoothChatService = null
    }

    override fun onDetach() {
        super.onDetach()
        bluetoothChatService?.stop()
        bluetoothChatService = null
    }

    override fun onDestroy() {
        super.onDestroy()
        openScreenPresenter.onDestroy()
        bluetoothChatService?.stop()
        bluetoothChatService = null
    }

    override fun showToast(message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
    }

    override fun onDataReceived(readMessage: String) {
        callback.onDataReceived(readMessage)
    }

}