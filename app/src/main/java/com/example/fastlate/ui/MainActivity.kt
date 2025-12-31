package com.example.fastlate.ui

import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.chaquo.python.Python
import com.chaquo.python.android.AndroidPlatform
import com.example.fastlate.R
import android.animation.ValueAnimator
import androidx.core.content.ContextCompat
import androidx.annotation.ColorRes
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatDelegate



class MainActivity : AppCompatActivity() {

    private lateinit var txtStatus: TextView
    private lateinit var txtLatency: TextView
    private lateinit var txtDeviceName: TextView
    private lateinit var txtDeviceAddress: TextView
    private lateinit var txtConnectionType: TextView
    private lateinit var deviceInfoContainer: LinearLayout
    private lateinit var voiceAnimContainer: LinearLayout
    private lateinit var bar1: View
    private lateinit var bar2: View
    private lateinit var bar3: View
    private lateinit var btnPair: Button
    private lateinit var btnListen: Button

    private lateinit var btnThemeToggle: ImageButton


    private var isListening = false

    override fun onCreate(savedInstanceState: Bundle?) {

        // 1️⃣ Read saved theme BEFORE view inflation
        val prefs = getSharedPreferences("settings", MODE_PRIVATE)
        val isDark = prefs.getBoolean("dark_mode", true)

        AppCompatDelegate.setDefaultNightMode(
            if (isDark)
                AppCompatDelegate.MODE_NIGHT_YES
            else
                AppCompatDelegate.MODE_NIGHT_NO
        )

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 2️⃣ Bind views
        val btnThemeToggle = findViewById<ImageButton>(R.id.btnThemeToggle)

        // 3️⃣ Set correct icon after recreation
        btnThemeToggle.setImageResource(
            if (isDark) R.drawable.ic_sun else R.drawable.ic_moon
        )

        // 4️⃣ Toggle theme on click
        btnThemeToggle.setOnClickListener {
            val newMode = !prefs.getBoolean("dark_mode", true)

            prefs.edit().putBoolean("dark_mode", newMode).apply()

            AppCompatDelegate.setDefaultNightMode(
                if (newMode)
                    AppCompatDelegate.MODE_NIGHT_YES
                else
                    AppCompatDelegate.MODE_NIGHT_NO
            )
        }


        // ---- rest of your existing setup code below ----
        // buttons, pairing logic, animations, etc.
    }

    private fun applyTheme(isDark: Boolean) {
        if (isDark) {
            AppCompatDelegate.setDefaultNightMode(
                AppCompatDelegate.MODE_NIGHT_YES
            )
            btnThemeToggle.setImageResource(R.drawable.ic_sun)
        } else {
            AppCompatDelegate.setDefaultNightMode(
                AppCompatDelegate.MODE_NIGHT_NO
            )
            btnThemeToggle.setImageResource(R.drawable.ic_moon)
        }
    }

    private fun startPairing() {
        txtStatus.text = "Pairing..."
        animateStatusBarColor(R.color.status_pairing)
        val py = Python.getInstance()
        val backend = py.getModule("backend")

        val result =
            backend.callAttr("start_pairing").asMap() as Map<String, Any?>


        txtStatus.text = result["status"].toString()
        txtDeviceName.text = "Name: ${result["device_name"]}"
        txtDeviceAddress.text = "MAC: ${result["mac"]}"
        txtConnectionType.text = "Connection: ${result["connection"]}"

        deviceInfoContainer.visibility = View.VISIBLE
        btnListen.isEnabled = true
        btnListen.alpha = 1f
        animateStatusBarColor(R.color.status_paired)

    }
    private fun animateStatusBarColor(@ColorRes targetColorRes: Int) {
        val window = window
        val startColor = window.statusBarColor
        val endColor = ContextCompat.getColor(this, targetColorRes)

        val animator = ValueAnimator.ofArgb(startColor, endColor)
        animator.duration = 400 // smooth but fast
        animator.addUpdateListener {
            window.statusBarColor = it.animatedValue as Int
        }
        animator.start()
    }

    private fun toggleListening() {
        val py = Python.getInstance()
        val backend = py.getModule("backend")

        isListening = !isListening

        if (isListening) {
            val result =
                backend.callAttr("start_listening").asMap() as Map<String, Any?>

            txtStatus.text = result["status"].toString()
            txtLatency.text = "Latency: ${result["latency"]} ms"
            startVoiceAnimation()
        } else {
            txtStatus.text = "Paired"
            txtLatency.text = "Latency: -- ms"
            stopVoiceAnimation()
            animateStatusBarColor(R.color.status_paired)

        }
    }


    private fun startVoiceAnimation() {
        voiceAnimContainer.visibility = View.VISIBLE
        val anim = AnimationUtils.loadAnimation(this, R.anim.voice_pulse)
        bar1.startAnimation(anim)
        bar2.startAnimation(anim)
        bar3.startAnimation(anim)
    }

    private fun stopVoiceAnimation() {
        voiceAnimContainer.visibility = View.GONE
        bar1.clearAnimation()
        bar2.clearAnimation()
        bar3.clearAnimation()
    }
}
