package com.project.onestop.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.onestop.adapter.ListOutAdapter
import com.project.onestop.chatbot.ChatActivity
import com.project.onestop.databinding.ActivityUserDashboardBinding
import com.project.onestop.model.Entries
import com.project.onestop.response.CommonResponse
import com.project.onestop.response.RetrofitInstance
import com.project.onestop.response.RetrofitInstance.TYPE
import com.project.onestop.utils.SessionManager
import com.project.onestop.utils.showToast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Locale

class UserDashboard : AppCompatActivity() {
    private val bind by lazy { ActivityUserDashboardBinding.inflate(layoutInflater) }
    private val shared by lazy { SessionManager(applicationContext) }
    private lateinit var listOutAdapter: ListOutAdapter
    private val simple = SimpleDateFormat("dd MMMM yyyy HH:mm:ss", Locale.getDefault())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(bind.root)

        // Initialize adapter
        listOutAdapter = ListOutAdapter(
            this@UserDashboard,
            emptyList(),
            { request,date,message -> sendRequest(request,date,message)
            },
            { /* Unused onvierccclik callback */ },
            ""
        )
        bind.rvzl.adapter = listOutAdapter
        bind.rvzl.layoutManager = LinearLayoutManager(this)

        // Set up UI actions
        bind.chat.setOnClickListener {
            startActivity(Intent(this, ChatActivity::class.java))
        }

        bind.usetTxt.text = "Welcome\n ${shared.getUserName()} 😀"

        bind.profile.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }

        bind.requestes.setOnClickListener {
            startActivity(Intent(this, ViewUserOrderActivity::class.java))
        }

        // Fetch data
        getData()
    }

    private fun getData() {
        RetrofitInstance.instance.getAll().enqueue(object : Callback<CommonResponse?> {
            override fun onResponse(call: Call<CommonResponse?>, response: Response<CommonResponse?>) {
                val data = response.body()?.data
                if (data.isNullOrEmpty()) {
                    showToast("No Entries available")
                    listOutAdapter.updateList(emptyList())
                } else {
                    val list = data.filter { it.type == TYPE && it.role != "User" }
                    listOutAdapter.updateList(list)
                }
            }

            override fun onFailure(call: Call<CommonResponse?>, t: Throwable) {
                showToast(t.message ?: "Error fetching data")
            }
        })
    }

    private fun sendRequest(it: Entries, date: String, message: String) {
        RetrofitInstance.instance.sendNgoRequests(
            ngoMobile = it.mobile,
            userMobile = "${shared.getUserMobile()}",
            userName = "${shared.getUserName()}",
            "${shared.getUserId()}",
            "${it.id}",
            image = "${it.image}",
            ngoName = it.name,
            requestsDetails = "${it.role},${shared.getUserEmail()}",
            time = "${System.currentTimeMillis()}",
            status = "Requested",
            TYPE,
            "$date", "$message"
        ).enqueue(object : Callback<CommonResponse?> {
            override fun onResponse(call: Call<CommonResponse?>, response: Response<CommonResponse?>) {
                val result = response.body()
                if (result?.error == true) {
                    showToast("Error occurred")
                } else {
                    showToast("Action taken")
                }
            }

            override fun onFailure(call: Call<CommonResponse?>, t: Throwable) {
                showToast(t.message ?: "Error sending request")
            }
        })
    }

    override fun onResume() {
        super.onResume()
        getData()
    }
}
