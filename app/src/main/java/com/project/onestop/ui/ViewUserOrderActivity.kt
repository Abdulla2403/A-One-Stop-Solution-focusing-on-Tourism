package com.project.onestop.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.onestop.databinding.ActivityViewUserOrderBinding
import com.project.onestop.response.ProductResponse
import com.project.onestop.response.RetrofitInstance
import com.project.onestop.utils.SessionManager
import com.project.onestop.utils.showToast
import com.project.onestop.adapter.RequestAdapter
import com.project.onestop.adapter.RequestAdapter2
import com.project.onestop.response.RetrofitInstance.TYPE
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ViewUserOrderActivity : AppCompatActivity() {
    private val bind by lazy { ActivityViewUserOrderBinding.inflate(layoutInflater) }
    private val shared by lazy { SessionManager(this) }
    private lateinit var orderAdapter: RequestAdapter2
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(bind.root)
        val id = shared.getUserId()!!

        orderAdapter = RequestAdapter2({

        },{

        },"User")

        bind.rvList.layoutManager = LinearLayoutManager(this)
        bind.rvList.adapter = orderAdapter

        CoroutineScope(IO).launch {
            RetrofitInstance.instance.getPostedbyUserid("$id",TYPE)
                .enqueue(object : Callback<ProductResponse?> {
                    override fun onResponse(
                        p0: Call<ProductResponse?>,
                        p1: Response<ProductResponse?>
                    ) {
                        val response = p1.body()!!
                        if (response.error) {
                            showToast("Error occurred")
                        } else {
                            response.data6?.let {
                                if (it.isEmpty()) {
                                    showToast("No Records")
                                } else {
                                    orderAdapter.submitList(it)
                                }

                            }
                        }
                    }

                    override fun onFailure(p0: Call<ProductResponse?>, p1: Throwable) {
                        showToast(p1.message!!)
                    }
                })
        }


    }
}