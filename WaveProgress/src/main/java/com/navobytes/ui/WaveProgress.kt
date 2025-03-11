package com.navobytes.ui

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
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
    private var waveSpeed = 0.1f     // 流动速度（0.1-0.5）
    private var progress = 0f        // 进度 0-1
    private var phase = 0f           // 相位偏移

    // 新增颜色数组属性
    private var colors = intArrayOf(
        Color.BLUE, Color.MAGENTA, Color.YELLOW
    )

    // 或使用可变参数版本
    fun setWaveColors(vararg colors: Int) {
        require(colors.size == 3) { "必须提供3个颜色值" }
        this.colors = colors
        updateGradient()
        invalidate()
    }
    private var gradient: LinearGradient? = null
    private var viewWidth = 0f
    private var viewHeight = 0f

    init {
        context.withStyledAttributes(attrs, R.styleable.WaveProgress) {
            amplitude = getDimension(R.styleable.WaveProgress_amplitude, 20f)
            waveSpeed = getFloat(R.styleable.WaveProgress_waveSpeed, 0.1f)
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
            0f,                          // 起点X：左上角
            viewHeight * (1 - progress), // 起点Y：基于进度的高度
            viewWidth,                   // 终点X：右下角
            viewHeight,                  // 终点Y：底部
            colors,
            floatArrayOf(0f, 0.5f, 1f), // 对应颜色分布位置
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