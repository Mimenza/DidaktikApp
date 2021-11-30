package com.example.didaktikapp.fragments

import `in`.codeshuffle.typewriterview.TypeWriterView
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.didaktikapp.R
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.navigation.Navigation
import com.example.didaktikapp.activities.Activity5_Mapa
import kotlinx.android.synthetic.main.activity5_mapa.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Fragment3_info.newInstance] factory method to
 * create an instance of this fragment.
 */
class Fragment3_info : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var audio: MediaPlayer? = null

    // private lateinit var button_back:ImageView
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

        val view = inflater.inflate(R.layout.fragment3_info, container, false)


        val sharedPreferences = this.activity?.getSharedPreferences("site", 0)
        val numero = sharedPreferences?.getString("numero", null)?.toInt()


        val tvTitulo: TextView? = view?.findViewById<TextView>(R.id.txtv3f_nombrezona)
        val imgvImagen: ImageView? = view?.findViewById<ImageView>(R.id.imgv3f_fotozona)
        val tvDescripcion: TextView? = view?.findViewById<TextView>(R.id.txtv3f_textozona)

        var titulo: String = ""
        var imagen: String = ""
        var descripcion: String = ""
        val button: Button = view.findViewById(R.id.btn3f_jugar)
        val buttonBackToMap: ImageView = view.findViewById(R.id.imgv3f_backtomap)

        buttonBackToMap.setOnClickListener{
            activity?.let{
                val intent = Intent (it, Activity5_Mapa::class.java)
                it.startActivity(intent)
            }
            //Ocultamos el tutorial, para que no salga siempre solo en la primera vez

        }

        when (numero) {
            0 -> {
                titulo = resources.getString(R.string.titulo1_juego)
                imagen = R.drawable.img_sagardoetxea.toString()
                descripcion = resources.getString(R.string.text_1juego)

                button.setOnClickListener {
                    Navigation.findNavController(view)
                        .navigate(R.id.action_fragment3_info_to_fragment1_1_juego)

                }
            }
            1-> {
                titulo = resources.getString(R.string.titulo2_juego)
                imagen = R.drawable.img_murgiajauregia.toString()
                descripcion = resources.getString(R.string.text_2juego)

                button.setOnClickListener {
                    Navigation.findNavController(view)
                        .navigate(R.id.action_fragment3_info_to_fragment1_2_juego)

                }
            }
            2-> {
                titulo = resources.getString(R.string.titulo31_juego)
                imagen = R.drawable.img_foruplaza.toString()
                descripcion = resources.getString(R.string.text_31juego)

                button.setOnClickListener {
                    Navigation.findNavController(view)
                        .navigate(R.id.action_fragment3_info_to_fragment1_3_1_juego)

                }
            }
            3-> {
                titulo = resources.getString(R.string.titulo32_juego)
                imagen = R.drawable.img_foruplaza2.toString()
                descripcion = resources.getString(R.string.text_32juego)

                button.setOnClickListener {
                    Navigation.findNavController(view)
                        .navigate(R.id.action_fragment3_info_to_fragment1_3_2_juego)

                }
            }
            4-> {
                titulo = resources.getString(R.string.titulo4_juego)
                imagen = R.drawable.img_astigarelkartea.toString()
                descripcion = resources.getString(R.string.text_4juego)

                button.setOnClickListener {
                    Navigation.findNavController(view)
                        .navigate(R.id.action_fragment3_info_to_fragment1_4_juego)

                }
            }
            5-> {
                titulo = resources.getString(R.string.titulo5_juego)
                imagen = R.drawable.img_ipintzasagardotegia.toString()
                descripcion = resources.getString(R.string.text_5juego)

                button.setOnClickListener {
                    Navigation.findNavController(view)
                        .navigate(R.id.action_fragment3_info_to_fragment1_5_juego)

                }
                audioTutorialZona6()
            }
            6-> {
                titulo = resources.getString(R.string.titulo6_juego)
                imagen = R.drawable.img_rezolasagardotegia.toString()
                descripcion = resources.getString(R.string.text_6juego)

                button.setOnClickListener {
                    Navigation.findNavController(view)
                        .navigate(R.id.action_fragment3_info_to_fragment1_6_juego)

                }

                audioTutorialZona7()
            }
        }

        if (tvTitulo != null) {
            tvTitulo.setText(titulo)
        }
        if (imgvImagen != null) {
            imgvImagen.setBackgroundResource(imagen.toInt())
        }
        if (tvDescripcion != null) {
            tvDescripcion.setText(descripcion)
        }
        return view
    }



    fun audioTutorialZona6(){

        //Audio zona 6 info
        runBlocking {
            launch {
                audio = MediaPlayer.create(context, R.raw.juego6audio)
                audio?.start()

            }
        }

    }

    fun audioTutorialZona7(){


        //Audio zona 7 info
        runBlocking {
            launch {
                audio = MediaPlayer.create(context, R.raw.juego7audio)
                audio?.start()

            }


        }

    }



    override fun onDestroy() {
        audio?.stop()
        super.onDestroy()
    }

    override fun onStop() {
        audio?.stop()
        super.onStop()
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Fragment3_info.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Fragment3_info().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}