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

        fun setAdmin(value: Boolean) {
            this.isAdmin = value
        }

        fun setUser(pUser: User) {
            this.usuario = pUser
        }

        fun setLastUserId(pNumber: Int) {
            this.lastUserid = pNumber
        }

    }

    fun requestDbUserCount(pUserRegister: String?, callback: queryResponseDone) {
        getDbInstance().collection("Usuarios").get()
        .addOnSuccessListener {
            lastUserid = it.size()
            callback.responseDbUserCount(pUserRegister,it.size())
        }
    }

    fun requestDbUserLogin(pUsername: String, callback: queryResponseDone) {
        getDbInstance().collection("Usuarios").whereEqualTo("nombre", pUsername).get()
        .addOnSuccessListener {
            if (it.size() > 0) {
                var vId:Int = it.documents[0]["id"].toString().replace("u","").toInt()
                var vNombre:String = it.documents[0]["nombre"].toString()
                //var vAdmin:Int = it.documents[0]["admin"].toString().toInt()
                var vTutoFinalizado:Int = it.documents[0]["tutorialFinalizado"].toString().toInt()
                var vPuntuacion:Int = it.documents[0]["puntuacion"].toString().toInt()
                var vUltimoPto:Int = it.documents[0]["ultimo_punto"].toString().toInt()
                setUser(User(vId, vNombre, vTutoFinalizado, vPuntuacion, vUltimoPto))
                callback.responseDbUserLogin(null)
            } else {
                callback.responseDbUserLogin(pUsername)
            }

        }
    }

    fun getHashObjectTest() {

    }

    /*
    fun requestDbUserUpdate(callback: queryResponseDone) {
        if (usuario == null) {
            return
        }
        getDbInstance().collection("Usuarios").whereEqualTo("nombre", usuario!!.nombre).get()
            .addOnSuccessListener {
                if (it.size() > 0) {

                } else {

                }


                getDbInstance().collection("Usuarios").document(newQuickId.toString())
                    .set(defaultData, )
                    .addOnSuccessListener {

                    }
            }
    }

     */

    fun requestDbUserRegister(pUsername: String, callback: queryResponseDone) {
        val newQuickId: Int = lastUserid + 1
        val defaultData = hashMapOf(
            //"admin" to 0,
            "id" to "u"+(newQuickId),
            "nombre" to pUsername,
            "tutorialFinalizado" to 0,
            "puntuacion" to 0,
            "ultimo_punto" to 0)
        getDbInstance().collection("Usuarios").document(newQuickId.toString())
            .set(defaultData, )
            .addOnSuccessListener {
                setUser(User(newQuickId, pUsername,0,  0, 0))
                setLastUserId(newQuickId)
                callback.responseDbUserRegister(true)
            }
            .addOnFailureListener {
                callback.responseDbUserRegister(false)
            }
    }


    interface queryResponseDone {
        fun responseDbUserLogin(accountRegister: String?)

        fun responseDbUserCount(accountRegister: String?, response: Int)

        fun responseDbUserRegister(response: Boolean)
    }
}