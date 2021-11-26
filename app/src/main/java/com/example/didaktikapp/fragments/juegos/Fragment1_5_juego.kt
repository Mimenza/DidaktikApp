package com.example.didaktikapp.fragments.juegos

import android.annotation.SuppressLint
import android.graphics.Color
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
import androidx.constraintlayout.widget.ConstraintLayout
import java.util.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Fragment1_juego.newInstance] factory method to
 * create an instance of this fragment.
 */

//Objeto para gestionar facilmente los juegos que requieran drag and drop.
data class DragnDropImage(var origen: ImageView, var objetivo: ImageView, var acertado: Boolean = false)

class Fragment1_5_juego : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var button: Button
    private lateinit var myLayout: View

    private lateinit var globalView: View

    val listaImagenes = listOf(
        listOf(R.id.imgOrigenCamisa,R.id.imgObjetivoCamisa),
        listOf(R.id.imgOrigenCinturon,R.id.imgObjetivoCinturon),
        listOf(R.id.imgOrigenGorro,R.id.imgObjetivoGorro),
        listOf(R.id.imgOrigenManzana,R.id.imgObjetivoManzana),
        listOf(R.id.imgOrigenZapatos,R.id.imgObjetivoZapatos)
    )

    var manzanaList: MutableList<DragnDropImage>? = mutableListOf()
    //private lateinit var objetivo: ImageView

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
        globalView = view
        button = view.findViewById(R.id.btnf1_5_siguiente)
        button.visibility = View.GONE
        val ajustes: ImageButton = view.findViewById(R.id.btnf1_5_ajustes)
        //myLayout = view.findViewById(R.id.mainlayout)
        myLayout = view.findViewById<ConstraintLayout>(R.id.mainlayout)






        for (vItemList in listaImagenes) {
            var vItemOrigen: ImageView = view.findViewById(vItemList[0])
            var vItemDestino: ImageView = view.findViewById(vItemList[1])
            manzanaList!!.add(DragnDropImage(vItemOrigen,vItemDestino))
            vItemDestino.setColorFilter(Color.argb(150, 0, 80, 200))
            vItemOrigen.setOnTouchListener(listener)
        }

        button.setOnClickListener(){
            Navigation.findNavController(view).navigate(R.id.action_fragment1_5_juego_to_fragment2_5_minijuego)
        }
        ajustes.setOnClickListener(){
            Navigation.findNavController(view).navigate(R.id.action_fragment1_5_juego_to_fragment4_menu)
        }
        return view
    }

    var opX: Float = 0F
    var opY: Float = 0F

    @SuppressLint("ClickableViewAccessibility")
    var listener = View.OnTouchListener { viewElement, motionEvent ->
        var itemInList: DragnDropImage? = findItemByOrigen(viewElement)
        if (itemInList != null) {
            if (!itemInList.acertado) {
                val action = motionEvent.action
                when(action) {
                    MotionEvent.ACTION_DOWN -> {
                        opX = viewElement.x
                        opY = viewElement.y
                    }
                    MotionEvent.ACTION_MOVE -> {
                        viewElement.x = motionEvent.rawX - viewElement.width/2
                        viewElement.y = motionEvent.rawY - viewElement.height/2
                    }
                    MotionEvent.ACTION_UP -> {
                        viewElement.x = motionEvent.rawX - viewElement.width/2
                        viewElement.y = motionEvent.rawY - viewElement.height/2
                        var objetivoEncontrado: View = itemInList!!.objetivo
                        var posX = objetivoEncontrado.getLeft()
                        var posY = objetivoEncontrado.getTop()
                        var sizeX = objetivoEncontrado.width
                        var sizeY = objetivoEncontrado.height

                        if ( (viewElement.x + viewElement.width/2) >= posX && (viewElement.y + viewElement.height/2) >= posY && (viewElement.x + viewElement.width/2) <= posX+sizeX && (viewElement.y + viewElement.height/2) <= posY+sizeY) {
                            //viewElement.visibility = View.GONE
                            //viewElement.x = opX
                            //viewElement.y = opY
                            viewElement.x = posX.toFloat()
                            viewElement.y = posY.toFloat()
                            //Utils.drawLine(globalView, requireContext(),opX,opY,posX.toFloat(),posY.toFloat(),15F, (0..255).random(),(0..255).random(),(0..255).random())
                            itemInList.acertado = true
                            if (juegoCompletado()) {
                                button.visibility = View.VISIBLE
                                Toast.makeText(requireContext(), "FINALIZADO !!", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            }
        }
        true
    }

    private fun juegoCompletado(): Boolean {
        var finalizado: Boolean = true
        for (item in manzanaList!!) {
            if (!item.acertado) {
                finalizado = false
                break
            }
        }
        return finalizado
    }

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


