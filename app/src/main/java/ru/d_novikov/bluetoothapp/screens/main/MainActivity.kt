package ru.d_novikov.bluetoothapp.screens.main

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import butterknife.ButterKnife
import kotlinx.android.synthetic.main.activity_main.*
import ru.d_novikov.bluetoothapp.R


class MainActivity : AppCompatActivity(), MainView, TabLayout.OnTabSelectedListener {

    private val mainPresenter: MainPresenter = MainPresenter()

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
        container.adapter = MainPagerAdapter(supportFragmentManager)
        tab_layout.setupWithViewPager(container)
        tab_layout.addOnTabSelectedListener(this)
    }

    override fun onTabReselected(tab: TabLayout.Tab?) {

    }

    override fun onTabUnselected(tab: TabLayout.Tab?) {

    }

    override fun onTabSelected(tab: TabLayout.Tab?) {
        val tabFromLayout = tab ?: return
        container.setCurrentItem(tabFromLayout.position, false)
    }
}
