package com.example.didaktikapp.fragments.juegos

import android.media.MediaPlayer
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.example.didaktikapp.Model.Constantsjuego2
import com.example.didaktikapp.R
import kotlinx.android.synthetic.main.fragment1_2_juego_results.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


class Fragment1_2_juego_results : Fragment() {

    private lateinit var audio: MediaPlayer

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment1_2_juego_results, container, false)

        //Recojemos los datos del intent en el fragment del juego 2
        val totalQuestions = activity?.intent?.getIntExtra(Constantsjuego2.TOTAL_QUESTIONS, 0)
        val correctAnswers = activity?.intent?.getIntExtra(Constantsjuego2.CORRECT_ANSWERS, 0)


        txtv7_scoreuser.text = "Zure emaitza: $correctAnswers/$totalQuestions"


        //Boton finish que nos redirecciona al mapa

        if (correctAnswers != null) {
            if (correctAnswers >= 2) {
                //Audio acierto
                runBlocking() {
                    launch {
                        audio = MediaPlayer.create(requireContext(), R.raw.ongiaudioa)
                        audio.start()
                    }
                }
                btn7_terminar.setOnClickListener {
                    audio.stop()
                }
                btn7_saiatuberriro.setOnClickListener {

                }

                //Ocultamos boton intentar de nuevo

                btn7_saiatuberriro2.isVisible = false
            } else {
                //Audio error
                runBlocking() {
                    launch {
                        audio = MediaPlayer.create(requireContext(), R.raw.gaizkiaudioa)
                        audio.start()
                    }
                }
                btn7_terminar.isVisible = false
                btn7_saiatuberriro.isVisible = false
                btn7_saiatuberriro2.setOnClickListener {

                }
            }
        }
    }
}
