package ru.d_novikov.bluetoothapp.models

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.util.*

open class BdModel(@PrimaryKey var id: Long = 0,
                   var valueX: Date = Calendar.getInstance().time,
                   var valueY: Int = 0) : RealmObject() {

}
