package com.project.onestop.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.onestop.databinding.ActivityViewListBinding
import com.project.onestop.response.CommonResponse
import com.project.onestop.response.RetrofitInstance
import com.project.onestop.utils.showToast
import com.project.onestop.adapter.NGOListAdapter
import com.project.onestop.response.RetrofitInstance.TYPE
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ViewList : AppCompatActivity() {
    private val bind by lazy { ActivityViewListBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(bind.root)

        val intent = intent.getStringExtra("role")

        bind.textView2.text = "$intent's List"
        showToast("$intent")
        CoroutineScope(IO).launch {
            RetrofitInstance.instance.getRole("$intent").enqueue(object :
                Callback<CommonResponse?> {
                override fun onResponse(
                    p0: Call<CommonResponse?>,
                    p1: Response<CommonResponse?>,
                ) {
                    if (p1.isSuccessful) {
                        val response = p1.body()!!
                        if (response.error) {
                            showToast("Error Occurred")
                        } else {
                            val sellers = response.data.filter { it.type == TYPE }
                            if (sellers.isEmpty()) {
                                showToast("No Records")
                            } else {
                                bind.rvzlIst.adapter = NGOListAdapter(sellers) {
                                    showToast(it.name)
                                }
                                bind.rvzlIst.layoutManager = LinearLayoutManager(this@ViewList)
                            }
                        }

                    } else {
                        showToast("Failed to load the list")
                    }
                    bind.progressBar4.isVisible = false
                }

                override fun onFailure(p0: Call<CommonResponse?>, p1: Throwable) {
                    showToast(p1.message!!)
                    bind.progressBar4.isVisible = false

                }
            })
        }


    }
}