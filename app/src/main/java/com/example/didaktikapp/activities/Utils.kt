package com.example.didaktikapp.activities

import android.content.Context
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.didaktikapp.Model.CustomLine
import com.example.didaktikapp.R

class Utils {
    companion object {

        private var explicacionMapaFinalizada: Boolean = false

        fun setExplicacionFinalizada() {
            this.explicacionMapaFinalizada = true
        }

        fun getExplicacionFinalizada() = this.explicacionMapaFinalizada

        fun drawLine(globalView: View, context: Context, startX: Float, startY: Float, endX: Float, endY: Float, width: Float, r: Int, g: Int, b: Int) {
            val myLinObjecte = CustomLine(context,startX,startY,endX,endY,width, r,g,b)
            val constraintLayoutFound = globalView.findViewById<ConstraintLayout>(R.id.mainlayout)
            constraintLayoutFound.addView(myLinObjecte)
        }
    }
}