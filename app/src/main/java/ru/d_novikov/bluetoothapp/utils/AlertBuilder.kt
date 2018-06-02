package ru.d_novikov.bluetoothapp.utils

import ru.d_novikov.bluetoothapp.R
import ru.d_novikov.bluetoothapp.models.AlertModel

object AlertBuilder {

    fun getStressAlert(): AlertModel {
        return AlertModel(R.drawable.ic_lightning,
                R.string.have_stress,
                R.string.stress_description,
                R.drawable.alert_button_bg)
    }

    fun getSleepingAlert(): AlertModel {
        return return AlertModel(R.drawable.ic_alarm,
                R.string.have_sleeping,
                R.string.sleeping_description,
                R.drawable.alert_button_bg_2)
    }
}