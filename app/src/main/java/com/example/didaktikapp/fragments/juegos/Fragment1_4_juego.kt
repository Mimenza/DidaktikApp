package com.example.didaktikapp.fragments.juegos

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.Navigation
import com.example.didaktikapp.Model.CustomLine
import com.example.didaktikapp.Model.DragnDropImage
import com.example.didaktikapp.R
import com.example.didaktikapp.activities.Activity6_Site
import com.example.didaktikapp.activities.DbHandler
import com.google.android.gms.maps.model.LatLng
import org.w3c.dom.Text
import java.util.*
import kotlin.collections.ArrayList
import android.widget.LinearLayout
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Fragment1_juego.newInstance] factory method to
 * create an instance of this fragment.
 */
class Fragment1_4_juego : Fragment() {
    private val thisJuegoId = 4
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var layout: ConstraintLayout

    private lateinit var constraintFila1: View

    private var myLineTest: CustomLine? = null

    private var testWidth: Int? = null
    private var testHeight: Int? = null

    private var nFilas: Int = 14
    private var nCols: Int = 13

    private lateinit var globalView: View
    private lateinit var matrizMain: LinearLayout
    private var letterWeight = 1/nCols


//    val letterList = arrayListOf<ArrayList<Any>>(
//        arrayListOf("A","B","C","D","E"),
//        arrayListOf("F","G","H","I","J"),
//        arrayListOf("K","L","M","N","O"),
//        arrayListOf("Q","R","S","T","U"),
//        arrayListOf("Q","R","S","T","U"),
//    )

    val lettersPosition = arrayListOf<ArrayList<Any>>(
        //arrayListOf(intArrayOf(12,12)),   // ARRAY DE EJEMPLO
    )

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
        val view = inflater.inflate(R.layout.fragment1_4_juego, container, false)
        globalView = view
        matrizMain = view.findViewById(R.id.matrizprincipal)
        val button: Button = view.findViewById(R.id.btnf1_4_siguiente)
        val ajustes: ImageButton = view.findViewById(R.id.btnf1_4_ajustes)
        layout = view.findViewById(R.id.myConstraintTest)



        /*
        //TODO DONT REMOVE THIS. This is drawline from a vector a to b
        val imageTest: ImageView = view.findViewById(R.id.manzanaTest)
        imageTest.setOnTouchListener(listener)
        myLineTest = CustomLine(requireContext(),0f,0f,250f,250f, 15F, 162, 224, 23)
        layout.addView(myLineTest)
         */

        button.setOnClickListener(){
            Navigation.findNavController(view).navigate(R.id.action_fragment1_4_juego_to_fragment2_4_minijuego)
        }
        ajustes.setOnClickListener(){
                (activity as Activity6_Site?)?.menuCheck()
        }
        //Typewriter juego 4 tutorial
      /*  Handler(Looper.getMainLooper()).postDelayed({
            if (getView() != null) {
                typewriter(view)
            }
        }, 2000) */


        //Animacion manzana al iniciar el juego
      /*  starAnimationfun(view)


        runBlocking {
            launch {
                audio = MediaPlayer.create(context, R.raw.juego4audiotutorial)
                audio?.start()

                audio?.setOnCompletionListener {

                    Handler(Looper.getMainLooper()).postDelayed({
                        if (getView() != null) {
                            //Llama a la funcion para la animacion de salida cuando el audio se termina
                            exitAnimationfun(view)
                        }
                    }, 1000)
                }
            }
        }*/

        view.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                view.viewTreeObserver.removeOnGlobalLayoutListener(this)


