package ru.d_novikov.bluetoothapp.screens.openScreen

import android.annotation.SuppressLint
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
import butterknife.BindView
import butterknife.OnClick
import ru.d_novikov.bluetoothapp.R
import ru.d_novikov.bluetoothapp.connecting.BluetoothChatService


class OpenScreenFragment : Fragment(), OpenScreenView {

    var openScreenPresenter = OpenScreenPresenter()
    var bluetoothChatService: BluetoothChatService? = null


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
            when (msg.what) {
                MESSAGE_WRITE -> {
                    val writeBuf = msg.obj as ByteArray
                    // construct a string from the buffer
                    val writeMessage = String(writeBuf)
                    Log.d("javaClass", "Write Message = " + writeMessage)
                }
                MESSAGE_READ -> {
                    val readBuf = msg.obj as ByteArray
                    // construct a string from the valid bytes in the buffer
                    var readMessage = String(readBuf, 0, msg.arg1)
                    readMessage = readMessage.replace("[^\\d]", "")
                    if (readMessage.isNotEmpty()) {
                        Log.d("javaClass", "Read Message = " + readMessage + " messageLen = " + readMessage.length)
                    }
                }
                MESSAGE_DEVICE_NAME -> {
                    // save the connected device's name
                    val mConnectedDeviceName = msg.getData().getString(DEVICE_NAME)
                    Toast.makeText(activity, "Connected to $mConnectedDeviceName", Toast.LENGTH_SHORT).show()
                }
                MESSAGE_TOAST -> Toast.makeText(activity, msg.data.getString(TOAST),
                        Toast.LENGTH_SHORT).show()
            }
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
        bluetoothChatService?.stop()
        bluetoothChatService = null
    }
}