package com.example.contact

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.contact.AddEditActivity
import com.example.contact.databinding.ActivityMainBinding
import com.example.contact.mvvmarch.MainActivityViewModel
import com.example.contact.roomdb.DbBuilder
import com.example.contact.roomdb.entity.Contact
import com.example.contactapp.ContactAdapter

class MainActivity : AppCompatActivity() {
    private val binding by lazy {

        ActivityMainBinding.inflate(layoutInflater)
    }
   /* var contactList= ArrayList<Contact>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.floatingActionButton.setOnClickListener {

            startActivity(Intent(this@MainActivity, AddEditActivity::class.java))
        }

        DbBuilder.getdb(this)?.contactDao()?.let {
            contactList.addAll(it.readContact())

        }


        binding.rv.layoutManager= LinearLayoutManager(this)

        binding.rv.adapter= ContactAdapter(contactList,this)
        Log.d("MYTAG", "onCreate: ${R.layout.activity_main}")
    }
}*/
   var viewModel: MainActivityViewModel? = null
    var contactList = ArrayList<Contact>()
    lateinit var adapter: ContactAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        viewModel = ViewModelProvider(this)[MainActivityViewModel::class.java]
        binding.floatingActionButton.setOnClickListener {

            startActivity(Intent(this@MainActivity, AddEditActivity::class.java))
        }
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.CALL_PHONE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            createUI()
        } else if (ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                android.Manifest.permission.CALL_PHONE
            )
        ) {

            val alertDialog = AlertDialog.Builder(this)
                .setMessage("This app require read video permission to run")
                .setTitle("Permission Required")
                .setCancelable(false)
                .setPositiveButton("OK") { dialog, which ->
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(
                           android.Manifest.permission.CALL_PHONE
                        ), DIAl_CALL_PERMISSION
                    )
                }
                .setNegativeButton("cancel") { dialog, which ->
                    dialog.dismiss()
                }
                .create()
            alertDialog.show()

        } else {

            ActivityCompat.requestPermissions(
                this@MainActivity, arrayOf(android.Manifest.permission.CALL_PHONE),
                DIAl_CALL_PERMISSION
            )
        }

    }

    fun createUI() {

        viewModel = ViewModelProvider(this)[MainActivityViewModel::class.java]
        binding.floatingActionButton.setOnClickListener {

            startActivity(Intent(this@MainActivity, AddEditActivity::class.java))
        }


        viewModel!!.data.observeForever {
            contactList.clear()
            it.forEach {
                contactList.add(it)
            }
            adapter.notifyDataSetChanged()

        }
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            1,
            ItemTouchHelper.LEFT
                    or ItemTouchHelper.RIGHT
        ) {


            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                if (ItemTouchHelper.LEFT == direction) {

                    viewModel!!.deleteContact(contactList.get(viewHolder.adapterPosition))
                } else {
                    val intent = Intent(Intent.ACTION_CALL)
                    intent.data =
                        Uri.parse("tel:${contactList.get(viewHolder.adapterPosition).phoneNumber}")
                    startActivity(intent)
                    adapter.notifyDataSetChanged()
                }
                 fun onChildDraw(
                    c: Canvas,
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    dX: Float,
                    dY: Float,
                    actionState: Int,
                    isCurrentlyActive: Boolean
                ) {
                     super.onChildDraw(
                         c,
                         recyclerView,
                         viewHolder,
                         dX,
                         dY,
                         actionState,
                         isCurrentlyActive
                     )
                     if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE && isCurrentlyActive) {
                         val itemView = viewHolder.itemView
                         if (ItemTouchHelper.LEFT == direction) {


                             // Draw red background
                             val background = ColorDrawable(Color.RED)
                             background.setBounds(
                                 itemView.right + dX.toInt(),
                                 itemView.top,
                                 itemView.right,
                                 itemView.bottom
                             )
                             background.draw(c)
                             // Draw icon
                             val icon = ContextCompat.getDrawable(
                                 recyclerView.context,
                                 R.drawable.baseline_delete_24 // Replace with your icon
                             )
                             val iconMargin = (itemView.height - icon?.intrinsicHeight!!) / 2
                             val iconTop = itemView.top + (itemView.height - icon?.intrinsicHeight!!) / 2
                             val iconBottom = iconTop + icon?.intrinsicHeight!!
                             val iconLeft = itemView.right - iconMargin - icon?.intrinsicWidth!!
                             val iconRight = itemView.right - iconMargin
                             icon.setBounds(iconLeft, iconTop, iconRight, iconBottom)
                             icon.draw(c)
                         }

                     }
                 }
                 }



        }).attachToRecyclerView(binding.rv)

        binding.rv.layoutManager = LinearLayoutManager(this)
        adapter = ContactAdapter(contactList, this)
        binding.rv.adapter = adapter
        Log.d("MYTAG", "onCreate: ${R.layout.activity_main}")
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == DIAl_CALL_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                createUI()
            } else if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    android.Manifest.permission.CALL_PHONE
                )
            ) {

                val alertDialog = AlertDialog.Builder(this)
                    .setMessage("This app require read video permission to run")
                    .setTitle("Permission Required")
                    .setCancelable(false)
                    .setPositiveButton("OK") { dialog, which ->
                        ActivityCompat.requestPermissions(
                            this,
                            arrayOf(
                               android.Manifest.permission.CALL_PHONE
                            ), DIAl_CALL_PERMISSION
                        )
                    }
                    .setNegativeButton("cancel") { dialog, which ->
                        dialog.dismiss()
                    }
                    .create()
                alertDialog.show()

            } else {
                ActivityCompat.requestPermissions(
                    this@MainActivity, arrayOf(Manifest.permission.CALL_PHONE),
                    DIAl_CALL_PERMISSION
                )
            }
        }
    }
}