                //constraintFila1 = view.findViewById(R.id.fila1)
                //val childListFila1: ViewGroup = constraintFila1 as ViewGroup
                //println("******** CHILD COUNT: " + childListFila1.childCount)
                prepararJuego()
            }
        })

        return view
    }

    private var letras = "ABCDEFGHIJKLMNÑOPQRSTUVWXYZ"

    var letterList = arrayListOf<ArrayList<String>>(
//        arrayListOf("A","B","C","D","E"),
//        arrayListOf("F","G","H","I","J"),
//        arrayListOf("K","L","M","N","O"),
//        arrayListOf("Q","R","S","T","U"),
//        arrayListOf("Q","R","S","T","U"),
    )


    private var palabrasPintadas = arrayListOf<String>()

    private var palabras = arrayListOf<String>("TXISTULARIAK","SAGARDANTZA","TXALAPARTA","TRIKITIXA","ERAKUSKETA",
        "SALMENTA","DASDATZEA","BILKETA","GARBTIZEA")

    private var filasHuecosLibres = arrayListOf<Int>()
    private var colsHuecosLibres = arrayListOf<Int>()

    private fun prepararJuego() {
        //Primero generamos la matriz limpia
        for (i in 0 until nFilas) {
            var filaLetterList = arrayListOf<String>()
            letterList.add(filaLetterList)
            for (k in 0 until nCols) {
                filaLetterList.add("•")
            }
        }
        //Seleccionamos una palabra aleatoria de la lista
        //seleccionarPalabraAleatoria()
        escribirPalabrasModoSimple()
        // La pintamos sobre la sopa
        pintarSopa()
    }

    private fun escribirPalabrasModoSimple() {
        //Palabras Horizontales
        //#SAGARDANTZA
        letterList[0][2] = "S"
        letterList[0][3] = "A"
        letterList[0][4] = "G"
        letterList[0][5] = "A"
        letterList[0][6] = "R"
        letterList[0][7] = "D"
        letterList[0][8] = "A"
        letterList[0][9] = "N"
        letterList[0][10] = "T"
        letterList[0][11] = "Z"
        letterList[0][12] = "A"

        //SALMENTA
        letterList[11][3] = "S"
        letterList[11][4] = "A"
        letterList[11][5] = "L"
        letterList[11][6] = "M"
        letterList[11][7] = "E"
        letterList[11][8] = "N"
        letterList[11][9] = "T"
        letterList[11][10] = "A"

        //TRIKITIXA
        letterList[12][3] = "A"
        letterList[12][4] = "X"
        letterList[12][5] = "I"
        letterList[12][6] = "T"
        letterList[12][7] = "I"
        letterList[12][8] = "K"
        letterList[12][9] = "I"
        letterList[12][10] = "R"
        letterList[12][11] = "T"

        //palabras verticales
        letterList[3][1] = "T"
        letterList[4][1] = "X"
        letterList[5][1] = "A"
        letterList[6][1] = "L"
        letterList[7][1] = "A"
        letterList[8][1] = "P"
        letterList[9][1] = "A"
        letterList[10][1] = "R"
        letterList[11][1] = "T"
        letterList[12][1] = "A"

        letterList[2][11] = "G"
        letterList[3][11] = "A"
        letterList[4][11] = "R"
        letterList[5][11] = "B"
        letterList[6][11] = "I"
        letterList[7][11] = "T"
        letterList[8][11] = "Z"
        letterList[9][11] = "E"
        letterList[10][11] = "A"

        letterList[1][12] = "D"
        letterList[2][12] = "A"
        letterList[3][12] = "S"
        letterList[4][12] = "T"
        letterList[5][12] = "A"
        letterList[6][12] = "T"
        letterList[7][12] = "Z"
        letterList[8][12] = "E"
        letterList[9][12] = "A"


        //convert0ToRandomLetters()
        //#SALMENTZA
        //for (i in 3 until letterList[11].size-1) {
         //   letterList[11][i] = "SALMENTZA".get(i).toString()
        //}
    }

    private fun convert0ToRandomLetters() {
        for (i in 0 until letterList.size-1) {
            for (k in 0 until letterList[i].size) {
                if (letterList[i][k].equals("0")) {
                    val letrasLength = letras.length
                    val randomLetter = (0 until letrasLength-1).random()
                    letterList[i][k] = letras.substring(randomLetter,randomLetter+1)
                }
            }
        }
    }

    private var palabraPreparada: String = ""

    private fun pintarSopa() {
        /*
        val filaRandom = (0 until filasHuecosLibres.size-1).random()
        for ((i, value) in letterList[filaRandom].withIndex()) {
            if (i <= palabraPreparada.length-1 ) {
                letterList[filaRandom][i] = palabraPreparada.get(i).toString()
            } else {
                //break
            }
        }

         */


        //PINTAR
        for (i in 0 until letterList.size-1) {
            var filaLetterList = arrayListOf<String>()
            //letterList.add(filaLetterList)
            var nuevaFila = LinearLayout(requireContext())
            val paramsLinear: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
            )
            paramsLinear.weight = 13.toFloat()
            nuevaFila.setLayoutParams(paramsLinear)
            nuevaFila.setOrientation(LinearLayout.HORIZONTAL)
            matrizMain.addView(nuevaFila)

            for (k in 0 until letterList[i].size) {
                val letrasLength = letras.length
                val randomLetter = (0 until letrasLength-1).random()
                var letraElement = TextView(requireContext())
                letraElement.id = k+(i*nCols)
                //println("********** GENERATED ID: " + letraElement.id)
                //letraElement.text = letras.substring(randomLetter,randomLetter+1)
                letraElement.text = letterList[i][k]
                //filaLetterList.add("0")
                letraElement.textSize = 24.toFloat()
                letraElement.width = nuevaFila.width/nCols
                val params: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT
                )
                params.weight = 1.toFloat()
                letraElement.setLayoutParams(params)
                nuevaFila.addView(letraElement)
            }
        }
    }

    private fun seleccionarPalabraAleatoria() {
        //val palabraRandom = (0 until palabras.size-1).random()
        //palabrasPintadas.add(palabras[palabraRandom])
        for ((index, value) in palabras.withIndex()) {
            if (!palabrasPintadas.contains(value)) {
                palabrasPintadas.add(value)
                palabraPreparada = value
                findHuecosFilasLibres()
                break
            }
        }

    }

    private fun tipoMovimiento() {

    }

    private fun findDiagDchaLibres() {

    }

    private fun findDiagIzqLibres() {

    }

    private fun findHuecosFilasLibres() {
        for ((i, value) in letterList.withIndex()) {
            var filaHuecosLibres = 0
            for ((k, letter) in letterList.withIndex()) {
                if (letter.equals("0")) {
                    filaHuecosLibres++
                    if (filaHuecosLibres >= palabraPreparada.length) {
                        break
                    }
                }
            }
            filasHuecosLibres.add(i)
        }
        println(Arrays.toString(filasHuecosLibres.toIntArray()))
    }

    private fun findHuecosColsLibres() {

    }

    private fun findFilasLibres() {

    }

    private fun findColsLibres() {

    }

   /* private fun typewriter(view: View) {
        val typeWriterView = view.findViewById(R.id.txtv1_4tutorialjuego4) as TypeWriterView
        typeWriterView.setWithMusic(false)
        typeWriterView.animateText(resources.getString(R.string.juego4audiotutorialtxt))
        typeWriterView.setDelay(65)
    }

    private fun starAnimationfun(view: View) {
        //Animacion fondo gris
        val txtAnimacion = view.findViewById(R.id.txtv1_4fondogris) as TextView
        val aniFade = AnimationUtils.loadAnimation(context, R.anim.fade)
        txtAnimacion.startAnimation(aniFade)

        //Animacion entrada upelio
        vistaAnimada = TranslateAnimation(-1000f, 0f, 0f, 0f)
        vistaAnimada.duration = 2000
        val upelio = view.findViewById(R.id.imgv1_4_upelio) as ImageView
        upelio.startAnimation(vistaAnimada)

        //llamamos a la animacion para animar a upelio
        Handler(Looper.getMainLooper()).postDelayed({
            if (getView() != null) {
                upelio.isVisible = false
                talkAnimationfun(view)
            }
        }, 2000)
    }
    */






    fun prepareSopa() {
        var vFila1: ArrayList<View>? = getConstraintChildCount(constraintFila1)

        for ((index, value) in vFila1!!.withIndex()) {
            var txtElement: TextView = value as TextView
            var txtPosX = txtElement.x
            var txtPosY = txtElement.y
            var txtSizeX = txtElement.getLayoutParams().width
            var txtSizeY = txtElement.getLayoutParams().width
            if (testWidth == null ||testHeight == null) {
                testWidth = txtSizeX
                testHeight = txtSizeY
            }
            println("********* the element at $index is ${txtElement.text.toString()}")

            if (lettersPosition.size == 0) {
                lettersPosition.add(arrayListOf())
            }




            /*
            if (lettersPosition.isEmpty()) {
                //lettersPosition.add(arrayListOf())
                lettersPosition.add(arrayListOf())
            }

             */
            //lettersPosition[0].add(arrayListOf(floatArrayOf(txtPosX,txtPosY)))
            lettersPosition[0].add(floatArrayOf(txtPosX,txtPosY))
            txtElement.setOnTouchListener(listener)
        }
        println("******** TESTING ROW: " +lettersPosition[0])
    }

    fun getConstraintChildCount(contraintView: View): ArrayList<View>? {
        //TODO TENER CUIDADO DE USAR ESTA FUNCION CON HIJOS QUE NO SON DIRECTOS (PODRIA SEGUIR INCREMENTANDO)
        val result = ArrayList<View>()
        val childList: ViewGroup = contraintView as ViewGroup
        // No se puede realizar un bucle a un viewgroup
        for (i in 0 until childList.childCount) {
            val child = childList.getChildAt(i)
            result.add(child)
        }
        return result
    }

    fun checkOtherLettersHover(posX: Int, posY: Int) {
        for ((i, row) in lettersPosition.withIndex()) {
            for ((k, col) in lettersPosition[i].withIndex()) {

            }
        }

        /*
        for (i in 0 until lettersPosition.size-1) {
            for (k in 0 until lettersPosition[i].size-1) {
            }
        }
         */
    }

    fun findPossibleMove() {

    }

    fun checkNearestLetter() {

    }

    fun findMyFirstLetterKey() {

    }

    fun checkNextLetter() {

    }



    @SuppressLint("ClickableViewAccessibility")
    var listener = View.OnTouchListener { viewElement, motionEvent ->
        var tElementTxt: TextView = viewElement as TextView
        println("******************** TECLA PRESIONADA: " + tElementTxt.text)
        val action = motionEvent.action
        when(action) {
            MotionEvent.ACTION_MOVE -> {
                checkOtherLettersHover(motionEvent.rawX.toInt(), motionEvent.rawY.toInt())
                //motionEvent.rawX
                /*
                viewElement.x = motionEvent.rawX - viewElement.width/2
                viewElement.y = motionEvent.rawY - viewElement.height/2
                layout.removeView(myLineTest)
                myLineTest = null
                myLineTest = CustomLine(requireContext(),0f,0f,motionEvent.rawX,motionEvent.rawY, 15F, 162, 224, 23)
                layout.addView(myLineTest)
                 */
            }
            MotionEvent.ACTION_UP -> {

                //viewElement.x = motionEvent.rawX - viewElement.width/2
                //viewElement.y = motionEvent.rawY - viewElement.height/2


            }
        }
        true
    }


    /*
    //TODO DONT REMOVE THIS. This is drawline from a vector a to b
    @SuppressLint("ClickableViewAccessibility")
    var listener = View.OnTouchListener { viewElement, motionEvent ->
        viewElement.bringToFront()
        val action = motionEvent.action
        when(action) {
            MotionEvent.ACTION_MOVE -> {
                viewElement.x = motionEvent.rawX - viewElement.width/2
                viewElement.y = motionEvent.rawY - viewElement.height/2
                layout.removeView(myLineTest)
                myLineTest = null
                myLineTest = CustomLine(requireContext(),0f,0f,motionEvent.rawX,motionEvent.rawY, 15F, 162, 224, 23)
                layout.addView(myLineTest)
            }
            MotionEvent.ACTION_UP -> {
                viewElement.x = motionEvent.rawX - viewElement.width/2
                viewElement.y = motionEvent.rawY - viewElement.height/2


            }
        }
        true
    }
     */

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
            Fragment1_4_juego().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}