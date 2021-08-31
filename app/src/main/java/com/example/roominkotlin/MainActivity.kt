package com.example.roominkotlin

import android.app.Activity
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.activity_main.*
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager.VERTICAL
import androidx.room.Database
import com.example.roominkotlin.DB.UserEntity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*

import java.util.Calendar.getInstance
import java.util.EnumSet.of
import java.util.List.of
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity(), RecyclerViewAdapter.rowClickListener {

    lateinit var recyclerViewAdapter: RecyclerViewAdapter
    lateinit var viewModel: ActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        recyclerview.apply{
            layoutManager = LinearLayoutManager(this@MainActivity)
            recyclerViewAdapter = RecyclerViewAdapter(this@MainActivity)
            adapter = recyclerViewAdapter
            val divider = DividerItemDecoration(applicationContext, VERTICAL)
            addItemDecoration(divider)
        }
        viewModel = ViewModelProviders.of(this).get(ActivityViewModel::class.java)
        viewModel.getAllUsersObservers().observe(this, androidx.lifecycle.Observer {
            recyclerViewAdapter.setListData(ArrayList(it))
            recyclerViewAdapter.notifyDataSetChanged()
        })

        savebtn.setOnClickListener {
           val name = inputname.text.toString()
            val email = inputemail.text.toString()

            if (savebtn.text.equals("save")) {
                 val user = UserEntity(0, name, email)
                 viewModel.insertUserInfo(user)
                Toast.makeText(applicationContext, "$user", Toast.LENGTH_SHORT).show()
             } else{
                 val user = UserEntity(inputname.getTag(inputname.id).toString().toInt(), name, email)
                 viewModel.updateUserInfo(user)
                 savebtn.setText("save")
             }
            inputname.setText("")
            inputemail.setText("")
        }
        // this update button is for seperating button
        updatebtn.setOnClickListener {
            val name00 = inputname.text.toString()
            val email00 = inputemail.text.toString()

            if (savebtn.text.equals("update")) {
                val user = UserEntity(0, name00, email00)
                viewModel.insertUserInfo(user)

            } else {
                val user =
                    UserEntity(inputname.getTag(inputname.id).toString().toInt(), name00, email00)
                viewModel.updateUserInfo(user)
            }
            inputname.setText("")
            inputemail.setText("")
        }

    }

    override fun onDeleteUserClickListener(user: UserEntity) {
        viewModel.deleteUserInfo(user)
    }

    override fun onItemClickListener(user: UserEntity) {
        inputname.setText(user.name)
        inputemail.setText(user.email)
        inputname.setTag(inputname.id, user.id)
        // savebtn.setText("Update")
    }

}



