package com.example.didaktikapp.Model

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.view.View

class CustomLine (context: Context, var startX: Float, var startY: Float, var endX: Float, var endY: Float, var width: Float,var a: Int, var r: Int, var g: Int, var b: Int): View(context) {
    override fun onDraw(canvas: Canvas) {
        val pincel1 = Paint()
        pincel1.setARGB(a, r, g, b)
        pincel1.setStrokeWidth(width)
        canvas.drawLine(startX, startY, endX, endY, pincel1)
    }
}

fun CustomLine.clone(): CustomLine {
    val clCopy = CustomLine (context, startX, startY, endX, endY, width, a, r, g, b)
    return clCopy
}