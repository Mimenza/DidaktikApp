package com.example.didaktikapp.activities

import android.widget.Toast
import com.example.reto01.Model.User
import com.google.firebase.firestore.FirebaseFirestore

class DbHandler {

    companion object {
        private var lastUserid: Int = 0
        private var usuario: User? = null
        private var dbInstance: FirebaseFirestore? = null
        private var isAdmin: Boolean = true


        fun getDbInstance(): FirebaseFirestore {
            if (dbInstance == null) {
                dbInstance = FirebaseFirestore.getInstance()
            }
            return dbInstance!!
        }

        fun getLastUserId() = this.lastUserid

        fun getUser() = this.usuario

        fun getAdmin() = this.isAdmin

        fun setAdmin() {
            this.isAdmin = true
        }

        fun setUser(pUser: User) {
            this.usuario = pUser
        }

        fun setLastUserId(pNumber: Int) {
            this.lastUserid = pNumber
        }

    }

    fun requestDbUserCount(pUserRegister: String?, callback: queryResponseDone) {
        println("******* INITIAL COUNT FUNCION: " + pUserRegister)
        getDbInstance().collection("Usuarios").get()
        .addOnSuccessListener {
            println("****************** USER COUNT: " + it.size())
            callback.responseDbUserCount(pUserRegister,it.size())
        }
    }

    fun requestDbUserLogin(pUsername: String, callback: queryResponseDone) {
        //println("******* INITIAL LOGIN FUNCION: " + pUsername)
        getDbInstance().collection("Usuarios").whereEqualTo("nombre", pUsername).get()
        .addOnSuccessListener {
            if (it.size() > 0) {
                callback.responseDbUserLogin(null)
            } else {
                callback.responseDbUserLogin(pUsername)
            }

        }
    }

    fun requestDbUserRegister(pUsername: String, callback: queryResponseDone) {
        //println("******* INITIAL REGISTER FUNCION: " + pUsername)
        val newQuickId = lastUserid + 1
        val defaultData = hashMapOf(
            "admin" to 0,
            "id" to "u"+(newQuickId),
            "nombre" to pUsername,
            "puntuacion" to 0,
            "ultimo_punto" to 0)
        val querydb = getDbInstance().collection("Usuarios").document((newQuickId.toString())).set(defaultData)
            //querydb.get()
            .addOnSuccessListener {
                //querydb.document(newQuickId.toString()).set(defaultData)
                setUser(User(newQuickId, pUsername, 0, 0, 0))
                setLastUserId(newQuickId)
                callback.responseDbUserRegister(true)
            }
    }


    interface queryResponseDone {
        fun responseDbUserLogin(accountRegister: String?)

        fun responseDbUserCount(accountRegister: String?, response: Int)

        fun responseDbUserRegister(response: Boolean)
    }
}