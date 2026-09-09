package com.example.strangerthings

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import kotlin.math.sin

class FairyLightsView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : View(context, attrs) {

    data class Light(val pos: Float, val color: Int, val phase: Float)

    private val lights = listOf(
        Light(0.03f, Color.RED, 0.0f),
        Light(0.09f, Color.YELLOW, 0.6f),
        Light(0.15f, Color.GREEN, 1.2f),
        Light(0.21f, Color.BLUE, 1.8f),
        Light(0.27f, Color.WHITE, 2.4f),
        Light(0.33f, Color.RED, 3.0f),
        Light(0.39f, Color.YELLOW, 0.3f),
        Light(0.45f, Color.GREEN, 0.9f),
        Light(0.51f, Color.BLUE, 1.5f),
        Light(0.57f, Color.WHITE, 2.1f),
        Light(0.63f, Color.RED, 2.7f),
        Light(0.69f, Color.YELLOW, 3.3f),
        Light(0.75f, Color.GREEN, 0.4f),
        Light(0.81f, Color.BLUE, 1.0f),
        Light(0.87f, Color.WHITE, 1.6f),
        Light(0.93f, Color.RED, 2.2f)
    )

    private val wirePaint = Paint().apply {
        color = Color.parseColor("#333333")
        strokeWidth = 2.5f
        style = Paint.Style.STROKE
    }
    private val cordPaint = Paint().apply {
        color = Color.parseColor("#222222")
        strokeWidth = 1.5f
        style = Paint.Style.STROKE
    }
    private val glowPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
    }
    private val bulbPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
    }

    private var animTime = 0f

    private val animator = ValueAnimator.ofFloat(
        0f, (Math.PI * 2).toFloat()
    ).apply {
        duration = 2500
        repeatCount = ValueAnimator.INFINITE
        addUpdateListener {
            animTime = it.animatedValue as Float
            invalidate()
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        animator.start()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        animator.cancel()
    }

    override fun onDraw(canvas: Canvas) {
        val w = width.toFloat()
        val wireY = 12f
        canvas.drawLine(0f, wireY, w, wireY, wirePaint)

        lights.forEach { light ->
            val x = light.pos * w
            val brightness = ((sin(
                (animTime + light.phase).toDouble()) + 1.0) / 2.0
                    ).toFloat()
            val alpha = 0.25f + brightness * 0.75f

            canvas.drawLine(x, wireY, x, wireY + 16f, cordPaint)

            glowPaint.color = light.color
            glowPaint.alpha = (alpha * 55).toInt()
            canvas.drawCircle(x, wireY + 26f, 14f, glowPaint)

            bulbPaint.color = light.color
            bulbPaint.alpha = (alpha * 255).toInt()
            canvas.drawCircle(x, wireY + 26f, 7f, bulbPaint)

            bulbPaint.color = Color.WHITE
            bulbPaint.alpha = (alpha * 160).toInt()
            canvas.drawCircle(x, wireY + 24f, 3f, bulbPaint)
        }
    }
}