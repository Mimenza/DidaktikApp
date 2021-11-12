package com.example.didaktikapp.activities

import android.content.Intent
import android.widget.Toast
import com.example.reto01.Model.User
import com.google.firebase.firestore.FirebaseFirestore

class DbHandler {

    companion object {
        private var lastUserid: Int? = null
        private var usuario: User? = null
        private var dbInstance: FirebaseFirestore? = null

        fun getDbInstance(): FirebaseFirestore {
            if (dbInstance == null) {
                dbInstance = FirebaseFirestore.getInstance()
            }

            //if (this.lastUserid == null) {
                //this.lastUserid = getLastUserId()
            //}
            return dbInstance!!
        }

        fun logUser(pUsername: String): Boolean {
            var logSuccess: Boolean = false
            getDbInstance().collection("Usuarios").whereEqualTo("nombre", pUsername).get()
            .addOnSuccessListener { registros ->
                if (registros.size() > 0) {
                    /*
                    var vId: Int = registros.documents[0]["id"].toString().replace("u","").toInt()
                    var vNombre: String = registros.documents[0]["nombre"].toString()
                    var vAdmin: Int = registros.documents[0]["admin"].toString().toInt()
                    var vPuntuacion: Int = registros.documents[0]["puntuacion"].toString().toInt()
                    var vUltimoPto: Int = registros.documents[0]["ultimo_punto"].toString().toInt()
                    this.usuario = User(vId,vNombre,vAdmin,vPuntuacion,vUltimoPto)
                    logSuccess = true

                     */
                }
            }
            .addOnFailureListener{
                val functionName = object{}.javaClass.enclosingMethod.name
                println("[ERROR] Found at: ${functionName.toString()}")
            }
            return logSuccess
        }

        fun createUser(pUsername: String): Boolean {
            var createSuccess = false
            val defaultData = hashMapOf(
                "admin" to 0,
                "id" to "u"+((getLastUserId()+1).toString()),
                "nombre" to pUsername,
                "puntuacion" to 0,
                "ultimo_punto" to 0)

            getDbInstance().collection("Usuarios").document(((getLastUserId()+1).toString())).set(defaultData)
                .addOnSuccessListener {
                    createSuccess = true
                    /*
                    Toast.makeText(this, "Usuario insertado", Toast.LENGTH_SHORT).show()
                    var i = Intent(this, Activity4_bienvenida::class.java)
                    startActivity(i)
                    this.overridePendingTransition(0, 0)
                     */
                }
                .addOnFailureListener{
                    val functionName = object{}.javaClass.enclosingMethod.name
                    println("[ERROR] Found at: ${functionName.toString()}")
                    //Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
                }
            return createSuccess
        }

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
            return rowCount;
        }

        fun getUser() = this.usuario
    }
}