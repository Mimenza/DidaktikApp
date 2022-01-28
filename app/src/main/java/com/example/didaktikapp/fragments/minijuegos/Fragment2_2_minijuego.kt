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

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Fragment2_minijuego.newInstance] factory method to
 * create an instance of this fragment.
 */
class Fragment2_2_minijuego : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var aciertoTxt:TextView
    private lateinit var vistaAnimada:TranslateAnimation
    private var acierto:Int = 0
    private lateinit var txtcartel: TextView
    private lateinit var cartel: ImageView
    private lateinit var btnsiguiente:Button
    private lateinit var btnrepetir:Button
    private lateinit var btninfominijuego: ImageButton

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
        val view= inflater.inflate(R.layout.fragment2_2_minijuego, container, false)

        val ajustes: ImageButton = view.findViewById(R.id.btnf2_2ajustes)


        ajustes.setOnClickListener() {
            (activity as Activity6_Site?)?.menuCheck()

        }

        val manzana1:ImageView = view.findViewById(R.id.imgv2_2_manzanav)
        val manzana2:ImageView = view.findViewById(R.id.imgv2_2_manzanav1)
        val manzana3:ImageView = view.findViewById(R.id.imgv2_2_manzanav2)
        val manzana4:ImageView = view.findViewById(R.id.imgv2_2_manzanav3)
        val manzana5:ImageView = view.findViewById(R.id.imgv2_2_manzanav4)


        cartel= view.findViewById((R.id.imgv2_2cartelmadera))
        txtcartel= view.findViewById((R.id.txtv2_2carteltexto))
        btnrepetir = view.findViewById(R.id.btn2_2_repetir)
        btnsiguiente = view.findViewById(R.id.btn2_2_siguiente)
        btninfominijuego= view.findViewById((R.id.btn2_2_infominijuego))

        btninfominijuego.setOnClickListener(){
            showDialogInfo()
        }

        manzana1.setOnClickListener(){desaparecer(manzana1)}
        manzana2.setOnClickListener(){desaparecer(manzana2)}
        manzana3.setOnClickListener(){desaparecer(manzana3)}
        manzana4.setOnClickListener(){desaparecer(manzana4)}
        manzana5.setOnClickListener(){desaparecer(manzana5)}
        return view
    }

    fun showDialogInfo(){

        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.info)
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

    fun desaparecer(manzana:ImageView){

        val aniFade = AnimationUtils.loadAnimation(context, R.anim.disapear)
        manzana.startAnimation(aniFade)
        manzana.isVisible = false
        acierto++


        manzana.setOnClickListener(){}

        checkProgress()

    }

    fun checkProgress(){

        if(acierto==5){

            starAnimationfun()

        }
    }
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
            val i = Intent(activity, Activity5_Mapa::class.java)
            startActivity(i)
            (activity as Activity?)!!.overridePendingTransition(0, 0)
        }

        btnrepetir.setOnClickListener(){
            Navigation.findNavController(it).navigate(R.id.action_fragment2_2_minijuego_self)
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
            Fragment2_2_minijuego().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}