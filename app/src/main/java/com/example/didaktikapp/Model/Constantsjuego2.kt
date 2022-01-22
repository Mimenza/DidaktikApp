package com.example.didaktikapp.Model

import android.content.Context
import com.example.didaktikapp.R


object Constantsjuego2 {

   //Recojemos todas las preguntas
    const val TOTAL_QUESTIONS: String= "total_question"
    //Recojemos las respuestas correctas
    const val CORRECT_ANSWERS: String="correct_answers"


//HACEMOS UN GET DE TODAS LAS PREGUNTAS
    fun getQuestions(): ArrayList<Preguntasjuego2>{

      val questionList= ArrayList<Preguntasjuego2>()
    val que1= Preguntasjuego2(1,
        "Ematen digu fruituak, gerizpea eta itzala, ekaitza iristen denean… azpian babes gaitzala!",
       "Zuhaitza",
        "Etxea",
        "Lorea",
        "Atea",
        1)


    val que2= Preguntasjuego2(2,
        "Zaharra naiz ni, zaharra da herria, Murgia jauregiaren ate gainean nagoen harria.",
       "Teilatua",
        "Armarria",
        "Leihoa",
        "Sarrera",
        2)

    val que3= Preguntasjuego2(3,
        "Gogorra eta indartsua naiz, ez nauzue apurtuko garaiz, inguruan ikusten nauzue maiz. Nor naiz?",
        "Egurra",
        "Zuhaitzak",
        "Harria",
       "Leihoa",
        3)

    val que4= Preguntasjuego2(4,
      "Tipi-tapa banoa gorantza, tipi-tapa banoa beherantza, tipi-tapa guztiak batera igo eta jaistean datza.",
       "Eskailerak",
       "Leihoak",
        "Zuhaitzak",
        "Hostoak",
        1)

            questionList.add(que1)
            questionList.add(que2)
            questionList.add(que3)
            questionList.add(que4)

          return questionList
    }
}