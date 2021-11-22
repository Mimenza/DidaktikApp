package com.example.didaktikapp.fragments.juegos

import `in`.codeshuffle.typewriterview.TypeWriterView
import android.graphics.Canvas
import android.media.MediaPlayer
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import androidx.navigation.Navigation
import com.example.didaktikapp.R
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.ArrayList
import android.graphics.Paint

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Fragment1_juego.newInstance] factory method to
 * create an instance of this fragment.
 */
class Fragment1_1_juego : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    var paint = Paint()
    val canvas = Canvas()
    private val p1: List<Float> = ArrayList()
    private val p2: List<Float> = ArrayList()
    private val p3: List<Float> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment1_1_juego, container, false)
        val button: Button = view.findViewById(R.id.btnf1_1siguiente)
        val ajustes: ImageButton = view.findViewById(R.id.btnf1_1_ajustes)
        var ring: MediaPlayer

        button.setOnClickListener() {
            Navigation.findNavController(view).navigate(R.id.action_fragment1_1_juego_to_fragment2_1_minijuego)
        }

        ajustes.setOnClickListener() {
            Navigation.findNavController(view).navigate(R.id.action_fragment1_1_juego_to_fragment4_menu)
        }

        //Typewriter juego 1 tutorial
        val typeWriterView = view.findViewById(R.id.txtv1_1tutorialjuego1) as TypeWriterView
        typeWriterView.setWithMusic(false)
        typeWriterView.animateText(resources.getString(R.string.jositajositext))
        typeWriterView.setDelay(70)
        //Typewriter juego 1 tutorial fin

        //Audio juego 1
        runBlocking() {
            launch {
                ring = MediaPlayer.create(context, R.raw.juego1audio)
                ring.start()
            }
        }
        //Audio juego 1 fin

        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Fragment1_juego.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Fragment1_1_juego().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}