package com.navobytes.ui

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.core.content.withStyledAttributes
import kotlin.math.PI
import kotlin.math.sin

class WaveProgress @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val wavePaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val wavePath = Path()

    // 配置参数
    private var amplitude = 20f      // 波峰高度（建议20-40dp）
    private var waveSpeed = 0.8f     // 流动速度（0.5-1.5）
    private var progress = 0f        // 进度 0-1
    private var phase = 0f           // 相位偏移

    // 固定渐变颜色
    private val colors = intArrayOf(
        Color.parseColor("#FF2196F3"), // 右：蓝色
        Color.parseColor("#FF9C27B0"), // 中：紫色
        Color.parseColor("#FFFF9800")  // 左：橙色
    )
    private var gradient: LinearGradient? = null
    private var viewWidth = 0f
    private var viewHeight = 0f

    init {
        context.withStyledAttributes(attrs, R.styleable.WaveProgress) {
            amplitude = getDimension(R.styleable.WaveProgress_amplitude, 20f)
            waveSpeed = getFloat(R.styleable.WaveProgress_waveSpeed, 0.8f)
        }
        setupPaint()
        startWaveAnimation()
    }

    private fun setupPaint() {
        wavePaint.style = Paint.Style.FILL
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        viewWidth = w.toFloat()
        viewHeight = h.toFloat()
        updateGradient()
    }

    private fun updateGradient() {
        gradient = LinearGradient(
            viewWidth.toFloat(),  // 起点X：右侧边界
            viewHeight * (1 - progress),  // 起点Y：保持原垂直位置
            0f,                   // 终点X：左侧边界
            viewHeight * (1 - progress),  // 终点Y：保持原垂直位置
            colors,
            floatArrayOf(0f, 0.5f, 1f),
            Shader.TileMode.CLAMP
        ).also {
            wavePaint.shader = it
        }
    }

    private fun startWaveAnimation() {
        post(object : Runnable {
            override fun run() {
                phase += waveSpeed * 0.5f // 降低相位变化速度
                if (phase > 2 * PI) phase -= 2 * PI.toFloat()
                invalidate()
                postDelayed(this, 16) // 保持60FPS
            }
        })
    }

    override fun onDraw(canvas: Canvas) {
        val waveTop = viewHeight * (1 - progress)

        wavePath.reset()
        wavePath.moveTo(-viewWidth, viewHeight)

        // 每个视图宽度一个完整波形
        val wavelength = viewWidth

        for (x in -viewWidth.toInt()..(viewWidth * 2).toInt()) {
            val radians = (2 * PI / wavelength) * x + phase
            val y = waveTop - amplitude * sin(radians).toFloat()
            wavePath.lineTo(x.toFloat(), y)
        }

        wavePath.lineTo(viewWidth * 2, viewHeight)
        wavePath.lineTo(-viewWidth, viewHeight)
        wavePath.close()

        canvas.drawPath(wavePath, wavePaint)
    }

    fun setProgress(value: Float) {
        progress = value.coerceIn(0f, 1f)
        updateGradient()
        invalidate()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        removeCallbacks(null)
    }
}