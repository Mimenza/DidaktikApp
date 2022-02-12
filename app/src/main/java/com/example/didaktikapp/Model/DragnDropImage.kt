package com.example.didaktikapp.Model

import android.widget.ImageView

/**
 * Objeto para gestionar facilmente los juegos que requieran drag and drop.
 *
 * @property origen (por ejemplo, la manzana)
 * @property objetivo (por ejemplo, la cesta)
 * @property acertado (por ejemplo, si la manzana se inserta en la cesta)
 */
open class DragnDropImage(
    var origen: ImageView,
    var objetivo: ImageView,
    var acertado: Boolean = false
)
