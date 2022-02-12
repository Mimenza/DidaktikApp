package com.example.didaktikapp.Model

import android.widget.ImageView

/**
 * Objeto para gestionar facilmente los juegos que requieran drag and drop.
 *
 * @property corcho imagen del corcho del minijuego 6
 * @property nivel  nivel
 * @constructor
 *
 * @param origen (por ejemplo, el vaso)
 * @param objetivo (por ejemplo, la botella)
 * @param acertado (si el vaso se mueve sobre la botella)
 */
class DragnDropImageLevel(
    origen: ImageView,
    objetivo: ImageView,
    var corcho: ImageView,
    acertado: Boolean = false,
    var nivel: Int = 5,

): DragnDropImage(origen, objetivo, acertado)
