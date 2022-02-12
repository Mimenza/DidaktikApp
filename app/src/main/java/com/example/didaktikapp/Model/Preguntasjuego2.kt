package com.example.didaktikapp.Model

/**
 * Data class con los atributos que guardamos para las preguntas del juego 2
 *
 * @property id atributo que guarda la id
 * @property question atributo que guarda la pregunta
 * @property optionOne atributo que guarda la opción 1 de las respuestas
 * @property optionTwo atributo que guarda la opción 2 de las respuestas
 * @property optionThree atributo que guarda la opción 3 de las respuestas
 * @property optionFour atributo que guarda la opción 4 de las respuestas
 * @property correctAnswer atributo que guarda la opción correcta de las respuestas
 */
data class Preguntasjuego2(

    val id: Int? = null,
    val question: String? = null,
    val optionOne: String? = null,
    val optionTwo: String? = null,
    val optionThree: String? = null,
    val optionFour: String? = null,
    val correctAnswer: Int? = null

)

