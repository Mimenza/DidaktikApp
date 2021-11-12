package com.example.didaktikapp.activities.sites


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.didaktikapp.R

import com.example.didaktikapp.databinding.Activity61SagardoetxeaBinding


class Activity6_1_Sagardoetxea : AppCompatActivity() {


    private lateinit var binding : Activity61SagardoetxeaBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getSupportActionBar()?.hide()
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
        setContentView(R.layout.activity6_1_sagardoetxea)
        getSupportActionBar()?.hide()



        binding= Activity61SagardoetxeaBinding.inflate(layoutInflater)
        setContentView(binding.root)




    }
}