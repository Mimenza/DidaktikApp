package com.example.didaktikapp.activities

import android.content.Intent
import android.widget.Toast
import com.example.reto01.Model.User
import com.google.firebase.firestore.FirebaseFirestore

class DbHandler {

    companion object {
        private var lastUserid: Int = 0
        private var usuario: User? = null
        private var dbInstance: FirebaseFirestore? = null

        fun getDbInstance(): FirebaseFirestore {
            if (dbInstance == null) {
                dbInstance = FirebaseFirestore.getInstance()
            }
            println("***** Fire base instance should be initialized")
            //if (this.lastUserid == null) {
                //this.lastUserid = getLastUserId()
            //}
            return dbInstance!!
        }

        fun logUser(pUsername: String): Boolean {
            var logSuccess: Boolean = false
            /*
            val querydb = getDbInstance().collection("Usuarios")
            val query = querydb.whereEqualTo("nombre", pUsername.toString())
                .get()
                .addOnSuccessListener { rows ->
                    if (rows.size() > 0) {
                        var vId:Int = rows.documents[0]["id"].toString().replace("u","").toInt()
                        var vNombre:String = rows.documents[0]["nombre"].toString()
                        var vAdmin:Int = rows.documents[0]["admin"].toString().toInt()
                        var vPuntuacion:Int = rows.documents[0]["puntuacion"].toString().toInt()
                        var vUltimoPto:Int = rows.documents[0]["ultimo_punto"].toString().toInt()
                        this.usuario = User(vId, vNombre, vAdmin, vPuntuacion, vUltimoPto)
                        logSuccess = true
                    }

                    onSuccess() {

                    }
                }
                .addOnFailureListener { exception ->
                    val functionName = object{}.javaClass.enclosingMethod.name
                    println("[ERROR] Found at: ${functionName.toString()}")
                }
             */
            return logSuccess
        }

        /*
        suspend fun esperarResultado(): String? = FirebaseFirestore.getInstance()
            .collection("videos").document("listadevideos").get().await().getString("video")


         */
        fun createUser(pUsername: String): Boolean {
            var createSuccess: Boolean = false
            val defaultData = hashMapOf(
                "admin" to 0,
                "id" to "u"+((getLastUserId()+1).toString()),
                "nombre" to pUsername,
                "puntuacion" to 0,
                "ultimo_punto" to 0)

            val querydb = getDbInstance().collection("Usuarios")
            querydb.document("Usuarios").set(defaultData)
            /*
            val query = querydd.whereEqualTo("nombre", pUsername.toString())
                .get()
                .addOnSuccessListener { rows ->
                    if (rows.size() > 0) {
                        var vId:Int = rows.documents[0]["id"].toString().replace("u","").toInt()
                        var vNombre:String = rows.documents[0]["nombre"].toString()
                        var vAdmin:Int = rows.documents[0]["admin"].toString().toInt()
                        var vPuntuacion:Int = rows.documents[0]["puntuacion"].toString().toInt()
                        var vUltimoPto:Int = rows.documents[0]["ultimo_punto"].toString().toInt()
                        this.usuario = User(vId, vNombre, vAdmin, vPuntuacion, vUltimoPto)
                        createSuccess = true
                    }
                }
                .addOnFailureListener { exception ->
                    val functionName = object{}.javaClass.enclosingMethod.name
                    println("[ERROR] Found at: ${functionName.toString()}")
                }

             */
            return createSuccess
        }

        fun updateLastUserId() {
            //var rowCount: Int = 0
            val querydb = getDbInstance().collection("Usuarios")
            val query = querydb
                .get()
                .addOnSuccessListener { rows ->
                    this.lastUserid = rows.size()

                }
                .addOnFailureListener { exception ->
                    val functionName = object{}.javaClass.enclosingMethod.name
                    println("[ERROR] Found at: ${functionName.toString()}")
                }
        }

        fun getLastUserId() = this.lastUserid



        /*
        fun getLastUserId(): Int {
            var rowCount: Int = 0
            getDbInstance().collection("Usuarios").get().
            addOnSuccessListener { registros->
                if (registros.size() > 0) {
                    rowCount = registros.size()
                }
            }
            .addOnFailureListener{
                val functionName = object{}.javaClass.enclosingMethod.name
                println("[ERROR] Found at: ${functionName.toString()}")
            }
            this.lastUserid = rowCount
            println("**** Last user id: " + rowCount)
            return rowCount;
        }


         */
        fun getUser() = this.usuario

    }
}