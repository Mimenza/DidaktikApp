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
import com.example.didaktikapp.activities.DbHandler
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class Fragment1_2_juego_results : Fragment(), DbHandler.QueryResponseDone {


    /*hay dos botones retry, si el usuario aprueba se mostrará
     el boton btnTerminar y el boton btnRetry. Si supende, el boton btnRetry2 solo*/

    private lateinit var audio: MediaPlayer //audio
    private lateinit var scoreUser: TextView //resultado del juego 2
    private lateinit var btnTerminar: Button  //boton para terminar e ir al minijuego2
    private lateinit var btnRetry: Button  //boton para reiniciar el juego 2
    private lateinit var btnRetry2: Button //boton para reiniciar el juego 2
    val thisJuegoId: Int = 2 //id del juego (para las puntuaciones al terminar el juego)

    override fun onCreateView(

        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment1_2_juego_results, container, false)

        //Inicializar las vistas
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

        //Si aprueba el juego (2/4) o más
        if (correctAnswers != null) {
            if (correctAnswers.toInt() >= 2) {
                //Audio acierto
                DbHandler.userAumentarPuntuacion(10)
                DbHandler.userActualizarUltimoPunto(thisJuegoId)
                DbHandler().requestDbUserUpdate(this)
                runBlocking() {
                    launch {
                        audio = MediaPlayer.create(requireContext(), R.raw.ongiaudioa)
                        audio.start()
                    }
                }
                //al hacer click en terminar, para el audio y a traves del nav nos redirecciona al minijuego 2
                btnTerminar.setOnClickListener {
                    audio.stop()
                    view?.findNavController()?.navigate(R.id.action_fragment1_2_juego_results_to_fragment2_2_minijuego)
                }
                //al hacer click en terminar, para el audio y a traves del nav nos redirecciona al juego 2
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
                //al hacer click en terminar, para el audio y a traves del nav nos redirecciona al juego 2
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
