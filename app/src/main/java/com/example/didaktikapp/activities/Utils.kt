package com.example.didaktikapp.activities

import android.app.Activity
import android.content.Context
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.didaktikapp.Model.CustomLine
import com.example.didaktikapp.R
import java.lang.Exception

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

        fun setUserPreferences(activity: Activity, prefName: String, dataName: String, dataValue: String): Boolean {
            try {
                var sharedPref = activity.getSharedPreferences(prefName, Context.MODE_PRIVATE)
                var editor = sharedPref.edit()
                editor.putString(dataName, dataValue)
                editor.apply()
                return true;
            } catch (e: Exception) {
                //Pass this catch
            }
            return false
        }

        fun getUserPreferences(activity: Activity, prefName: String, dataName: String): String? {
            try {
                var sharedPref = activity.getSharedPreferences(prefName, Context.MODE_PRIVATE)
                return sharedPref.getString(dataName, null)
            }catch (e: Exception) {
                //Pass this catch
            }
            return null
        }
    }
}