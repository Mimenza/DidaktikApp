package com.example.didaktikapp.fragments.juegos

import android.media.MediaPlayer
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.example.didaktikapp.Model.Constantsjuego7
import com.example.didaktikapp.R
import kotlinx.android.synthetic.main.fragment1_7_juego_results.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class Fragment1_7_juego_results : Fragment() {

    private lateinit var audio: MediaPlayer

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment1_7_juego_results, container, false)

        val totalQuestions = activity?.intent?.getIntExtra(Constantsjuego7.TOTAL_QUESTIONS, 0)
        val correctAnswers = activity?.intent?.getIntExtra(Constantsjuego7.CORRECT_ANSWERS, 0)



        txtv7_1_scoreuser.text = "Zure emaitza: $correctAnswers/$totalQuestions"


        //Boton finish que nos redirecciona al mapa

        if (correctAnswers != null) {
            if (correctAnswers >= 6) {
                //Audio acierto
                runBlocking() {
                    launch {
                        audio = MediaPlayer.create(requireContext(), R.raw.ongiaudioa7)
                        audio.start()
                    }
                }
                btn7_1_terminar.setOnClickListener {
                    audio.stop()
                }
                btn7_1_saiatuberriro.setOnClickListener {

                }
                btn7_1_saiatuberriro2.isVisible = false

            } else {
                //Audio error
                runBlocking() {
                    launch {
                        audio = MediaPlayer.create(requireContext(), R.raw.gaizkiaudioa)
                        audio.start()
                    }
                }
            }

            btn7_1_terminar.isVisible = false
            btn7_1_saiatuberriro.isVisible = false
            btn7_1_saiatuberriro2.setOnClickListener {

            }
        }
    }
}
