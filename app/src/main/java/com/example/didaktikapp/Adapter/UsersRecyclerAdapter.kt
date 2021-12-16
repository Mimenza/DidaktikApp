package com.example.didaktikapp.Adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.didaktikapp.R
import com.example.didaktikapp.activities.Activity3_Load
import com.example.didaktikapp.activities.Activity4_bienvenida
import com.example.didaktikapp.activities.Activity5_Mapa
import com.example.didaktikapp.activities.DbHandler
import com.example.reto01.Model.User
import kotlinx.android.synthetic.main.itemuserrecycler.view.*

class UsersRecyclerAdapter(val pContext: Context, private val listUsers: List<User>,  val context:Context) : RecyclerView.Adapter<UsersRecyclerAdapter.UserViewHolder>() {

    private val thisActivity: Activity = Activity()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersRecyclerAdapter.UserViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.itemuserrecycler, parent, false)
        return UserViewHolder(itemView)
    }

    override fun onBindViewHolder(holder:UsersRecyclerAdapter.UserViewHolder, i:Int) {
        holder.render(listUsers[i])
    }

    override fun getItemCount():Int {
        return listUsers.size
    }

    fun requestLoadUserData(pUserObject: User) {
        DbHandler.setUser(pUserObject)
        var bvIntent = Intent(pContext, Activity5_Mapa::class.java)
        pContext.startActivity(bvIntent)
        (pContext as Activity).finish()
    }

    inner class UserViewHolder(val view:View) : RecyclerView.ViewHolder(view) {
        fun render(item: User) {
            view.txtv_nombre.text = item.nombre
            view.txtv_puntuacion.text = item.puntuacion.toString()
            view.txtv_ultimopunto.text = item.ultima_puntuacion.toString()

            view.setOnClickListener() {
                requestLoadUserData(item)
            }
        }
    }
}