package com.example.didaktikapp.Model

import android.content.Context
import com.example.didaktikapp.R


object Constantsjuego2 {


    /**
     * HACEMOS UN GET DE TODAS LAS PREGUNTAS
     *
     * @return DEVUELVE LA LISTA DE LAS PREGUNTAS DEL JUEGO 2
     */
    fun getQuestions(): ArrayList<Preguntasjuego2>{

      val questionList= ArrayList<Preguntasjuego2>() //arraylist de las preguntas del juego 2 vacio
   //pregunta 1 del data class pregunta juego 2
    val que1= Preguntasjuego2(1,
        "Ematen digu fruituak, gerizpea eta itzala, ekaitza iristen denean… azpian babes gaitzala!",
       "Zuhaitza",
        "Etxea",
        "Lorea",
        "Atea",
        1)

   //pregunta 2 del data class pregunta juego 2
    val que2= Preguntasjuego2(2,
        "Zaharra naiz ni, zaharra da herria, Murgia jauregiaren ate gainean nagoen harria.",
       "Teilatua",
        "Armarria",
        "Leihoa",
        "Sarrera",
        2)
   //pregunta 3 del data class pregunta juego 2
    val que3= Preguntasjuego2(3,
        "Gogorra eta indartsua naiz, ez nauzue apurtuko garaiz, inguruan ikusten nauzue maiz. Nor naiz?",
        "Egurra",
        "Zuhaitzak",
        "Harria",
       "Leihoa",
        3)
        //pregunta 4 del data class pregunta juego 2
    val que4= Preguntasjuego2(4,
      "Tipi-tapa banoa gorantza, tipi-tapa banoa beherantza, tipi-tapa guztiak batera igo eta jaistean datza.",
       "Eskailerak",
       "Leihoak",
        "Zuhaitzak",
        "Hostoak",
        1)

        //Añadimos las preguntas al array creado anteriormente
            questionList.add(que1)
            questionList.add(que2)
            questionList.add(que3)
            questionList.add(que4)

        //Añadimos las preguntas al array creado anteriormente
          return questionList
    }
}