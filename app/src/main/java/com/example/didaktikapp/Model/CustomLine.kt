package com.example.didaktikapp.Model

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.view.View

/**
 * Clase que dibuja una línea según las coordenadas
 *
 * @property startX posición del comienzo de la coordenada x de la línea que se dibuja
 * @property startY posición del comienzo de la coordenada de y la línea que se dibuja
 * @property endX   posición del fin de la coordenada x de la línea que se dibuja
 * @property endY   posición del fin de la coordenada y de la línea que se dibuja
 * @property width  anchura de la línea que se dibuja
 * @property a      color de la línea (a,r,g,b)
 * @property r      color de la línea (a,r,g,b)
 * @property g      color de la línea (a,r,g,b)
 * @property b      color de la línea (a,r,g,b)
 * @constructor
 *
 * @param context de la actividad o fragment en la que se usa la clase, las coordenadas,
 * el color, y la anchura
 *
 */
class CustomLine (context: Context, var startX: Float, var startY: Float, var endX: Float, var endY: Float, var width: Float,var a: Int, var r: Int, var g: Int, var b: Int): View(context) {
    override fun onDraw(canvas: Canvas) {
        val pincel1 = Paint() //variable que hará pintar la línea creada
        pincel1.setARGB(a, r, g, b)
        pincel1.setStrokeWidth(width)
        canvas.drawLine(startX, startY, endX, endY, pincel1)
    }
}

//funcion que clona la linea dibujada
fun CustomLine.clone(): CustomLine {
    val clCopy = CustomLine (context, startX, startY, endX, endY, width, a, r, g, b)
    //devuelve la linea dibujada que hemos clonado
    return clCopy
}