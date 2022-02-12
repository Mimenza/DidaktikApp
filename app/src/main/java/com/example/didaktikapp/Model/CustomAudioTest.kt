package com.example.didaktikapp.Model

import android.app.Activity
import android.media.MediaPlayer

//modelo para modificar los audios
object CustomAudioTest {
    init {
        println("****************** SINGLETON OBJECT INICIALIZADO")

    }

    //TODO INTENTAR IMPLEMENTAR CALLBACKS PARA CIERTOS EVENTOS (setOnCompletionListener)
    //TODO POSIBLE SOLUCION: RETORNAR EL MISMO OBJETO Y USAR LOS EVENTOS DE LA MISMA CLASE

    var audio: MediaPlayer? = null //audio

    /**
     * Start del audio
     *
     * @param audioObject el audio que queremos que se inicie
     * @return el audio
     */
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

    /**
     * Función para parar el audio
     *
     */
    fun stopAudio() {
        audio?.stop()
        audio = null
    }


}