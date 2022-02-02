package com.example.didaktikapp.fragments.minijuegos

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.animation.AnimationUtils
import android.view.animation.TranslateAnimation
import android.widget.*
import androidx.core.view.isVisible
import androidx.navigation.Navigation
import com.example.didaktikapp.R
import com.example.didaktikapp.activities.Activity5_Mapa
import com.example.didaktikapp.activities.Activity6_Site
import com.example.didaktikapp.activities.DbHandler


class Fragment2_5_minijuego : Fragment(), DbHandler.QueryResponseDone {

    private var acierto: Int = 0

    private lateinit var vaso: ImageView
    private lateinit var vistaAnimada:TranslateAnimation

    private lateinit var txtcartel: TextView
    private lateinit var cartel: ImageView
    private lateinit var btnsiguiente:Button
    private lateinit var btnrepetir:Button
    private lateinit var btninfominijuego: ImageButton


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment2_5_minijuego, container, false)

        vaso  = view.findViewById(R.id.imgv2_5vaso)
        cartel= view.findViewById((R.id.imgv2_5cartelmadera))
        txtcartel= view.findViewById((R.id.txtv2_5carteltexto))
        btnrepetir = view.findViewById(R.id.btn2_5_repetir)
        btnsiguiente = view.findViewById(R.id.btn2_5_siguiente)
        btninfominijuego= view.findViewById((R.id.btn2_5_infominijuego))

        btninfominijuego.setOnClickListener(){
            showDialogInfo()
        }



        val manzana1: ImageView = view.findViewById(R.id.imgv2_5_applepiece1)
        val manzana2: ImageView = view.findViewById(R.id.imgv2_5_applepiece2)
        val manzana3: ImageView = view.findViewById(R.id.imgv2_5_applepiece3)
        val manzana4: ImageView = view.findViewById(R.id.imgv2_5_applepiece4)
        val manzana5: ImageView = view.findViewById(R.id.imgv2_5_applepiece5)

        val mapa: ImageButton = view.findViewById(R.id.btnf2_5_mapa)
        mapa.setOnClickListener {
            activity?.let{
                getActivity()?.finish()
            }

        }

        manzana1.setOnClickListener() { machacar(manzana1) }
        manzana2.setOnClickListener() { machacar(manzana2) }
        manzana3.setOnClickListener() { machacar(manzana3) }
        manzana4.setOnClickListener() { machacar(manzana4) }
        manzana5.setOnClickListener() { machacar(manzana5) }


        return view
    }


    fun showDialogInfo(){

        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.info_dialog)
        dialog.show()
        dialog.window!!.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )


        val textInfo = dialog.findViewById<View>(R.id.txtv_infominijuego) as TextView
        var texto =""
        //Recojemos datos de shared preferences
        val sharedPreferences = this.activity?.getSharedPreferences("site", 0)
        val numero = sharedPreferences?.getString("numero", null)?.toInt()
        println(numero)
        when(numero){

            0->  {texto=resources.getString(R.string.ayudaminijuego1)}
            1->  {texto=resources.getString(R.string.ayudaminijuego2)}
            2->  {texto=resources.getString(R.string.ayudaminijuego3)}
            3->  {texto=resources.getString(R.string.ayudaminijuego4)}
            4->  {texto=resources.getString(R.string.ayudaminijuego5)}
            5->  {texto=resources.getString(R.string.ayudaminijuego6)}

        }
        println(texto)
        if (textInfo!=null){

            textInfo.setText(texto)
        }
    }

    fun machacar(manzana: ImageView) {

        println(manzana.id)
        //declaramos variable a 0
        var pulsaciones: Int = 0
        zoom(manzana)
        println(pulsaciones)

        manzana.setOnClickListener() {
            if (pulsaciones < 4) {
                zoom(manzana)
                pulsaciones++
            }

            if (pulsaciones == 4) {
                disapear(manzana)

                checkProgress()
            }
        }


    }

    fun checkProgress() {
        println("check" + acierto)
        if (acierto == 5) {
            DbHandler.userAumentarPuntuacion(5)
            DbHandler().requestDbUserUpdate(this)
            starAnimationfun()
        }
    }

    fun starAnimationfun(){

        //Diseñar cartel madera
        btnsiguiente.visibility = View.VISIBLE
        btnrepetir.visibility = View.VISIBLE
        cartel.visibility=View.VISIBLE
        txtcartel.visibility=View.VISIBLE



        vistaAnimada = TranslateAnimation(-1000f, 0f, 0f, 0f)
        vistaAnimada.duration = 1000

        cartel.startAnimation(vistaAnimada)
        btnrepetir.startAnimation(vistaAnimada)
        btnsiguiente.startAnimation(vistaAnimada)
        txtcartel.startAnimation(vistaAnimada)

        btnsiguiente.setOnClickListener(){
            getActivity()?.finish()
        }

        btnrepetir.setOnClickListener(){
            Navigation.findNavController(it).navigate(R.id.action_fragment2_5_minijuego_self)
        }

    }
    fun zoom(manzana: ImageView) {
        println("ZOOM")
        val zoom = AnimationUtils.loadAnimation(context, R.anim.zoom)
        manzana.startAnimation(zoom)
    }

    fun disapear(manzana: ImageView) {
        manzana.setOnClickListener() {}
        println("DISAPEAR")
        manzana.setImageResource(R.drawable.manchaverde)
        val disapear = AnimationUtils.loadAnimation(context, R.anim.disapear)
        manzana.startAnimation(disapear)
        manzana.isVisible = false
        changeVaso(manzana)
        acierto++

    }

    fun changeVaso(manzana: ImageView){
        manzana.setOnClickListener() {}

        when (acierto) {
            0 ->{vaso.setImageResource(R.drawable.vaso2) }
            1 ->{vaso.setImageResource(R.drawable.vaso3) }
            2 ->{vaso.setImageResource(R.drawable.vaso4) }
            3 ->{vaso.setImageResource(R.drawable.vaso5) }
            4 ->{vaso.setImageResource(R.drawable.vaso6) }

            else -> {
                print("x is neither 1 nor 2")
            }
        }

    }

}