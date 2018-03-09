package com.brit.swiftinstaller.ui.activities

import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import com.brit.swiftinstaller.R
import com.brit.swiftinstaller.ui.CircleDrawable
import com.brit.swiftinstaller.utils.InstallerServiceHelper
import com.brit.swiftinstaller.utils.Utils
import com.brit.swiftinstaller.utils.getAccentColor
import com.brit.swiftinstaller.utils.setAccentColor
import kotlinx.android.synthetic.main.activity_customize.*
import kotlinx.android.synthetic.main.customize_toolbar.*

class CustomizeActivity : AppCompatActivity() {

    private var settingsIcons: Array<ImageView?> = arrayOfNulls(3)

    private var mAccent: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mAccent = getAccentColor(this)
        setContentView(R.layout.activity_customize)
        setupAccentSheet()
        updateColor(mAccent)

        customizeConfirmBtn.setOnClickListener {
            setAccentColor(this, mAccent)
            if (Utils.isOverlayInstalled(this, Utils.getOverlayPackageName("android"))) {
                InstallerServiceHelper.install(List(1, { "android" }))
            }
            finish()
        }
    }

    private fun setupAccentSheet() {
        palette.adapter = PaletteAdapter(resources.getIntArray(R.array.accent_colors))
        settingsIcons[0] = connectionsIcon
        settingsIcons[1] = soundIcon
        settingsIcons[2] = notificationsIcon
    }

    fun updateColor(color: Int) {
        mAccent = color
        for (icon: ImageView? in settingsIcons) {
            icon!!.setColorFilter(color)
        }
        hexInput.background.setTint(color)
        applyHex.setTextColor(color)
    }

    inner class PaletteAdapter constructor(private val mColors: IntArray) : BaseAdapter() {

        override fun getCount(): Int {
            return mColors.size
        }

        override fun getItem(position: Int): Any {
            return mColors[position]
        }

        override fun getItemId(position: Int): Long {
            return 0
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            var mainView = convertView
            if (mainView == null) {
                mainView = LayoutInflater.from(parent.context)
                        .inflate(R.layout.palette_view, parent, false)
            }
            val iv = mainView!!.findViewById<ImageView>(R.id.icon)
            iv.background = CircleDrawable(mColors[position])
            mainView.tag = mColors[position]
            mainView.setOnClickListener { updateColor(mColors[position]) }
            return mainView
        }
    }

    fun blackBgClick(view: View) {
        val dialog = View.inflate(this, R.layout.background_alert_dialog, null)
        val builder = AlertDialog.Builder(this, R.style.AppAlertDialogTheme).create()
        builder.setView(dialog)
        builder.show()
    }

    fun cancelBtnClick(view: View) {
        finish()
    }

}