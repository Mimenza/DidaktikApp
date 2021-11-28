package com.example.didaktikapp.fragments.juegos

import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.Navigation
import com.example.didaktikapp.Model.CustomLine
import com.example.didaktikapp.R

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Fragment1_juego.newInstance] factory method to
 * create an instance of this fragment.
 */
class Fragment1_3_2_juego : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var globalView: View

    private var viewActiva = false

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
        val view = inflater.inflate(R.layout.fragment1_3_2_juego, container, false)
        globalView = view
        val button: Button = view.findViewById(R.id.btnf1_3_2_siguiente)
        val ajustes: ImageButton = view.findViewById(R.id.btnf1_3_2_ajustes)


        val constraintLayoutFound = globalView.findViewById<ConstraintLayout>(R.id.mainlayout)
        var newView: ImageView
        newView = ImageView(requireContext())
        newView.layoutParams.height = 200
        newView.layoutParams.width = 200
        newView.x = 300F
        newView.y = 500F
        newView.setBackgroundColor(Color.BLUE)
        //newView.setImageResource(R.drawable.sagarragorria)

        constraintLayoutFound.addView(newView)

        button.setOnClickListener(){
            Navigation.findNavController(view).navigate(R.id.action_fragment1_3_2_juego_to_fragment2_3_2_minijuego)
        }
        ajustes.setOnClickListener(){
            Navigation.findNavController(view).navigate(R.id.action_fragment1_3_2_juego_to_fragment4_menu)
        }
        return view
    }

    fun startTimeCounter(view: View, timeInSeconds: Int) {
        object: CountDownTimer(50000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                //Each second create an element
            }
            override fun onFinish() {
                //Timer Finished + Might this should show win screen
            }
        }.start()
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
            Fragment1_3_2_juego().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}