package com.example.didaktikapp.activities

import com.example.reto01.Model.User
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

/**
 *
 * Esta es una clase Gestora semi-hibrida para controlar las consultas a la base de datos y la gestion del objeto del usuario
 * Se caracteriza por usar una sola instancia de la misma y evitar consultas innecesarias a la misma
 * @author Luis
 *
 */

class DbHandler {

    /**
     *
     * Para evitar crear multiples instancias de la clase, tenemos la parte estatica y dinamica
     * Tras inicializar o crear instancias de los objetos relacionados con la base de datos, los almacenamos ne la variables
     * @param lastUserid Variable para controlar la creacion de nuevos usuarios y asignarle una key en la BD
     * @param usuario Objeto que almacena todos los datos del usuario (Nombre, puntos, ultimo punto ...etc) [CONSULTAR CLASE DEL OBJETO]
     * @param dbInstance Variable para controlar una simple instancia del objeto de FirebaseFirestore
     * @param isAdmin Variable para controlar el modo administrador de la aplicacion
     *
     */

    companion object {
        private var lastUserid: Int = 0
        private var usuario: User? = null
        private var dbInstance: FirebaseFirestore? = null
        private var isAdmin: Boolean = false

        /**
         * Metodo para obtener la instancia del objeto que gestion la conexion con la base de datos de FireBase
         *
         * @return Un objeto de tipo FirebaseFirestore para realizar consultas de diferentes clase (En caso de querer progresar en la aplicacion)
         */
        fun getDbInstance(): FirebaseFirestore {
            if (dbInstance == null) {
                dbInstance = FirebaseFirestore.getInstance()
            }
            return dbInstance!!
        }
        /**
         * Metodo para obtener el objeto del usuario y luego poder acceder a sus propiedades
         *
         * @return Un objeto de tipo User que contiene diversas propiedades
         */
        fun getUser() = this.usuario

        /**
         * Metodo para comprobar si el modo admin esta activado o no
         *
         * @return Booleano que indica el estado del modo administrador
         */
        fun getAdmin() = this.isAdmin

        /**
         * Metodo que cambiar el estado de la variable del modo administrador
         *
         * @param Boolean true para activar el modo admin, falso para caso contrario
         */
        fun setAdmin(value: Boolean) {
            this.isAdmin = value
        }

        /**
         * Metodo para establecer el tutorial finalizado para un usuario con respecto a la parte del mapa
         *
         */
        fun setTutorialFinalizado() {
            this.usuario!!.tutorialFinalizado = 1
        }

        /**
         * Metodo que devuelve el estado del tutorial finalizado
         *
         * @return Booleano, true indica que el tutorial se ha finalizado, falso caso contrario
         */
        fun getTutorialFinalizado() = this.usuario!!.tutorialFinalizado

        /**
         * Metodo para establecer el objeto de tipo User de manera estatica en esta clase
         *
         * @param pUser Objeto de tipo User
         */
        fun setUser(pUser: User) {
            this.usuario = pUser
        }

        /**
         * Metodo para establecer el ultimo id del usuario registrado en la base de datos y asi poder gestionar posteriormente nuevos registros
         *
         * @param pNumber Objeto de tipo Integer
         */
        fun setLastUserId(pNumber: Int) {
            this.lastUserid = pNumber
        }

        /**
         * Metodo para modificar una de las propiedades del usuario (los puntos). Este metodo se llamada desde diferentes clases
         *
         * @param pAumento Objeto de tipo Integer con el valor que se desea aumentar/reducir
         */
        fun userAumentarPuntuacion(pAumento: Int) {
            this.usuario!!.puntuacion = this.usuario!!.puntuacion!! + pAumento
        }

        /**
         * Metodo para actualizar el ultimo punto del usuario para controlar el progreso del juego
         *
         * @param pNuevoPunto Objeto de tipo Integer indicando el ultimo punto superado del juego
         */
        fun userActualizarUltimoPunto(pNuevoPunto: Int) {
            if (pNuevoPunto > this.usuario!!.ultima_puntuacion!!) {
                this.usuario!!.ultima_puntuacion = pNuevoPunto
            }
        }
    }

    /**
     * Metodo asociado a la interfaz de la clase para controlar las respuestas ASINCRONAS de la base de datos
     *
     * @param pUserRegister String indicando el nombre del usuario al que se desea acceder.
     * @param callback Respuesta al callback de la interfaz para obtener una respuesta de que se ha realizado correctamente/erroneamente.
     */
    fun requestDbUserCount(pUserRegister: String?, callback: QueryResponseDone) {
        getDbInstance().collection("Usuarios").get()
        .addOnSuccessListener {
            lastUserid = it.size()
            callback.responseDbUserCount(pUserRegister,it.size())
        }
    }

    /**
     * Metodo para comprobar el inicio de sesion del usuario mediante su nombre
     *
     * @param pUsername String indicando el nombre al que se desea acceder.
     * @param callback Respuesta al callback de la interfaz para obtener una respuesta de que se ha realizado correctamente/erroneamente.
     */
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

    /**
     * Metodo para actualizar los datos del usuario en la BD (Puntuacion, ultimo punto ...) basandonos en los datos del objeto del tipo User
     *
     * @param callback Respuesta al callback de la interfaz para obtener una respuesta de que se ha realizado correctamente/erroneamente.
     */
    fun requestDbUserUpdate(callback: QueryResponseDone) {
        if (usuario == null) {
            return
        }
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

    /**
     * Metodo para crear el usuario con ciertos datos predeterminados en la BD.
     *
     * @param pUsername String indicando el nombre de usuario que se desea obtener
     * @param callback Respuesta al callback de la interfaz para obtener una respuesta de que se ha realizado correctamente/erroneamente.
     */
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


    /**
     * Metodos de la interfaz implementada para controlar las respuestas asincronas de la BD junto a los metodos antes comentados
     *
     */
    interface QueryResponseDone {
        fun responseDbUserLogin(accountRegister: String?) {}

        fun responseDbUserCount(accountRegister: String?, response: Int) {}

        fun responseDbUserRegister(response: Boolean) {}

        fun responseDbUserUpdated(response: Boolean) {}
    }
}