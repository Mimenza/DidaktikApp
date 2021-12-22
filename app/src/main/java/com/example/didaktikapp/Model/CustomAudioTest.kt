package com.example.didaktikapp.Model

import android.app.Activity
import android.media.MediaPlayer

object CustomAudioTest {
    init {
        println("****************** SINGLETON OBJECT INICIALIZADO")

        /*
        var audio: MediaPlayer? = null

        fun playAudio(audioObject: MediaPlayer) {
            audio?.stop()
            audio = null
            audio = audioObject
            /*
            if (audio != null) {
                audio = null
            }

             */
        }

        fun stopAudio() {
            audio?.stop()
            audio = null
        }

         */
    }

    //TODO INTENTAR IMPLEMENTAR CALLBACKS PARA CIERTOS EVENTOS (setOnCompletionListener)
    //TODO POSIBLE SOLUCION: RETORNAR EL MISMO OBJETO Y USAR LOS EVENTOS DE LA MISMA CLASE

    var audio: MediaPlayer? = null

    fun playAudio(audioObject: MediaPlayer): MediaPlayer? {
        try {
            stopAudio()
            audio = audioObject
            audio!!.start()
            return  audio
        } catch (e: Exception) {
            return null
        } finally {
            return null
        }

    }

    fun stopAudio() {
        audio?.stop()
        audio = null
    }


}