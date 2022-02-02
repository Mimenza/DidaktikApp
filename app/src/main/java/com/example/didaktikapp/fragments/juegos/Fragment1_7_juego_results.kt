package com.example.didaktikapp.fragments.juegos

import android.app.Activity
import android.media.MediaPlayer
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentManager
import androidx.navigation.findNavController
import com.example.didaktikapp.R
import com.example.didaktikapp.activities.DbHandler
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class Fragment1_7_juego_results : Fragment(), DbHandler.QueryResponseDone {
    private val thisJuegoId = 7
    private lateinit var audio: MediaPlayer
    private lateinit var scoreUser: TextView
    private lateinit var btnTerminar: Button
    private lateinit var btnRetry: Button
    private lateinit var btnRetry2: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment1_7_juego_results, container, false)

        scoreUser = view.findViewById(R.id.txtv7_1_scoreuser)
        btnTerminar = view.findViewById(R.id.btn7_1_terminar)
        btnRetry = view.findViewById(R.id.btn7_1_saiatuberriro)
        btnRetry2 = view.findViewById(R.id.btn7_1_saiatuberriro2)


        //Recojemos los datos del intent en el fragment del juego 2
        val sharedPreferences = requireContext().getSharedPreferences("scoreGame7", 0)
        val correctAnswers = sharedPreferences?.getString("correctAnswers", null)?.toInt()
        val totalQuestions = sharedPreferences?.getString("totalQuestions", null)?.toInt()

        println(correctAnswers)
        println(totalQuestions)

        scoreUser.text = "Zure emaitza: $correctAnswers/$totalQuestions"


        //Boton finish que nos redirecciona al mapa

        if (correctAnswers != null) {
            if (correctAnswers >= 6) {
                //Audio acierto
                DbHandler.userAumentarPuntuacion(10)
                DbHandler.userActualizarUltimoPunto(thisJuegoId)
                DbHandler().requestDbUserUpdate(this)

                btnTerminar.setOnClickListener {
                    if (this::audio.isInitialized){
                        if(audio.isPlaying){
                            audio.stop()
                        }
                    }

                    view?.findNavController()?.navigate(R.id.action_fragment1_7_juego_results_to_fragment2_7_minijuego)
                }

                btnRetry.setOnClickListener {
                    if (this::audio.isInitialized){
                        if(audio.isPlaying){
                            audio.stop()
                        }
                    }
                    view?.findNavController()?.navigate(R.id.action_fragment1_7_juego_results_to_fragment1_7_juego)
                }

                btnTerminar.isVisible = true
                btnRetry.isVisible = true
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
                btnRetry2.isVisible = true
            }


            btnRetry2.setOnClickListener {
                if (this::audio.isInitialized){
                    if(audio.isPlaying){
                        audio.stop()
                    }
                }
                view?.findNavController()?.navigate(R.id.action_fragment1_7_juego_results_to_fragment1_7_juego)
            }
        }
        return view
    }
}
