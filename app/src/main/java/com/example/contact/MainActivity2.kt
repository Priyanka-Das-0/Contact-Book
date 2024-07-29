package com.example.contact

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.contact.databinding.ActivityMain2Binding

class MainActivity2 : AppCompatActivity() {
    lateinit var viewModel: TempViewModel
    val binding by lazy {
        ActivityMain2Binding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(TempViewModel::class.java)


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.textView.text = viewModel.provideA().toString()
        binding.button.setOnClickListener {
            viewModel.a++
            binding.textView.text = viewModel.provideA().toString()

        }

    }


}