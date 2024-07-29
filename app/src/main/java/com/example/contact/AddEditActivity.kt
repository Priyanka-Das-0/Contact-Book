package com.example.contact

import android.app.Activity
import android.app.Dialog
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.contact.databinding.ActivityAddEditBinding
import com.example.contact.mvvmarch.AddEditActivityViewModel
import com.example.contact.roomdb.DbBuilder
import com.example.contact.roomdb.entity.Contact
import com.github.dhaval2404.imagepicker.ImagePicker


class AddEditActivity : AppCompatActivity() {
    val binding by lazy {
        ActivityAddEditBinding.inflate(layoutInflater)
    }
    var contact = Contact()
    private val startForProfileImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data

            if (resultCode == RESULT_OK) {
                //Image Uri will not be null for RESULT_OK
                val fileUri = data?.data!!


                binding.imageView.setImageURI(fileUri)

                val imageBytes = contentResolver.openInputStream(fileUri)?.readBytes()
             //   BitmapFactory.decodeByteArray(imageBytes,0,imageBytes!!.size).compress(Bitmap.CompressFormat.JPEG,80)
                contact.profile= imageBytes
            } else if (resultCode == ImagePicker.RESULT_ERROR) {
                Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
            }
        }

   /* @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }*/

        lateinit var viewModel: AddEditActivityViewModel
        var flag = -1
        @RequiresApi(Build.VERSION_CODES.S)
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            enableEdgeToEdge()
            setContentView(binding.root)
            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
                val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
                insets
            }

            viewModel = ViewModelProvider(this).get(AddEditActivityViewModel::class.java)


            if (intent.hasExtra("FLAG")) {
                flag = intent.getIntExtra("FLAG", -1)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                    contact = intent.getSerializableExtra("DATA", Contact::class.java)!!
                else {
                    contact = intent.getSerializableExtra("DATA") as Contact

                }
                binding.saveContact.text = "Update Contact"
                var imageByte = contact.profile

                if (imageByte != null) {
                    var image = BitmapFactory.decodeByteArray(imageByte, 0, imageByte.size)
                    binding.imageView.setImageBitmap(image)


                }
                binding.email.editText?.setText(contact.email)
                binding.phone.editText?.setText(contact.phoneNumber)
                binding.name.editText?.setText(contact.name)

            }
            binding.imageView.setOnLongClickListener {

            val dialog= Dialog(this)
            dialog.setContentView(R.layout.image_dialog)

            val image= dialog.findViewById<ImageView>(R.id.imageView)
            val imageObject= binding.imageView.drawable
            image?.setImageDrawable(imageObject)

            val lp = WindowManager.LayoutParams()
            lp.width= WindowManager.LayoutParams.WRAP_CONTENT
            lp.height= WindowManager.LayoutParams.WRAP_CONTENT
            lp.blurBehindRadius=100
            lp.flags = WindowManager.LayoutParams.FLAG_BLUR_BEHIND
            dialog.window?.attributes= lp
            dialog.show()

            true


        }

            binding.saveContact.setOnClickListener {

                contact.name = binding.name.editText?.text.toString()
                contact.email = binding.email.editText?.text.toString()
                contact.phoneNumber = binding.phone.editText?.text.toString()
                if (flag == 1) {

                    viewModel.updateData(contact) {
                        if (it != null) {
                            if (it > 0) {
                                Toast.makeText(this@AddEditActivity, it.toString(), Toast.LENGTH_SHORT)
                                    .show()
                                finish()
                            }
                        }
                    }

                } else {
                    viewModel.storeData(contact) {

                        if (it != null) {
                            if (it > 0) {
                                Toast.makeText(this@AddEditActivity, it.toString(), Toast.LENGTH_SHORT)
                                    .show()
                                finish()
                            }
                        }
                    }
                }


            }
            binding.imageView.setOnClickListener {
                ImagePicker.with(this).compress(1).crop().createIntent { intent ->
                    startForProfileImageResult.launch(intent)
                }
            }


        }


}


