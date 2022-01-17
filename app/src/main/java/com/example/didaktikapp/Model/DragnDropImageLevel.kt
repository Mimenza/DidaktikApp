package com.example.didaktikapp.Model

import android.widget.ImageView

//Objeto para gestionar facilmente los juegos que requieran drag and drop.
class DragnDropImageLevel(
    origen: ImageView,
    objetivo: ImageView,
    var corcho: ImageView,
    acertado: Boolean = false,
    var nivel: Int = 5,

): DragnDropImage(origen, objetivo, acertado)
