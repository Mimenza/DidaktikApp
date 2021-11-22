package com.example.didaktikapp.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.text.InputType
import android.text.Layout
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.didaktikapp.R


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Fragment5_ajustes.newInstance] factory method to
 * create an instance of this fragment.
 */
class Fragment5_ajustes : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private val pass: String = "admin"

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
        val view = inflater.inflate(R.layout.fragment5_ajustes, container, false)
        val buttonAcercaDe: Button = view.findViewById(R.id.btn5f_acercade)
        val buttonAdmin: Button = view.findViewById(R.id.btn5f_admin)


        buttonAcercaDe.setOnClickListener{ showAcercaDeInfo()}
        buttonAdmin.setOnClickListener{ showdialog()}

        return view
    }

    fun showAcercaDeInfo(){
        var dialog = Dialog(requireContext())
        val layout:View = layoutInflater.inflate(R.layout.acercadedialog, null)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(layout)
        dialog.show()
        dialog.window!!.setLayout(
            900, 1400
        )
    }

    fun showdialog(){
        val builder: AlertDialog.Builder = android.app.AlertDialog.Builder(requireContext())
        builder.setTitle("Admin Password")
        val input = EditText(requireContext())
        input.setHint("Enter the admin password")
        input.inputType = InputType.TYPE_CLASS_TEXT
        builder.setView(input)

        builder.setPositiveButton("Iniciar", DialogInterface.OnClickListener { dialog, which ->
            //var m_Text = input.text.toString()
            if (pass.equals(input.text.toString())) {
                Toast.makeText(requireContext(), "MODO ADMIN ACTIVADO CORRECTAMENTE", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "ERROR MODO ADMIN", Toast.LENGTH_SHORT).show()
            }
        })
        builder.setNegativeButton("Cancelar", DialogInterface.OnClickListener { dialog, which -> dialog.cancel() })
        builder.show()
    }



    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Fragment5_ajustes.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Fragment5_ajustes().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}