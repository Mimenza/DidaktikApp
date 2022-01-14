package com.example.didaktikapp.fragments.minijuegos

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
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.Navigation
import com.example.didaktikapp.Model.DragnDropImage
import com.example.didaktikapp.R

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Fragment2_minijuego.newInstance] factory method to
 * create an instance of this fragment.
 */
class Fragment2_6_minijuego : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var globalView: View

    private var tiempoCompletarComprobacion = 1000

//    val listaImagenes = listOf(
//        listOf(R.id.imgV2_3CleanApple1,R.id.imgv2_3cesta),
//        listOf(R.id.imgV2_3CleanApple2,R.id.imgv2_3cesta),
//        listOf(R.id.imgV2_3CleanApple3,R.id.imgv2_3cesta),
//        listOf(R.id.imgV2_3CleanApple4,R.id.imgv2_3cesta),
//        listOf(R.id.imgV2_3CleanApple5,R.id.imgv2_3cesta),
//    )

    private var manzanaList: MutableList<DragnDropImage> = mutableListOf()

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
        val view = inflater.inflate(R.layout.fragment2_6_minijuego, container, false)
        globalView = view
        val button: Button = view.findViewById(R.id.btnf2_6siguiente)
        val ajustes: ImageButton = view.findViewById(R.id.btnf2_6ajustes)

        button.setOnClickListener(){
            Navigation.findNavController(view).navigate(R.id.action_fragment2_6_minijuego_to_fragment4_menu)
        }

        ajustes.setOnClickListener(){
            Navigation.findNavController(view).navigate(R.id.action_fragment2_6_minijuego_to_fragment4_menu)
        }

        generarVasonTarget()

        return view
    }

    private var cleanManzanaTimer: Boolean? = null

    @SuppressLint("ClickableViewAccessibility")
    var listener = View.OnTouchListener { viewElement, motionEvent ->
        var itemInList: DragnDropImage? = findItemByOrigen(viewElement)

        if (itemInList != null) {
            //if (!itemInList.acertado) {
            viewElement.bringToFront()
            val action = motionEvent.action
            when(action) {
                MotionEvent.ACTION_MOVE -> {
                    viewElement.x = motionEvent.rawX - viewElement.width/2
                    viewElement.y = motionEvent.rawY - viewElement.height/2
                    /*
                    //Aqui comprobamos si la manzana no esta limpia.
                    if (!itemInList.acertado) {
                        val aguaLocation = IntArray(2)
                        agua.getLocationOnScreen(aguaLocation);
                        //Posicion/tamaño del agua
                        var aguaPosX = aguaLocation[0]
                        var aguaPosY = aguaLocation[1]
                        var aguaSizeX = agua.width
                        var aguaSizeY = agua.height

                        // Si la imagen pasa por el agua, la manzana cambiara a limpia
                        if ( (viewElement.x + viewElement.width/2) >= aguaPosX && (viewElement.y + viewElement.height/2) >= aguaPosY && (viewElement.x + viewElement.width/2) <= aguaPosX+aguaSizeX && (viewElement.y + viewElement.height/2) <= aguaPosY+aguaSizeY) {

                            if (cleanManzanaTimer == null) {
                                cleanManzanaTimer = true
                                Handler(Looper.getMainLooper()).postDelayed({
                                    if (getView() != null && cleanManzanaTimer != null) {
                                        if ( (viewElement.x + viewElement.width/2) >= aguaPosX && (viewElement.y + viewElement.height/2) >= aguaPosY && (viewElement.x + viewElement.width/2) <= aguaPosX+aguaSizeX && (viewElement.y + viewElement.height/2) <= aguaPosY+aguaSizeY) {
                                            itemInList.acertado = true
                                            itemInList.origen.setImageResource(R.drawable.sagarraberdea)
                                            cleanManzanaTimer = null
                                        }
                                    }
                                }, tiempoCompletarComprobacion.toLong())
                            }
                        } else {
                            if (cleanManzanaTimer != null) {
                                cleanManzanaTimer = null
                            }
                        }
                    }

                     */
                }
                MotionEvent.ACTION_UP -> {
                    /*
                    if (itemInList.acertado) {
                        viewElement.x = motionEvent.rawX - viewElement.width / 2
                        viewElement.y = motionEvent.rawY - viewElement.height / 2
                        var objetivoEncontrado: View = itemInList!!.objetivo
                        val location = IntArray(2)
                        objetivoEncontrado.getLocationOnScreen(location);
                        var posX = location[0]
                        var posY = location[1]
                        var sizeX = objetivoEncontrado.width
                        var sizeY = objetivoEncontrado.height
                        if ((viewElement.x + viewElement.width / 2) >= posX && (viewElement.y + viewElement.height / 2) >= posY && (viewElement.x + viewElement.width / 2) <= posX + sizeX && (viewElement.y + viewElement.height / 2) <= posY + sizeY) {
                            viewElement.x = posX.toFloat()
                            viewElement.y = posY.toFloat()
                            viewElement.setOnTouchListener(null)
                        }
                    }
                    cleanManzanaTimer = null

                     */
                }
            }
        }
        true
    }

    private fun generarVasonTarget() {
        val constraintLayoutFound = globalView.findViewById<ConstraintLayout>(R.id.mainlayout_Minijuego6)
        // Generamos el vaso
        var imgVasoLleno: ImageView = ImageView(requireContext())

        constraintLayoutFound.addView(imgVasoLleno)
        imgVasoLleno.layoutParams.height = 200
        imgVasoLleno.layoutParams.width = 200
        //imgVasoLleno.x = ((0..globalView.width - 200).random()).toFloat()
        //imgVasoLleno.y = ((0..globalView.height/2 - 200).random()).toFloat()
        imgVasoLleno.x = (globalView.width - 200).toFloat()
        imgVasoLleno.y = (globalView.height/2).toFloat()
        imgVasoLleno.setImageResource(R.drawable.vaso6)

        // Generamos la Botella Vacia
        var imgBotellaVacia: ImageView = ImageView(requireContext())
        constraintLayoutFound.addView(imgBotellaVacia)
        imgBotellaVacia.layoutParams.height = 200
        imgBotellaVacia.layoutParams.width = 200
        imgBotellaVacia.x = (globalView.width/2 - 200/2).toFloat()
        imgBotellaVacia.y = (globalView.height/2 - 200/2).toFloat()
        imgBotellaVacia.setImageResource(R.drawable.botellallena)

        manzanaList.add(DragnDropImage(imgVasoLleno,imgBotellaVacia))

        imgVasoLleno.setOnTouchListener(listener)

    }

    private fun generarBotellaVacia() {

    }

    private fun actualizarBotellasLlenas() {

    }

    private fun actualizarVasosVacios() {

    }

    /*
    fun generarManzana() {
        var imgManzanaGenerada: ImageView = ImageView(requireContext())
        val constraintLayoutFound = globalView.findViewById<ConstraintLayout>(R.id.mainlayout_Minijuego2)
        constraintLayoutFound.addView(imgManzanaGenerada)
        imgManzanaGenerada.layoutParams.height = 200
        imgManzanaGenerada.layoutParams.width = 200
        imgManzanaGenerada.x = ((0..globalView.width - 200).random()).toFloat()
        imgManzanaGenerada.y = ((0..globalView.height/2 - 200).random()).toFloat()

        var tipoManzana = (0..1).random()
        var mznGnrDestino: ImageView
        if (tipoManzana == 1) {
            imgManzanaGenerada.setImageResource(R.drawable.manzanaroja)
            mznGnrDestino = cesta
        } else {
            imgManzanaGenerada.setImageResource(R.drawable.sagarraberdea)
            mznGnrDestino = cesta
        }
        manzanaList!!.add(DragnDropImage(imgManzanaGenerada,mznGnrDestino))

        imgManzanaGenerada.setOnTouchListener(listener)

        //OPTIONAL TO DO
        //val newAnimation: AnimatorSet = AnimatorSet()
        //newAnimation.stop

    }

     */

    private fun findItemByOrigen(view: View): DragnDropImage? {
        for (item in manzanaList!!) {
            if (item.origen == view) {
                return item
            }
        }
        return null
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Fragment2_minijuego.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Fragment2_6_minijuego().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}