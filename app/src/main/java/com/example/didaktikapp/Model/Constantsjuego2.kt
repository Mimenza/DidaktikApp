package com.example.didaktikapp.Model

import com.example.didaktikapp.R


object Constantsjuego2 {

//HACEMOS UN GET DE TODAS LAS PREGUNTAS
    fun getQuestions(): ArrayList<Preguntasjuego2>{

      val questionList= ArrayList<Preguntasjuego2>()

    val que1= Preguntasjuego2(1,
        R.string.que1_juego2.toString(),
       R.string.que1_ans1_juego2.toString(),
        R.string.que1_ans2_juego2.toString(),
        R.string.que1_ans3_juego2.toString(),
        R.string.que1_ans4_juego2.toString(),
        1)


    val que2= Preguntasjuego2(2,
        R.string.que2_juego2.toString(),
        R.string.que2_ans1_juego2.toString(),
        R.string.que2_ans2_juego2.toString(),
        R.string.que2_ans3_juego2.toString(),
        R.string.que2_ans4_juego2.toString(),
        2)

    val que3= Preguntasjuego2(3,
        R.string.que3_juego2.toString(),
        R.string.que3_ans1_juego2.toString(),
        R.string.que3_ans2_juego2.toString(),
        R.string.que3_ans3_juego2.toString(),
        R.string.que3_ans4_juego2.toString(),
        3)

    val que4= Preguntasjuego2(4,
        R.string.que4_juego2.toString(),
        R.string.que4_ans1_juego2.toString(),
        R.string.que4_ans2_juego2.toString(),
        R.string.que4_ans3_juego2.toString(),
        R.string.que4_ans4_juego2.toString(),
        1)

            questionList.add(que1)
            questionList.add(que2)
            questionList.add(que3)
            questionList.add(que4)

          return questionList
    }
}