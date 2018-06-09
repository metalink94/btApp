package ru.d_novikov.bluetoothapp.screens.main

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import ru.d_novikov.bluetoothapp.BuildConfig
import ru.d_novikov.bluetoothapp.R
import ru.d_novikov.bluetoothapp.interfaces.DataSendListener
import ru.d_novikov.bluetoothapp.models.AlertModel
import ru.d_novikov.bluetoothapp.models.SaveModel
import ru.d_novikov.bluetoothapp.screens.alert.AlertFragment
import ru.d_novikov.bluetoothapp.interfaces.AlertFragmentCallback
import java.io.File
import java.io.FileWriter
import java.io.IOException


class MainActivity : AppCompatActivity(), MainView, TabLayout.OnTabSelectedListener, DataSendListener, AlertFragmentCallback {

    private val mainPresenter: MainPresenter = MainPresenter()

    private var mainPagerAdapter: MainPagerAdapter? = null

    val frManager = supportFragmentManager

    var alertFragment = AlertFragment()

    companion object {
        var isShown: Boolean = true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mainPresenter.setView(this)
        isShown = true
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, Array(1) { Manifest.permission.ACCESS_FINE_LOCATION }, 1)
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
        mainPagerAdapter = MainPagerAdapter(supportFragmentManager, bluetoothAdapter)
        container.adapter = mainPagerAdapter
        container.offscreenPageLimit = 3
        tab_layout.setupWithViewPager(container)
        tab_layout.addOnTabSelectedListener(this)
        setIcons()
    }

    private fun setIcons() {
        tab_layout.getTabAt(0)?.icon = ContextCompat.getDrawable(this, R.drawable.main_icon)
        tab_layout.getTabAt(1)?.icon = ContextCompat.getDrawable(this, R.drawable.increasing_chart)
//        tab_layout.getTabAt(2)?.icon = ContextCompat.getDrawable(this, R.drawable.ic_menu)
        tab_layout.getTabAt(2)?.icon = ContextCompat.getDrawable(this, R.drawable.pulse_line)
//        tab_layout.getTabAt(4)?.icon = ContextCompat.getDrawable(this, R.drawable.ic_categories)
    }

    override fun onTabReselected(tab: TabLayout.Tab?) {

    }

    override fun onTabUnselected(tab: TabLayout.Tab?) {

    }

    override fun onTabSelected(tab: TabLayout.Tab?) {
        val tabFromLayout = tab ?: return
        container.setCurrentItem(tabFromLayout.position, false)
        mainPagerAdapter?.updateChart(tabFromLayout.position)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1) {
            if (grantResults.size == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mainPresenter.onCreate()
            }
        }
    }

    override fun onDataReceived(value: Int, state: Int) {
        mainPresenter.onDataReceived(value, state)
    }

    override fun saveToFile(time: String, dataList: MutableList<SaveModel>) {
        try {
            val title = time.replace(":", "_").replace(".", "_")
            val myFile = File(this.getExternalFilesDir(null), "$title.txt")
            if (!myFile.exists())
                myFile.createNewFile()

            val writer = FileWriter(myFile)
            for (str in dataList) {
                writer.write(str.time + ": " + str.data + "\n")
            }
            writer.close()
        } catch (e: IOException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        }
    }

    override fun onResume() {
        super.onResume()
        isShown = true
    }

    override fun onPause() {
        super.onPause()
        isShown = false
        mainPresenter.onDestroy()
    }

    override fun onDestroy() {
        super.onDestroy()
        isShown = false
        mainPresenter.onDestroy()
    }

    override fun hideAlert() {
        if (!isShown) return
        frManager.beginTransaction()
                .remove(alertFragment)
                .commit()
    }

    override fun showAlert(model: AlertModel) {
        if (!isShown) return
        alertFragment = AlertFragment.getInstance(model)
        alertFragment.setCallback(this)
        frManager.beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                .add(R.id.fragment_container, alertFragment)
                .commit()
    }

    override fun onAcceptClick() {
        mainPresenter.onAlertClick()
    }

    override fun setStatus(state: Int) {
        mainPagerAdapter?.updateStatus(state)
    }

}
