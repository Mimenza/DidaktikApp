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

    val listaOrigen = listOf(R.id.imgOrigenCamisa, R.id.imgOrigenCinturon, R.id.imgOrigenGorro, R.id.imgOrigenManzana, R.id.imgOrigenZapatos)
    val listaObjetivos = listOf(R.id.imgObjetivoCamisa, R.id.imgObjetivoCinturon, R.id.imgObjetivoGorro, R.id.imgObjetivoManzana, R.id.imgObjetivoZapatos)

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
        button = view.findViewById(R.id.btnf1_5_siguiente)
        button.visibility = View.GONE
        val ajustes: ImageButton = view.findViewById(R.id.btnf1_5_ajustes)


        for (vItemList in listaImagenes) {
            var vItemOrigen: ImageView = view.findViewById(vItemList[0])
            var vItemDestino: ImageView = view.findViewById(vItemList[1])
            manzanaList!!.add(DragnDropImage(vItemOrigen,vItemDestino))
            vItemDestino.setColorFilter(Color.argb(150, 0, 80, 200))
            vItemOrigen.setOnTouchListener(listener)
        }
        /*

        //Imagenes de Origen (Estas van a ser draggables)
        val imgOrigenCamisa: ImageView = view.findViewById(R.id.imgOrigenCamisa)
        val imgOrigenCinturon: ImageView = view.findViewById(R.id.imgOrigenCinturon)
        val imgOrigenGorro: ImageView = view.findViewById(R.id.imgOrigenGorro)
        val imgOrigenManzana: ImageView = view.findViewById(R.id.imgOrigenManzana)
        val imgOrigenZapatos: ImageView = view.findViewById(R.id.imgOrigenZapatos)

        //Imageens de destino (Estas van a ser estaticas para indicar al usuario donde deben ir)
        val imgObjetivoCamisa: ImageView = view.findViewById(R.id.imgObjetivoCamisa)
        val imgObjetivoCinturon: ImageView = view.findViewById(R.id.imgObjetivoCinturon)
        val imgObjetivoGorro: ImageView = view.findViewById(R.id.imgObjetivoGorro)
        val imgObjetivoManzana: ImageView = view.findViewById(R.id.imgObjetivoManzana)
        val imgObjetivoZapatos: ImageView = view.findViewById(R.id.imgObjetivoZapatos)

        manzanaList!!.add(DragnDropImage(imgOrigenCamisa,imgObjetivoCamisa))
        manzanaList!!.add(DragnDropImage(imgOrigenCinturon,imgObjetivoCinturon))
        manzanaList!!.add(DragnDropImage(imgOrigenGorro,imgObjetivoGorro))
        manzanaList!!.add(DragnDropImage(imgOrigenManzana,imgObjetivoManzana))
        manzanaList!!.add(DragnDropImage(imgOrigenZapatos,imgObjetivoZapatos))

        for (item in manzanaList!!) {
            item.origen.setOnTouchListener(listener)
            item.objetivo.setColorFilter(Color.argb(150, 0, 80, 200))
        }

         */

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
        var itemInList: DragnDropImage? = findItemByOrigen(view)
        if (itemInList != null) {
            if (!itemInList.acertado) {
                val action = motionEvent.action
                when(action) {
                    MotionEvent.ACTION_MOVE -> {
                        view.y = motionEvent.rawY - view.height/2
                        view.x = motionEvent.rawX - view.width/2
                    }
                    MotionEvent.ACTION_UP -> {
                        view.x = motionEvent.rawX - view.width/2
                        view.y = motionEvent.rawY - view.height/2
                        var objetivoEncontrado: View = itemInList!!.objetivo
                        var posX = objetivoEncontrado.getLeft()
                        var posY = objetivoEncontrado.getTop()
                        var sizeX = objetivoEncontrado.width
                        var sizeY = objetivoEncontrado.height

                        if ( (view.x + view.width/2) >= posX && (view.y + view.height/2) >= posY && (view.x + view.width/2) <= posX+sizeX && (view.y + view.height/2) <= posY+sizeY) {
                            //view.visibility = View.GONE
                            view.x = posX.toFloat()
                            view.y = posY.toFloat()
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