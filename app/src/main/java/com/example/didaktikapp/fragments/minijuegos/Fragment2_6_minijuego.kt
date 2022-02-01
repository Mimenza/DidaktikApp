package com.example.didaktikapp.fragments.minijuegos

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.*
import android.view.animation.TranslateAnimation
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.Navigation
import com.example.didaktikapp.Model.DragnDropImageLevel
import com.example.didaktikapp.R
import com.example.didaktikapp.activities.Activity5_Mapa
import com.example.didaktikapp.activities.DbHandler


class Fragment2_6_minijuego : Fragment(), DbHandler.QueryResponseDone {

    private var acierto: Int = 0
    private lateinit var globalView: View

    private lateinit var vistaAnimada:TranslateAnimation
    private lateinit var txtcartel: TextView
    private lateinit var cartel: ImageView
    private lateinit var btnsiguiente:Button
    private lateinit var btnrepetir:Button
    private lateinit var btninfominijuego: ImageButton
    private var tiempoCompletarComprobacion = 1000


    private var manzanaList: MutableList<DragnDropImageLevel> = mutableListOf()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment2_6_minijuego, container, false)
        globalView = view


        cartel= view.findViewById((R.id.imgv2_6cartelmadera))
        txtcartel= view.findViewById((R.id.txtv2_6carteltexto))
        btnrepetir = view.findViewById(R.id.btn2_6_repetir)
        btnsiguiente = view.findViewById(R.id.btn2_6_siguiente)
        btninfominijuego= view.findViewById((R.id.btn2_6_infominijuego))


        val mapa: ImageButton = view.findViewById(R.id.btnf2_6_mapa)
        mapa.setOnClickListener {
            activity?.let{
                val intent = Intent (it, Activity5_Mapa::class.java)
                it.startActivity(intent)
            }

        }
        btninfominijuego.setOnClickListener(){
            showDialogInfo()
        }
        view.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                view.viewTreeObserver.removeOnGlobalLayoutListener(this)
                generarVasonTarget()
            }
        })



        return view
    }

    private var timerActive: Boolean? = null
    private var updateHandler: Handler? = null

    private fun itemActionCallback(pItem: DragnDropImageLevel) {
        //pItem.acertado = true
        //pItem.origen.setImageResource(R.drawable.vaso1)
        pItem.nivel = pItem.nivel - 1
        when(pItem.nivel) {
            4 -> {
                pItem.origen.setImageResource(R.drawable.vaso5)
                pItem.objetivo.setImageResource(R.drawable.mjuego5_botella2)
            }
            3 -> {
                pItem.origen.setImageResource(R.drawable.vaso4)
                pItem.objetivo.setImageResource(R.drawable.mjuego5_botella3)
            }
            2 -> {
                pItem.origen.setImageResource(R.drawable.vaso3)
                pItem.objetivo.setImageResource(R.drawable.mjuego5_botella4)
            }
            1 -> {
                pItem.origen.setImageResource(R.drawable.vaso2)
                pItem.objetivo.setImageResource(R.drawable.mjuego5_botella5)
            }
            else -> {
                pItem.origen.setImageResource(R.drawable.vaso1)
                pItem.objetivo.setImageResource(R.drawable.mjuego5_botella1)
                pItem.acertado = true
                vasoVaciado()
                pItem.origen.setOnTouchListener(null)
                pItem.origen.visibility = View.GONE
                pItem.corcho.setOnTouchListener(listener)
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    var listener = View.OnTouchListener { viewElement, motionEvent ->
        var itemInList: DragnDropImageLevel? = null
        itemInList = findItemByOrigen(viewElement)
        if (null == itemInList) {
            itemInList = findItemByCorcho(viewElement)
        }

        println("x = ${viewElement.x} | y = ${viewElement.y}")

       // if (itemInList != null && !itemInList.acertado) {
            viewElement.bringToFront()

            var objetivoEncontrado: View = itemInList!!.objetivo
            val location = IntArray(2)
            objetivoEncontrado.getLocationOnScreen(location)
            var posX = location[0]
            var posY = location[1]
            var sizeX = objetivoEncontrado.width
            var sizeY = objetivoEncontrado.height

            val action = motionEvent.action
            when(action) {
                MotionEvent.ACTION_MOVE -> {
                    viewElement.x = motionEvent.rawX - viewElement.width/2
                    viewElement.y = motionEvent.rawY - viewElement.height/2

                    if ((viewElement.x + viewElement.width / 2) >= posX && (viewElement.y + viewElement.height / 2) >= posY && (viewElement.x + viewElement.width / 2) <= posX + sizeX && (viewElement.y + viewElement.height / 2) <= posY + sizeY) {

                        if (!itemInList.acertado) {
                            if (updateHandler == null && itemInList.nivel > 0) {
                                updateHandler = Handler()
                                updateHandler?.postDelayed({
                                    itemActionCallback(itemInList)
                                    updateHandler?.removeCallbacksAndMessages(null)
                                    updateHandler = null
                                }, 1000)
                            }
                        }
                    } else {
                        if (!itemInList.acertado) {
                            if (updateHandler != null) {
                                updateHandler?.removeCallbacksAndMessages(null)
                                updateHandler = null
                            }
                        }

                    }


                }
                MotionEvent.ACTION_UP -> {

                    if (itemInList.acertado) {
                        if ((viewElement.x + viewElement.width / 2) >= posX && (viewElement.y + viewElement.height / 2) >= posY && (viewElement.x + viewElement.width / 2) <= posX + sizeX && (viewElement.y + viewElement.height / 2) <= posY + sizeY) {
                            viewElement.setOnTouchListener(null)
                            viewElement.visibility = View.GONE
                            itemInList.objetivo.visibility = View.GONE
                            acierto++
                            botellaLlena()
                            checkProgress()
                        }else{
                            viewElement.x = 710F
                            viewElement.y = 1290F
                        }
                    }else{
                        viewElement.x = 280F
                        viewElement.y = 1255F
                    }

                    if (updateHandler != null) {
                        updateHandler?.removeCallbacksAndMessages(null)
                        updateHandler = null
                    }
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
        //}
        true
    }

    private fun generarVasonTarget() {
        // Generamos el vaso
        var imgVasoLleno: ImageView = generateDinamycImageElement(R.drawable.vaso6, (globalView.width - 800), (globalView.height/2+150), 250,250)

        // Generamos la Botella Vacia
        var imgBotellaVacia: ImageView = generateDinamycImageElement(R.drawable.mjuego5_botella1, (globalView.width/2 - 200/2), (globalView.height/2 - 200/2), 475, 475)

        // Generamos el corcho de la botella
        var imgCorchoBotella: ImageView = generateDinamycImageElement(R.drawable.corchotest, (globalView.width - 370), (globalView.height - 920), 200, 200)

        manzanaList.add(DragnDropImageLevel(imgVasoLleno,imgBotellaVacia,imgCorchoBotella))
        imgVasoLleno.setOnTouchListener(listener)
    }

    //Metodo para generar Una imagen dentro del layout de manera dinamica.
    private fun generateDinamycImageElement(resourceInt: Int, posX: Int, posY: Int, sizeX: Int, sizeY: Int): ImageView  {
        val constraintLayoutFound = globalView.findViewById<ConstraintLayout>(R.id.mainlayout_Minijuego6)
        var imgElement: ImageView = ImageView(requireContext())
        constraintLayoutFound.addView(imgElement)
        imgElement.layoutParams.height = sizeX
        imgElement.layoutParams.width = sizeY
        imgElement.x = posX.toFloat()
        imgElement.y = posY.toFloat()
        imgElement.setImageResource(resourceInt)
        return imgElement
    }

    private fun vasoVaciado() {
        val txtVasosVacios: TextView = globalView.findViewById(R.id.minijuego6_txtvasosvacios)

        txtVasosVacios.text = "${(txtVasosVacios.text.trim()[0]-1)}" + "/5"
    }

    private fun botellaLlena() {
        val txtBotellasLlenas: TextView = globalView.findViewById(R.id.minijuego6_txtbotellasllenas)
        txtBotellasLlenas.text = "${txtBotellasLlenas.text.trim()[0]+1}" + "/5"
        if (acierto < 5) {
            generarVasonTarget()
        }
    }

    private fun checkProgress(){

        if (acierto==5){
            DbHandler.userAumentarPuntuacion(5)
            DbHandler().requestDbUserUpdate(this)
            starAnimationfun()
        }
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
            val i = Intent(activity, Activity5_Mapa::class.java)
            startActivity(i)
            (activity as Activity?)!!.overridePendingTransition(0, 0)
        }

        btnrepetir.setOnClickListener(){
            Navigation.findNavController(it).navigate(R.id.action_fragment2_6_minijuego_self)
        }

    }


    private fun findItemByOrigen(view: View): DragnDropImageLevel? {
        for (item in manzanaList!!) {
            if (item.origen == view) {
                return item
            }
        }
        return null
    }

    private fun findItemByCorcho(view: View): DragnDropImageLevel? {
        for (item in manzanaList!!) {
            if (item.corcho == view) {
                return item
            }
        }
        return null
    }

}