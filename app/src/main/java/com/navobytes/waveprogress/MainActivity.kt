package com.navobytes.waveprogress

import android.os.Bundle
import android.widget.SeekBar
import androidx.activity.ComponentActivity
import androidx.core.content.ContextCompat
import com.navobytes.ui.WaveProgress

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val waveProgress = findViewById<WaveProgress>(R.id.waveProgress)
        waveProgress.setWaveColors(
            ContextCompat.getColor(this, R.color.wave_orange),
            ContextCompat.getColor(this, R.color.wave_purple),
            ContextCompat.getColor(this, R.color.wave_blue)
        )
        val seekBar = findViewById<SeekBar>(R.id.seekBar)

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                waveProgress.setProgress(progress / 100f)
            }
            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })
    }
}