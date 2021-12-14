package com.example.didaktikapp.Model

import android.animation.AnimatorSet
import android.widget.ImageView

class ManzanaBuenaMala(origen: ImageView, objetivo: ImageView, acertado: Boolean, manzanaBuena: Boolean, animacion: AnimatorSet): DragnDropImage(origen, objetivo, acertado) {
}