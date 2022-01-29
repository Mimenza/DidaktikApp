package com.example.didaktikapp.Model

import android.content.Context
import com.example.didaktikapp.R


object Constantsjuego7 {


    //HACEMOS UN GET DE TODAS LAS PREGUNTAS
    fun getQuestions(): ArrayList<Preguntasjuego7>{

        val questionList= ArrayList<Preguntasjuego7>()
        val que1= Preguntasjuego7(1,
            "1. Aurten zenbagarren txotx irekiera izango dugu aurten",
            "28. txotx irekiera",
            "25. txotx irekiera",
            "23. txotx irekiera",
            1)


        val que2= Preguntasjuego7(2,
            "2. Zein hilabetetan izaten da txotx irekiera?",
            "Urtarrilan",
            "Otsailan",
            "Martxoan",
            1)

        val que3= Preguntasjuego7(3,
            "3. Zein izan zen azkenengo txotx irekierako gonbidatua? Zein sagardotegitan ospatu zen?",
            "Onintza Enbeitia",
            "Aitziber Garmendia",
            "Izaro Andres",
            3)

        val que4= Preguntasjuego7(4,
            "4. Zein jarduera egiten dira txotx irekieraren egunean?",
            "Zanpantzarrak, sagar dantza eta kalejira",
            "Sagar dantza, txalaparta eta txistulariak",
            "Larrain dantza, idi dema eta txalaparta",
            2)
        val que5= Preguntasjuego7(5,
            "5. Zein sagardotegitan ospatu zen azkeneko txotx irekiera? ",
            "Alorrenea Sagardotegian",
            "Petritegi Sagardotegian",
            "Lizeaga Sagardotegian",
            1)
        val que6_1= Preguntasjuego7(6_1,
            "6. Zein da hitz hauen esanahia?: txotx, kizki, kupela, ama, zizarra, dolare." +
                    "    TXOTX:",
            "Sagardotegian txotx garaian kupel bat ireki aurretik sagardogileak ozen botatzen duen deia da.",
            "SAurtengo sagardoa. Horregatik, “Gure sagardo berria” esanez hasten da sagardotegien denboraldia.",
            "Sagarrak ematen dituen fruta-arbola.",
            1)
        val que6_2= Preguntasjuego7(6_2,
              "KIZKI:",
            "Kupelaren aurrean gaudela, probatzeko irekitzen den zuloa ixteko erabiltzen den ziria.",
            "60 zentimetro inguruko makila, muturrean iltze oker bat duena eta sagastian sagarrak banan-banan lurretik biltzeko erabiltzen dena.",
            "Eskuz edo mekaniko sagarra lehertzea.",
            2)
        val que6_3= Preguntasjuego7(6_3,
            "KUPELA:",
            "Sagardoa ekoizten den eraikina edo lekua.",
            "Sagarra jotzeko makina, sagarra lehertu ahala sagar-patsa askara joaten da.",
            "Sagardoa egiteko 1.000 litro baino edukiera handiagoko eduki-ontzia..",
            3)
        val que6_4= Preguntasjuego7(6_4,
            "AMA:",
            "Sagarra jotzeko erabiltzen zen zurezko mailu handia.",
            "Sagarrak biltzeko erabiltzen den kirtenik gabeko zumezko ontzia",
            "Sagardoan geratzen den hondarra.",
            3)
        val que6_5= Preguntasjuego7(6_5,
            "ZIZARRA:",
            "Sagardoa eta ura erdibana nahastuta egiten den edari ahula.",
            "Ondo heldu gabeko sasoi-hasierako sagarrekin egiten den edari gozo lodia.",
            "Iazko sagardoa.",
            2)
        val que6_6= Preguntasjuego7(6_6,
            "DOLARE:",
            "Sagardoa ekoizten den eraikina edo lekua.",
            "Kupelaren aurrean gaudela, probatzeko irekitzen den zuloa ixteko erabiltzen den ziria.",
            "Sagarra sagardo bihurtzeko baliabideak dituenez, sagardoa egiten, dastatzen eta saltzen den lekua.",
            1)


        questionList.add(que1)
        questionList.add(que2)
        questionList.add(que3)
        questionList.add(que4)
        questionList.add(que5)
        questionList.add(que6_1)
        questionList.add(que6_2)
        questionList.add(que6_3)
        questionList.add(que6_4)
        questionList.add(que6_5)
        questionList.add(que6_6)

        return questionList
    }
}