package com.example.didaktikapp.fragments

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.*
import com.example.didaktikapp.R
import com.example.didaktikapp.activities.DbHandler
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat.recreate

import com.example.didaktikapp.Model.MyPreferences
import com.example.didaktikapp.activities.Activity1_Principal
import com.example.didaktikapp.activities.Activity5_Mapa
import java.util.*

class Fragment4_ajustes : Fragment() {
    private val pass: String = "upelio"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment4_ajustes, container, false)
        val buttonAcercaDe: Button = view.findViewById(R.id.btn4f_acercade)
        val buttonAdmin: Button = view.findViewById(R.id.btn4f_admin)
        val buttonTheme: Button = view.findViewById(R.id.btn4f_oscuro)
        val buttonIdiomas: Button = view.findViewById(R.id.btn4f_idioma)
        val buttonHome: Button = view.findViewById(R.id.btn4f_home)

        //Segun la opcion seleccionada, clickamos oscuro o claro
        checkTheme(requireContext())

        buttonAcercaDe.setOnClickListener{
            showAcercaDeInfo()
        }

        buttonAdmin.setOnClickListener{
            showModoAdminDialog()
        }

        buttonTheme.setOnClickListener{
            chooseThemeDialog(view)
        }

        buttonIdiomas.setOnClickListener{
            chooseLanguageDialog()
        }

        buttonHome.setOnClickListener{
            activity?.let{
                val intent = Intent (it, Activity1_Principal::class.java)
                it.startActivity(intent)
            }
        }

        return view
    }


          fun chooseThemeDialog(view:View){
              val builder = AlertDialog.Builder(requireContext())
              builder.setTitle(getString(R.string.tema))
              val styles = arrayOf("Claro", "Oscuro")
              val checkedItem = MyPreferences(requireContext()).darkMode

              builder.setSingleChoiceItems(styles, checkedItem) { dialog, which ->

                  when (which) {
                      0 -> {
                          AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                          MyPreferences(requireContext()).darkMode = 0
                          ( requireContext() as  AppCompatActivity).delegate.applyDayNight()

                          getActivity()?.onBackPressed()
                          dialog.dismiss()
                      }
                      1 -> {
                          AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                          MyPreferences(requireContext()).darkMode = 1
                          ( requireContext() as  AppCompatActivity).delegate.applyDayNight()


                          getActivity()?.onBackPressed()
                          dialog.dismiss()
                      }

                  }
              }

              val dialog = builder.create()
              dialog.show()

          }


    fun checkTheme(requireContext:Context) {

        when (MyPreferences(requireContext).darkMode) {
            0 -> {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                ( requireContext  as  AppCompatActivity).delegate.applyDayNight()

            }
            1 -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                ( requireContext as  AppCompatActivity).delegate.applyDayNight()

            }

        }
    }

    //IDIOMAS DIALOG
    fun chooseLanguageDialog() {

        val spanish = getString(R.string.español)
        val euskera = getString(R.string.euskera)

        val languages = arrayOf(euskera, spanish)
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(getString(R.string.Idiomas))

        val checkedItem = MyPreferences(requireContext()).lang

        builder.setSingleChoiceItems(languages, checkedItem) { dialog, which ->
            when (which) {
                0 -> {
                    setLocate("eu")
                    //Para cambiarlo directamente
                     recreate(requireContext() as Activity)
                    //Te lleva a la principal al cambiar
                    getActivity()?.onBackPressed()
                    MyPreferences(requireContext()).lang = 0
                    dialog.dismiss()
                }
                1 -> {
                    setLocate("es")
                    recreate(requireContext() as Activity)
                    getActivity()?.onBackPressed();
                    MyPreferences(requireContext()).lang = 1
                    dialog.dismiss()
                }


            }

        }
        val dialog = builder.create()
        dialog.show()
    }

    //for change language
    fun setLocate(lang: String) {
        val locale= Locale(lang)
        Locale.setDefault(locale)
        val config = Configuration()
        config.locale =locale
        requireContext().resources?.updateConfiguration(config, requireContext().resources.displayMetrics)

    }

    fun showAcercaDeInfo(){
        var dialog = Dialog(requireContext())
        val layout:View = layoutInflater.inflate(R.layout.acercadedialog, null)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(layout)
        dialog.window!!.setLayout(
            900, 1400
        )
        dialog.show()

    }

    fun showModoAdminDialog(){
        val builder: AlertDialog.Builder = android.app.AlertDialog.Builder(requireContext())
        builder.setView(R.layout.admin_login_dialog)

        val dialog = builder.create()


        dialog.show()

        val txtAdmin: TextView = dialog.findViewById(R.id.txtPassword)
        val btnCancel: TextView = dialog.findViewById(R.id.adminCancel)
        val btnLogin: TextView = dialog.findViewById(R.id.adminLogin)
        val txtInfo: TextView = dialog.findViewById(R.id.adminDialogInfoText)

        if (!DbHandler.getAdmin()) {
            txtAdmin.visibility = View.VISIBLE
            txtInfo.visibility = View.VISIBLE
            btnLogin.setBackgroundColor(resources.getColor(R.color.primary))
        } else {
            txtInfo.visibility = View.GONE
            txtAdmin.visibility = View.GONE
            btnLogin.text = getString(R.string.adminActivated)
            btnLogin.setBackgroundColor(resources.getColor(R.color.red))
        }

        btnCancel.setOnClickListener() {
            dialog.dismiss()
        }

        btnLogin.setOnClickListener() {
            if (!DbHandler.getAdmin()) {
                if (pass.equals(txtAdmin.text.toString())) {
                    DbHandler.setAdmin(true)
                    Toast.makeText(requireContext(), getString(R.string.adminActivated), Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                    callSingletonMapClass()
                } else {
                    Toast.makeText(requireContext(), getString(R.string.adminNotActivated), Toast.LENGTH_SHORT).show()
                }
            } else {
                DbHandler.setAdmin(false)
                dialog.dismiss()
                Toast.makeText(requireContext(), getString(R.string.adminDesactivated), Toast.LENGTH_SHORT).show()
                callSingletonMapClass()
            }
        }



        //dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        /*
        var dialog: Dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.admin_login_dialog)
        //dialog.window.setBackgroundDrawable(getDrawable())
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.setCancelable(false)
        dialog.window?.attributes?.windowAnimations = R.style.animation

         */


        /*
        val builder: AlertDialog.Builder = android.app.AlertDialog.Builder(requireContext())
        builder.setTitle("Modo Administrador")
        val input = EditText(requireContext())
        input.setHint("Ingrese la contraseña de administrador")
        input.inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD
        input.setTransformationMethod(PasswordTransformationMethod.getInstance());
        builder.setView(input)

        builder.setPositiveButton("Logear", DialogInterface.OnClickListener { dialog, which ->
            //var m_Text = input.text.toString()
            if (pass.equals(input.text.toString())) {
                DbHandler.setAdmin()
                Toast.makeText(requireContext(), "Modo Administrador activado correctamente", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Error al activar el modo administrador", Toast.LENGTH_SHORT).show()
            }
        })
        builder.setNegativeButton("Cancelar", DialogInterface.OnClickListener { dialog, which -> dialog.cancel() })
        builder.show()

         */
    }

    private fun callSingletonMapClass() {
        if (Activity5_Mapa.instancia != null) {
            Activity5_Mapa.instancia!!.checkPoints()
        }
    }




}