package ru.d_novikov.bluetoothapp.screens.main

import android.bluetooth.BluetoothAdapter
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import butterknife.BindView
import butterknife.ButterKnife
import ru.d_novikov.bluetoothapp.R
import android.support.v7.app.AlertDialog




class MainActivity : AppCompatActivity(), MainView, TabLayout.OnTabSelectedListener {

    private val mainPresenter: MainPresenter = MainPresenter()

    @BindView(R.id.container)
    lateinit var pager: ViewPager

    @BindView(R.id.tab_layout)
    lateinit var tabLayout: TabLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ButterKnife.bind(this)
        mainPresenter.setView(this)
        mainPresenter.onCreate()
    }

    override fun showAlertDialog() {
        AlertDialog.Builder(this)
                .setTitle("Ошибка")
                .setMessage("Ваше устройство не поддерживает Bluetooth")
                .setPositiveButton("Выход", { dialog, which -> System.exit(0) })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show()

    }

    override fun initPager() {
        pager.adapter = MainPagerAdapter(supportFragmentManager)
        tabLayout.setupWithViewPager(pager)
        tabLayout.addOnTabSelectedListener(this)
    }

    override fun onTabReselected(tab: TabLayout.Tab?) {

    }

    override fun onTabUnselected(tab: TabLayout.Tab?) {

    }

    override fun onTabSelected(tab: TabLayout.Tab?) {
        val tabFromLayout = tab ?: return
        pager.setCurrentItem(tabFromLayout.position, false)
    }
}
