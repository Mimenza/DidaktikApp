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
import com.example.didaktikapp.activities.DbHandler
import com.example.didaktikapp.activities.Utils


class Fragment2_2_minijuego : Fragment(), DbHandler.QueryResponseDone {

    private lateinit var vistaAnimada:TranslateAnimation
    private var acierto:Int = 0
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
        val view= inflater.inflate(R.layout.fragment2_2_minijuego, container, false)


        val manzana1:ImageView = view.findViewById(R.id.imgv2_2_manzanav)
        val manzana2:ImageView = view.findViewById(R.id.imgv2_2_manzanav1)
        val manzana3:ImageView = view.findViewById(R.id.imgv2_2_manzanav2)
        val manzana4:ImageView = view.findViewById(R.id.imgv2_2_manzanav3)
        val manzana5:ImageView = view.findViewById(R.id.imgv2_2_manzanav4)

        val manzana6:ImageView = view.findViewById(R.id.imgv2_2_manzanav5)
        val manzana7:ImageView = view.findViewById(R.id.imgv2_2_manzanav6)
        val manzana8:ImageView = view.findViewById(R.id.imgv2_2_manzanav7)
        val manzana9:ImageView = view.findViewById(R.id.imgv2_2_manzanav8)
        val manzana10:ImageView = view.findViewById(R.id.imgv2_2_manzanav9)



        cartel= view.findViewById((R.id.imgv2_2cartelmadera))
        txtcartel= view.findViewById((R.id.txtv2_2carteltexto))
        btnrepetir = view.findViewById(R.id.btn2_2_repetir)
        btnsiguiente = view.findViewById(R.id.btn2_2_siguiente)
        btninfominijuego= view.findViewById((R.id.btn2_2_infominijuego))

        val mapa: ImageButton = view.findViewById(R.id.btnf2_2_mapa)
        mapa.setOnClickListener {
                activity?.let{
                    getActivity()?.finish()
                }

        }

        btninfominijuego.setOnClickListener(){
            showDialogInfo()
        }

        manzana1.setOnClickListener(){desaparecer(manzana1)}
        manzana2.setOnClickListener(){desaparecer(manzana2)}
        manzana3.setOnClickListener(){desaparecer(manzana3)}
        manzana4.setOnClickListener(){desaparecer(manzana4)}
        manzana5.setOnClickListener(){desaparecer(manzana5)}

        manzana6.setOnClickListener(){vibrar()}
        manzana7.setOnClickListener(){vibrar()}
        manzana8.setOnClickListener(){vibrar()}
        manzana9.setOnClickListener(){vibrar()}
        manzana10.setOnClickListener(){vibrar()}

        return view
    }
    /**
     * Recogemos del shared preferences en que minijuego estamos y depende de cual sea muestra una
     * info de ayuda u otra
     *
     */
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

    /**
     * Una vez hemos clickado en una manzana, esta desaparece,
     * despues miramos si hemos compleado el juego o todavia quedan manzanas por seleccionar
     *
     * @param manzana manzana en la que hemos clickado
     */
    fun desaparecer(manzana:ImageView){

        val aniFade = AnimationUtils.loadAnimation(context, R.anim.disapear)
        manzana.startAnimation(aniFade)
        manzana.isVisible = false
        acierto++


        manzana.setOnClickListener(){}

        checkProgress()

    }

    /**
     * Si hemos cometido un error el movil vibra
     *
     */
    private fun vibrar(){
        Utils.vibrarTelefono(requireContext())
    }

    /**
     * Miramos si hemos clickado en todas las manzanas o todabia quedan manzanas
     *
     */
    fun checkProgress(){

        if(acierto>=5){
            DbHandler.userAumentarPuntuacion(5)
            DbHandler().requestDbUserUpdate(this)
            starAnimationfun()

        }
    }

    /**
     * Animacion de cierre del minijuego, generamos un cartel con un texto y dos botones
     *
     */
    fun starAnimationfun() {

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
            Navigation.findNavController(it).navigate(R.id.action_fragment2_2_minijuego_self)
        }

    }

}