package com.example.didaktikapp.fragments.juegos

import android.media.MediaPlayer
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.navigation.findNavController
import com.example.didaktikapp.R
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class Fragment1_2_juego_results : Fragment() {

    private lateinit var audio: MediaPlayer
    private lateinit var scoreUser: TextView
    private lateinit var btnTerminar: Button
    private lateinit var btnRetry: Button
    private lateinit var btnRetry2: Button


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment1_2_juego_results, container, false)

        scoreUser = view.findViewById(R.id.txtv1_2_scoreuser)
        btnTerminar = view.findViewById(R.id.btn1_2_terminar)
        btnRetry = view.findViewById(R.id.btn1_2_saiatuberriro)
        btnRetry2 = view.findViewById(R.id.btn1_2_saiatuberriro2)

        //Recojemos los datos del intent en el fragment del juego 2
        val sharedPreferences = requireContext().getSharedPreferences("scoreGame2", 0)
        val correctAnswers = sharedPreferences?.getString("correctAnswers", null)?.toInt()
        val totalQuestions = sharedPreferences?.getString("totalQuestions", null)?.toInt()

        //txtv1_2_scoreuser.text = "Zure emaitza: $correctAnswers/$totalQuestions"
        scoreUser.text = "Zure emaitza: $correctAnswers/$totalQuestions"

        //Boton finish que nos redirecciona al mapa
        if (correctAnswers != null) {
            if (correctAnswers.toInt() >= 2) {
                //Audio acierto
                runBlocking() {
                    launch {
                        audio = MediaPlayer.create(requireContext(), R.raw.ongiaudioa)
                        audio.start()
                    }
                }

                btnTerminar.setOnClickListener {
                    audio.stop()
                    view?.findNavController()?.navigate(R.id.action_fragment1_2_juego_results_to_fragment2_2_minijuego)
                }

                btnRetry.setOnClickListener {
                    audio.stop()
                    view?.findNavController()?.navigate(R.id.action_fragment1_2_juego_results_to_fragment1_2_juego)
                }

                //Ocultamos boton intentar de nuevo
                btnRetry2.isVisible = false
            } else {
                //Audio error
                runBlocking() {
                    launch {
                        audio = MediaPlayer.create(requireContext(), R.raw.gaizkiaudioa)
                        audio.start()
                    }
                }
                btnTerminar.isVisible = false
                btnRetry.isVisible = false

                btnRetry2.setOnClickListener {
                    audio.stop()
                    view?.findNavController()?.navigate(R.id.action_fragment1_2_juego_results_to_fragment1_2_juego)
                }
            }
        }
        // Inflate the layout for this fragment
        return view
    }
}
