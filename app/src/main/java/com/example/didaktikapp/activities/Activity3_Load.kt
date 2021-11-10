package com.example.didaktikapp.activities

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.didaktikapp.Adapter.UsersRecyclerAdapter
import com.example.didaktikapp.databinding.Activity3LoadBinding
import com.example.reto01.Model.User
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity3_load.*


class Activity3_Load : AppCompatActivity() {
    private val activity = this
    private lateinit var recyclerViewUsers:RecyclerView
    lateinit private var Usuario : User
    private lateinit var listUsers: MutableList<User>
    private lateinit var usersRecyclerAdapter:UsersRecyclerAdapter
    private lateinit var context:Context
    private lateinit var binding:Activity3LoadBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getSupportActionBar()?.hide()
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)

        binding= Activity3LoadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews()
        initObjects()
    }

    private fun initViews(){
        recyclerViewUsers =rview3_usuarios as RecyclerView

    }
    private fun initObjects(){
        listUsers = ArrayList()
        var db:FirebaseFirestore = FirebaseFirestore.getInstance()
        context =this

        getUsersData(db)


    }

    private fun  getUsersData(db:FirebaseFirestore){


        db.collection("Usuarios").get().
        addOnSuccessListener { documentos->
            for (documento in documentos){
                Usuario = User()
                Usuario.nombre=documento.data.get("nombre").toString()
                Usuario.ultima_puntuacion=documento.data.get("ultimo_punto").toString().toInt()
                Usuario.puntuacion=documento.data.get("puntuacion").toString().toInt()
                listUsers.add(Usuario)
            }

            cargarAdapter()

        }

    }
    private fun cargarAdapter(){

        //Cargar el adapter después de llamar a la bbdd
        val adapter = UsersRecyclerAdapter(listUsers, context)

        recyclerViewUsers.layoutManager = LinearLayoutManager(context)
        recyclerViewUsers.adapter = adapter
    }
}