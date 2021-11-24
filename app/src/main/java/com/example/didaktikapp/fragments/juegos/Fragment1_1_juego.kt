package com.example.didaktikapp.fragments.juegos

import `in`.codeshuffle.typewriterview.TypeWriterView
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
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import kotlinx.android.synthetic.main.fragment1_1_juego.*
import kotlinx.android.synthetic.main.fragment4_menu.*

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

    var progress: Int = 0
    var pressedImg: Int = 0

    var point1: Boolean = false
    var point2: Boolean = false
    var point3: Boolean = false

    var border1: Boolean = false
    var border2: Boolean = false
    var border3: Boolean = false

    lateinit var img1: ImageView
    lateinit var img2: ImageView
    lateinit var img3: ImageView

    lateinit var txtv1: TextView
    lateinit var txtv2: TextView
    lateinit var txtv3: TextView

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
            Navigation.findNavController(view)
                .navigate(R.id.action_fragment1_1_juego_to_fragment2_1_minijuego)
        }

        ajustes.setOnClickListener() {
            Navigation.findNavController(view)
                .navigate(R.id.action_fragment1_1_juego_to_fragment4_menu)
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

        img1 = view.findViewById<ImageView>(R.id.imgv1_1imagen1)
        img2 = view.findViewById<ImageView>(R.id.imgv1_1imagen2)
        img3 = view.findViewById<ImageView>(R.id.imgv1_1imagen3)

        txtv1 = view.findViewById<TextView>(R.id.txtv1_1azalpena1)
        txtv2 = view.findViewById<TextView>(R.id.txtv1_1azalpena2)
        txtv3 = view.findViewById<TextView>(R.id.txtv1_1azalpena3)

        img1.setOnClickListener() {
            pressedImg = 1

            if (!point1) {
                if (!border1) {
                    setBorder(img1, "black")
                    border1 = true
                } else {
                    unsetBorder(img1)
                    border1 = false
                }
            }

            if (!point2) {
                unsetBorder(img2)
                border2 = false
            }

            if (!point3) {
                unsetBorder(img3)
                border3 = false
            }
        }

        img2.setOnClickListener() {
            pressedImg = 2
            println(pressedImg)

            if (!point2) {
                if (!border2) {
                    setBorder(img2, "black")
                    border2 = true
                } else {
                    unsetBorder(img2)
                    border2 = false
                }
            }

            if (!point1) {
                unsetBorder(img1)
                border1 = false
            }

            if (!point3) {
                unsetBorder(img3)
                border3 = false
            }
        }

        img3.setOnClickListener() {
            pressedImg = 3
            println(pressedImg)

            if (!point3) {
                if (!border3) {
                    setBorder(img3, "black")
                    border3 = true
                } else {
                    unsetBorder(img3)
                    border3 = false
                }
            }

            if (!point1) {
                unsetBorder(img1)
                border1 = false
            }

            if (!point2) {
                unsetBorder(img2)
                border2 = false
            }
        }

        txtv1.setOnClickListener() {
            if (pressedImg == 2) {
                if (!point2) {
                    progress++
                    point2 = true
                    setBorder(img2, "green")
                    setBorder(txtv1, "green")
                    checkProgress(view, progress)
                }
            } else {
                resetGame()
            }
        }

        txtv2.setOnClickListener() {
            if (pressedImg == 3) {
                if (!point3) {
                    progress++
                    point3 = true
                    setBorder(img3, "green")
                    setBorder(txtv2, "green")
                    checkProgress(view, progress)
                }
            } else {
                resetGame()
            }
        }

        txtv3.setOnClickListener() {
            if (pressedImg == 1) {
                if (!point1) {
                    progress++
                    point1 = true
                    setBorder(img1, "green")
                    setBorder(txtv3, "green")
                    checkProgress(view, progress)
                }
            } else {
                resetGame()
            }
        }

        return view
    }

    private fun setBorder(img: ImageView, color: String) {
        var colorId = 0
        when(color){
            "black" -> colorId = R.drawable.bg_border_black
            "green" -> colorId = R.drawable.bg_border_green
        }

        img.setBackground(context?.let { it ->
            ContextCompat.getDrawable(it, colorId)
        })
    }

    private fun setBorder(txtv: TextView, color: String) {
        var colorId = 0
        when(color){
            "black" -> colorId = R.drawable.bg_border_black
            "green" -> colorId = R.drawable.bg_border_green
        }

        txtv.setBackground(context?.let { it ->
            ContextCompat.getDrawable(it, colorId)
        })
    }

    private fun unsetBorder(img: ImageView) {
        img.setBackgroundResource(0)
    }

    private fun unsetBorder(txtv: TextView) {
        txtv.setBackgroundResource(0)
    }

    private fun resetGame() {
        progress = 0
        pressedImg = 0

        unsetBorder(img1)
        unsetBorder(img2)
        unsetBorder(img3)

        unsetBorder(txtv1)
        unsetBorder(txtv2)
        unsetBorder(txtv3)

        point1 = false
        point2 = false
        point3 = false

        border1 = false
        border2 = false
        border3 = false
    }

    private fun checkProgress(view: View, progress: Int) {
        if (progress == 3) {
            val btnNext: Button = view.findViewById<Button>(R.id.btnf1_1siguiente)

            btnNext.isVisible = true
        }
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