package com.example.reto01.Model


/**
 * Data class con los atributos que guardamos para el usuario
 *
 * @property id del usuario
 * @property nombre del usuario
 * @property tutorialFinalizado si es nuevo usuario, este atributo está en 0,
 * por lo que le saldrá por ejemplo el texto de bienvenida, o el tutorial de mapa
 * @property puntuacion del usuario
 * @property ultima_puntuacion del usuario
 */
data class User(
    var id: Int? = null,
    var nombre: String? = null,
    //var admin: Int? = null,
    var tutorialFinalizado: Int? = null,
    var puntuacion: Int? = null,
    var ultima_puntuacion: Int? = null,
)

