package com.example.didaktikapp.activities

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import com.example.didaktikapp.Model.CustomLine
import com.example.didaktikapp.R
import java.lang.Exception
import android.os.VibrationEffect

import android.os.Build

import androidx.core.content.ContextCompat.getSystemService

import android.os.Vibrator
import android.os.VibratorManager
import androidx.core.content.ContextCompat


class Utils {

    companion object {
        fun drawLine(globalView: View, context: Context, startX: Float, startY: Float, endX: Float, endY: Float, width: Float, a: Int, r: Int, g: Int, b: Int) {
            val myLinObjecte = CustomLine(context,startX,startY,endX,endY,width, a, r,g,b)
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

        fun comprobarPermisosMap(pContext: Context): Boolean {
            if (ActivityCompat.checkSelfPermission(pContext, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                return true
            }
            return false
        }

        fun vibrarTelefono(pContext: Context) {
            val vibrator = pContext?.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            if (Build.VERSION.SDK_INT >= 26) {
                vibrator.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                vibrator.vibrate(200)
            }
        }
    }
}
