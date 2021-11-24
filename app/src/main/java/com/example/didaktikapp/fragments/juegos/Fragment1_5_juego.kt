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
import androidx.navigation.Navigation
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
class Fragment1_5_juego : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    var manzanaList: MutableList<draggableImg>? = mutableListOf()
    private lateinit var objetivo: ImageView

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
        val view = inflater.inflate(R.layout.fragment1_5_juego, container, false)
        val button: Button = view.findViewById(R.id.btnf1_5_siguiente)
        val ajustes: ImageButton = view.findViewById(R.id.btnf1_5_ajustes)

        val mznTest: ImageView = view.findViewById(R.id.imgManzanaTest)
        val mznTest3: ImageView = view.findViewById(R.id.imgManzanaTest3)
        val mznTest4: ImageView = view.findViewById(R.id.imgManzanaTest4)
        objetivo = view.findViewById(R.id.imageView3)


        manzanaList!!.add(draggableImg(mznTest,objetivo))
        manzanaList!!.add(draggableImg(mznTest3,objetivo))
        manzanaList!!.add(draggableImg(mznTest4,objetivo))

        for (item in manzanaList!!) {
            item.origen.setOnTouchListener(listener)
        }

        button.setOnClickListener(){
            Navigation.findNavController(view).navigate(R.id.action_fragment1_5_juego_to_fragment2_5_minijuego)
        }
        ajustes.setOnClickListener(){
            Navigation.findNavController(view).navigate(R.id.action_fragment1_5_juego_to_fragment4_menu)
        }
        return view
    }

    @SuppressLint("ClickableViewAccessibility")
    var listener = View.OnTouchListener { view, motionEvent ->
        val action = motionEvent.action
        when(action) {
            MotionEvent.ACTION_MOVE -> {
                view.y = motionEvent.rawY - view.height/2
                view.x = motionEvent.rawX - view.width/2
            }
            MotionEvent.ACTION_UP -> {
                view.x = motionEvent.rawX - view.width/2
                view.y = motionEvent.rawY - view.height/2
                println("**** MANZANA: "+ view.x + " // " + view.y)

                var posX = objetivo.getLeft()
                var posY = objetivo.getTop()
                var sizeX = objetivo.width
                var sizeY = objetivo.height
                println("**** OBJETIVO: "+ posX + " // " + posY)

                if ( (view.x + view.width/2) >= posX && (view.y + view.height/2) >= posY && (view.x + view.width/2) <= posX+sizeX && (view.y + view.height/2) <= posY+sizeY) {
                    view.visibility = View.GONE
                    Toast.makeText(requireContext(), "Haz metido la manzana en el cuadrado", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "ERROR AL METER LA MANZANA", Toast.LENGTH_SHORT).show()
                }
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
            Fragment1_5_juego().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}