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

class Utils {
    companion object {

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

        fun comprobarPermisosMap(pContext: Context): Boolean {
            if (ActivityCompat.checkSelfPermission(pContext, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                return true
            }
            return false
        }
    }

}