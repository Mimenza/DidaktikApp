package com.example.didaktikapp.activities

import com.example.reto01.Model.User
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

class DbHandler {

    companion object {
        private var lastUserid: Int = 0
        private var usuario: User? = null
        private var dbInstance: FirebaseFirestore? = null
        private var isAdmin: Boolean = false

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

        fun setTutorialFinalizado() {
            this.usuario!!.tutorialFinalizado = 1
        }

        fun getTutorialFinalizado() = this.usuario!!.tutorialFinalizado

        fun setUser(pUser: User) {
            this.usuario = pUser
        }

        fun setLastUserId(pNumber: Int) {
            this.lastUserid = pNumber
        }

        fun userAumentarPuntuacion(pAumento: Int) {
            this.usuario!!.puntuacion = this.usuario!!.puntuacion!! + pAumento
        }

        fun userActualizarUltimoPunto(pNuevoPunto: Int) {
            if (pNuevoPunto > this.usuario!!.ultima_puntuacion!!) {
                this.usuario!!.ultima_puntuacion = pNuevoPunto
            }
        }
    }

    fun requestDbUserCount(pUserRegister: String?, callback: QueryResponseDone) {
        getDbInstance().collection("Usuarios").get()
        .addOnSuccessListener {
            lastUserid = it.size()
            callback.responseDbUserCount(pUserRegister,it.size())
        }
    }

    fun requestDbUserLogin(pUsername: String, callback: QueryResponseDone) {
        getDbInstance().collection("Usuarios").whereEqualTo("nombre", pUsername).get()
        .addOnSuccessListener {
            if (it.size() > 0) {
                val vId:Int = it.documents[0]["id"].toString().replace("u","").toInt()
                val vNombre:String = it.documents[0]["nombre"].toString()
                //var vAdmin:Int = it.documents[0]["admin"].toString().toInt()
                val vTutoFinalizado:Int = it.documents[0]["tutorialFinalizado"].toString().toInt()
                val vPuntuacion:Int = it.documents[0]["puntuacion"].toString().toInt()
                val vUltimoPto:Int = it.documents[0]["ultimo_punto"].toString().toInt()
                setUser(User(vId, vNombre, vTutoFinalizado, vPuntuacion, vUltimoPto))
                callback.responseDbUserLogin(null)
            } else {
                callback.responseDbUserLogin(pUsername)
            }

        }
    }

    fun getHashObjectTest() {

    }

    fun requestDbUserUpdate(callback: QueryResponseDone) {
        if (usuario == null) {
            return
        }
        //FIXME ESTO PUEDE TAL VEZ PUEDE SER MEJORADO AHORRANDO LA CONSULTA DE BUSCAR EL USUARIO
        getDbInstance().collection("Usuarios").whereEqualTo("nombre", usuario!!.nombre).get()
            .addOnSuccessListener {
                if (it.size() > 0) {
                    val userData = hashMapOf(
                        "tutorialFinalizado" to usuario!!.tutorialFinalizado,
                        "puntuacion" to usuario!!.puntuacion,
                        "ultimo_punto" to usuario!!.ultima_puntuacion)
                    getDbInstance().collection("Usuarios").document(usuario!!.id.toString())
                        .set(userData, SetOptions.merge())
                        .addOnSuccessListener {
                            callback.responseDbUserUpdated(true)
                        }
                        .addOnFailureListener {
                            callback.responseDbUserUpdated(false)
                        }
                } else {
                    callback.responseDbUserUpdated(false)
                }
            }
            .addOnFailureListener {
                callback.responseDbUserUpdated(false)
            }
    }

    fun requestDbUserRegister(pUsername: String, callback: QueryResponseDone) {
        val newQuickId: Int = lastUserid + 1
        val defaultData = hashMapOf(
            //"admin" to 0,
            "id" to "u$newQuickId",
            "nombre" to pUsername,
            "tutorialFinalizado" to 0,
            "puntuacion" to 0,
            "ultimo_punto" to 0)
        getDbInstance().collection("Usuarios").document(newQuickId.toString())
            .set(defaultData)
            .addOnSuccessListener {
                setUser(User(newQuickId, pUsername,0,  0, 0))
                setLastUserId(newQuickId)
                callback.responseDbUserRegister(true)
            }
            .addOnFailureListener {
                callback.responseDbUserRegister(false)
            }
    }


    interface QueryResponseDone {
        fun responseDbUserLogin(accountRegister: String?) {}

        fun responseDbUserCount(accountRegister: String?, response: Int) {}

        fun responseDbUserRegister(response: Boolean) {}

        fun responseDbUserUpdated(response: Boolean) {}
    }
}