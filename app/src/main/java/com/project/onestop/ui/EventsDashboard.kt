package com.project.onestop.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.project.onestop.databinding.ActivityEventDashboardBinding
import com.project.onestop.databinding.DialogAddProductsBinding
import com.project.onestop.response.CommonResponse
import com.project.onestop.response.RetrofitInstance
import com.project.onestop.response.RetrofitInstance.TYPE
import com.project.onestop.utils.SessionManager
import com.project.onestop.utils.showToast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.async
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EventsDashboard : AppCompatActivity() {
    private val bind by lazy { ActivityEventDashboardBinding.inflate(layoutInflater) }
    private val shared by lazy { SessionManager(applicationContext) }

    private lateinit var pickImageResult: ActivityResultLauncher<Intent>
    private var selectedImagePath: Uri? = null
    var id = ""
    var rating = ""
    var name = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(bind.root)

        pickImageResult =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                result.data?.data?.let { uri ->
                    selectedImagePath = uri
                    currentDialog?.ivProduct?.setImageURI(uri)



                }
            }

        id = shared.getUserId()!!
        val role = shared.getUserRole()!!
        rating = shared.getFarmerRating()!!
        name = shared.getUserName()!!
        bind.usetTxt.text = "Welcome\nto\nthe\n NGO Dashboard"

        bind.profile.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }

        bind.requested.setOnClickListener {
            startActivity(Intent(this, RequestDetailsActivity::class.java).apply {
                putExtra("request", "Requested")
            })
        }

        bind.accepted.setOnClickListener {
            startActivity(Intent(this, RequestDetailsActivity::class.java).apply {
                putExtra("request", "Approved")
            })
        }

        bind.completed.setOnClickListener {
            startActivity(Intent(this, RequestDetailsActivity::class.java).apply {
                putExtra("request", "Completed")
            })
        }


    }

    private var currentDialog: DialogAddProductsBinding? = null

    private fun addItems() {
        val view = DialogAddProductsBinding.inflate(layoutInflater)
        val dialog = AlertDialog.Builder(this).setView(view.root).create()
        currentDialog = view

        view.ivProduct.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
                type = "image/*"
            }
            pickImageResult.launch(intent)
        }


        view.sendRequest.setOnClickListener {
            val name1 = view.etName.text.toString()
            val priceStr = view.info.text.toString().trim()
            when {
                name1.isEmpty() -> showToast("Please enter Pet Name")
                priceStr.isEmpty() -> showToast("Please enter Age")
                selectedImagePath==null -> showToast("Please select the image from the gallery")
                else -> {

                    CoroutineScope(IO).async{
                        contentResolver.openInputStream(selectedImagePath!!)?.readBytes()?.let{ it: ByteArray ->
                            RetrofitInstance.instance.sendNgoRequests(
                                ngoMobile = "${shared.getUserMobile()}",
                                userMobile = "",
                                userName = "",
                                "",
                                "${shared.getUserId()}",
                                image = Base64.encodeToString(
                                    it, Base64.NO_WRAP
                                ),
                                ngoName = "${shared.getUserName()}",
                                requestsDetails = "${name1},${priceStr}",
                                time = "",
                                status = "Posted",
                                TYPE,
                                ""
                                ,""
                            ).enqueue(object :
                                Callback<CommonResponse?> {
                                override fun onResponse(
                                    p0: Call<CommonResponse?>,
                                    p1: Response<CommonResponse?>
                                ) {
                                    val response = p1.body()!!
                                    if (response.error) {
                                        showToast("Error occurred")
                                    } else {
                                        runOnUiThread {
                                            showToast("Added for Adoption")

                                        }
                                    }
                                }

                                override fun onFailure(p0: Call<CommonResponse?>, p1: Throwable) {
                                    showToast(p1.message!!)
                                }
                            })
                        }
                    }.start()
                }
            }
            dialog.dismiss()
        }
        dialog.show()


    }
}