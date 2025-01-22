package com.project.onestop.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.onestop.adapter.RequestAdapter
import com.project.onestop.databinding.ActivityOrderDetailsBinding
import com.project.onestop.model.Requests
import com.project.onestop.response.CommonResponse
import com.project.onestop.response.ProductResponse
import com.project.onestop.response.RetrofitInstance
import com.project.onestop.response.RetrofitInstance.TYPE
import com.project.onestop.utils.SessionManager
import com.project.onestop.utils.showToast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RequestDetailsActivity : AppCompatActivity() {
    private val bind by lazy { ActivityOrderDetailsBinding.inflate(layoutInflater) }
    private val shared by lazy { SessionManager(applicationContext) }
    var id = ""
    private lateinit var requestAdapter: RequestAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(bind.root)


        val intent = intent.getStringExtra("request")!!

        bind.textUser.text = "$intent List"

        val actualStatus = when(intent){
            "Requested" -> "Requested"
            "Approved" -> "Approved"
            "Completed" -> "Completed"
            else -> "Approved"
        }

        val newstatus = when(intent){
            "Requested" -> "Approved"
            "Approved" -> "Completed"
            else -> "Approved"
        }

        requestAdapter = RequestAdapter(
            itemOnClick = {

            },
            statusOnClick = {
                updateRequest(it,newstatus)
            },
            role = "${shared.getUserRole()}"
        )


        bind.rvListy.adapter = requestAdapter
        bind.rvListy.layoutManager = LinearLayoutManager(this)


        CoroutineScope(IO).launch {
            RetrofitInstance.instance.getPostedbyngoUid("${shared.getUserId()}", TYPE,actualStatus)
                .enqueue(object : Callback<ProductResponse?> {
                    override fun onResponse(
                        p0: Call<ProductResponse?>,
                        p1: Response<ProductResponse?>,
                    ) {
                        val response = p1.body()!!
                        val list = response.data6!!
                        if (list.isNotEmpty()) {
                            if (!response.error) {
                                response.data6?.let { requestAdapter.submitList(it) }
                            } else {

                            }
                        } else {
                            showToast("No Requests Placed")
                        }

                    }

                    override fun onFailure(p0: Call<ProductResponse?>, p1: Throwable) {
                        showToast(p1.message!!)
                    }
                })
        }



    }

    private fun updateRequest(it: Requests, s: String) {
        CoroutineScope(IO).launch {
            RetrofitInstance.instance.updateNgoRequest(
                status = s,
                it.id,
                ngoid = "${shared.getUserId()}",
                userid = it.userid,
                type = TYPE
            ).enqueue(object : Callback<CommonResponse?> {
                override fun onResponse(p0: Call<CommonResponse?>, p1: Response<CommonResponse?>) {
                    val response = p1.body()!!
                    if (!response.error){
                        showToast("Status Updated to $s")
                    }else{
                        showToast("Failed to update the status")
                    }
                }

                override fun onFailure(p0: Call<CommonResponse?>, p1: Throwable) {
                    showToast(p1.message!!)
                }
            })
        }
    }
}