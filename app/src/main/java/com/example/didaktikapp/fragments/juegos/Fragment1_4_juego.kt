package com.example.didaktikapp.fragments.juegos

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.Navigation
import com.example.didaktikapp.Model.CustomLine
import com.example.didaktikapp.Model.DragnDropImage
import com.example.didaktikapp.R
import com.example.didaktikapp.activities.Activity6_Site
import com.example.didaktikapp.activities.DbHandler

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Fragment1_juego.newInstance] factory method to
 * create an instance of this fragment.
 */
class Fragment1_4_juego : Fragment() {
    private val thisJuegoId = 4
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var layout: ConstraintLayout

    private var myLineTest: CustomLine? = null

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
        val view = inflater.inflate(R.layout.fragment1_4_juego, container, false)
        val button: Button = view.findViewById(R.id.btnf1_4_siguiente)
        val ajustes: ImageButton = view.findViewById(R.id.btnf1_4_ajustes)
        layout = view.findViewById(R.id.myConstraintTest)

        val imageTest: ImageView = view.findViewById(R.id.manzanaTest)
        imageTest.setOnTouchListener(listener)
        myLineTest = CustomLine(requireContext(),0f,0f,250f,250f, 15F, 162, 224, 23)
        layout.addView(myLineTest)

        button.setOnClickListener(){
            Navigation.findNavController(view).navigate(R.id.action_fragment1_4_juego_to_fragment2_4_minijuego)
        }
        ajustes.setOnClickListener(){

                (activity as Activity6_Site?)?.menuCheck()


        }
        return view
    }

    @SuppressLint("ClickableViewAccessibility")
    var listener = View.OnTouchListener { viewElement, motionEvent ->
        viewElement.bringToFront()
        val action = motionEvent.action
        when(action) {
            MotionEvent.ACTION_MOVE -> {
                viewElement.x = motionEvent.rawX - viewElement.width/2
                viewElement.y = motionEvent.rawY - viewElement.height/2
                layout.removeView(myLineTest)
                myLineTest = null
                myLineTest = CustomLine(requireContext(),0f,0f,motionEvent.rawX,motionEvent.rawY, 15F, 162, 224, 23)
                layout.addView(myLineTest)
            }
            MotionEvent.ACTION_UP -> {
                viewElement.x = motionEvent.rawX - viewElement.width/2
                viewElement.y = motionEvent.rawY - viewElement.height/2


            }
        }
        true
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
            Fragment1_4_juego().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}