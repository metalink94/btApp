package ru.d_novikov.bluetoothapp.screens.alert

import android.media.RingtoneManager
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import io.realm.internal.SyncObjectServerFacade.getApplicationContext
import ru.d_novikov.bluetoothapp.R
import ru.d_novikov.bluetoothapp.interfaces.AlertFragmentCallback
import ru.d_novikov.bluetoothapp.models.AlertModel


class AlertFragment : Fragment() {

    lateinit var title: TextView
    lateinit var description: TextView
    lateinit var button: TextView
    lateinit var icon: ImageView

    private var callback: AlertFragmentCallback? = null


    companion object {
        private val KEY_ICON = "key_icon"
        private val KEY_TITLE = "key_title"
        private val KEY_DESCRIPTION = "key_description"
        private val KEY_COLOR = "key_color"

        fun getInstance(alertModel: AlertModel): AlertFragment {
            val alertFragment = AlertFragment()
            val b = Bundle()
            b.putInt(KEY_ICON, alertModel.icon)
            b.putInt(KEY_TITLE, alertModel.title)
            b.putInt(KEY_DESCRIPTION, alertModel.description)
            b.putInt(KEY_COLOR, alertModel.color)
            alertFragment.arguments = b
            return alertFragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_alert, container, false)
        initViews(view)
        if (arguments != null) {
            setViews(arguments!!)
        }
        setVibration()
        setSound()
        return view
    }

    private fun setViews(arguments: Bundle) {
        title.text = getString(arguments.getInt(KEY_TITLE))
        description.text = getString(arguments.getInt(KEY_DESCRIPTION))
        icon.setImageResource(arguments.getInt(KEY_ICON, R.drawable.ic_lightning))
        button.setBackgroundResource(arguments.getInt(KEY_COLOR, R.drawable.alert_button_bg))
    }

    private fun initViews(view: View) {
        title = view.findViewById(R.id.title_alert)
        description = view.findViewById(R.id.description_alert)
        button = view.findViewById(R.id.accept_button)
        icon = view.findViewById(R.id.icon_alert)

        button.setOnClickListener { callback?.onAcceptClick() }
    }

    private fun setVibration() {
        val v = activity?.getSystemService(AppCompatActivity.VIBRATOR_SERVICE) as Vibrator
        // Vibrate for 500 milliseconds
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            v.vibrate(500)
        }
    }

    private fun setSound() {
        val notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val r = RingtoneManager.getRingtone(getApplicationContext(), notification)
        r.play()
    }

    fun setCallback(callback: AlertFragmentCallback) {
        this.callback = callback
    }
}