package com.example.didaktikapp.Model

import android.widget.ImageView

//Objeto para gestionar facilmente los juegos que requieran drag and drop.
data class DragnDropImage(
    var origen: ImageView,
    var objetivo: ImageView,
    var acertado: Boolean = false
)
