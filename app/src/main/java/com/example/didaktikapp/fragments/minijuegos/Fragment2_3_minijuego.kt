package com.example.didaktikapp.fragments.minijuegos

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.*
import androidx.navigation.Navigation
import com.example.didaktikapp.Model.DragnDropImage
import com.example.didaktikapp.R
import com.example.didaktikapp.activities.DbHandler
import org.w3c.dom.Text

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
lateinit var agua:ImageView

var manzanaList: MutableList<DragnDropImage>? = mutableListOf()

/**
 * A simple [Fragment] subclass.
 * Use the [Fragment2_minijuego.newInstance] factory method to
 * create an instance of this fragment.
 */
class Fragment2_3_minijuego : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    var aciertosActuales: Int = 0
    private lateinit var globalView: View
    var minijuegoFinalizado: Boolean = false
    lateinit var  button:Button
    var acertados:Int = 0
    private var tiempoCompletarComprobacion = 1000

    val listaImagenes = listOf(
        listOf(R.id.imgV2_3CleanApple1,R.id.imgv2_3cesta),
        listOf(R.id.imgV2_3CleanApple2,R.id.imgv2_3cesta),
        listOf(R.id.imgV2_3CleanApple3,R.id.imgv2_3cesta),
        listOf(R.id.imgV2_3CleanApple4,R.id.imgv2_3cesta),
        listOf(R.id.imgV2_3CleanApple5,R.id.imgv2_3cesta),

        //listOf(R.id.puzzle_pieza_otest1,R.id.puzzle_pieza_otest2),
    )
    private lateinit var aciertosTxt:TextView

    var manzanaList: MutableList<DragnDropImage>? = mutableListOf()

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
        val view= inflater.inflate(R.layout.fragment2_3_minijuego, container, false)

        button = view.findViewById(R.id.btnf2_3_1siguiente)
        val ajustes: ImageButton = view.findViewById(R.id.btnf2_3_1ajustes)

        //agua  = view.findViewById((R.id.imgv2_3agua))

        button.setOnClickListener(){
            Navigation.findNavController(view).navigate(R.id.action_fragment2_3_minijuego_to_fragment4_menu)
        }

        ajustes.setOnClickListener(){
            Navigation.findNavController(view).navigate(R.id.action_fragment2_3_minijuego_to_fragment4_menu)
        }

        globalView = view

        view.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                view.viewTreeObserver.removeOnGlobalLayoutListener(this)

                prepairApples()
            }
        })

        return view
    }
    fun prepairApples() {
        for (vItemList in listaImagenes) {
            var vItemOrigen: ImageView = globalView.findViewById(vItemList[0])
            var vItemDestino: ImageView = globalView.findViewById(vItemList[1])
            manzanaList!!.add(DragnDropImage(vItemOrigen,vItemDestino))
            vItemOrigen.setOnTouchListener(listener)
        }
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
                    }
                MotionEvent.ACTION_UP -> {
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
                }
            }
        }
        true
    }

    private fun comprobarInsercionManzana(item: DragnDropImage, objetivoInsertado: ImageView) {
        //item.acertado = true
        if (item.objetivo == objetivoInsertado) {
            aciertosActuales++

            println(aciertosActuales)
        }
        comprobarJuegoFinalizado()
    }



    private fun findItemByOrigen(view: View): DragnDropImage? {
        for (item in manzanaList!!) {
            if (item.origen == view) {
                return item
            }
        }
        return null
    }

    private fun removeManzanasListener() {
        for (item in manzanaList!!) {
            item.origen.setOnTouchListener(null)
        }
    }

    private fun comprobarJuegoFinalizado() {
        if (aciertosActuales >= 5) {
            button.visibility = View.VISIBLE
            minijuegoFinalizado = true
            Toast.makeText(requireContext(), "ZORIONAK !!", Toast.LENGTH_SHORT).show()
            removeManzanasListener()
        }
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
            Fragment2_3_minijuego().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}