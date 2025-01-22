package com.project.onestop.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.project.onestop.R
import com.project.onestop.databinding.ActivityAddNgoBinding
import com.project.onestop.response.CommonResponse
import com.project.onestop.response.RetrofitInstance
import com.project.onestop.response.RetrofitInstance.TYPE
import com.project.onestop.utils.showToast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddCommonDataActivity : AppCompatActivity() {
    private val bind by lazy { ActivityAddNgoBinding.inflate(layoutInflater) }
    private lateinit var pickImageResult: ActivityResultLauncher<Intent>
    private var selectedImagePath: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(bind.root)


        val intent = "${intent.getStringExtra("role")}"


        pickImageResult =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                result.data?.data?.let { uri ->
                    selectedImagePath = uri
                    bind.image.setImageURI(uri)
                }
            }


        bind.tiName.hint = "Enter ${intent} Title"
        bind.tiEmail.hint = "Enter ${intent} Email"
        bind.tiPassword.hint = "Enter ${intent} Password"
        bind.location.hint = "Enter ${intent} Location"
        bind.mobile.hint = "Enter ${intent} Mobile No."

        bind.image.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
                type = "image/*"
            }
            pickImageResult.launch(intent)
        }

        bind.button1.setOnClickListener {
            val title = bind.tiName.text.toString()
            val email = bind.tiEmail.text.toString()
            val password = bind.tiPassword.text.toString()
            val location = bind.location.text.toString()
            val mobile = bind.mobile.text.toString()

            when {
                title.isEmpty() -> showToast("Please enter a valid title")
                email.isEmpty() || !email.contains("@gmail.com") -> showToast("Please enter a valid email")
                password.isEmpty() || password.length < 6 -> showToast("Please enter a valid password")
                location.isEmpty() -> showToast("Please enter a valid location")
                mobile.isEmpty() || mobile.length < 10 -> showToast("Please enter a valid mobile no.")
                selectedImagePath == null -> showToast("Please select an image")
                else -> {
                    bind.loadingBar.isVisible = true
                    bind.driverLayout.isVisible = false
                    CoroutineScope(IO).launch {
                        contentResolver.openInputStream(selectedImagePath!!)?.readBytes()
                            ?.let { it: ByteArray ->
                                RetrofitInstance.instance.userRegister(
                                    name = title,
                                    mobile = mobile,
                                    password = password,
                                    location = location,
                                    email = email,
                                    role = intent,
                                    type = TYPE,
                                    Base64.encodeToString(
                                        it, Base64.NO_WRAP
                                    )
                                ).enqueue(object : Callback<CommonResponse?> {
                                    override fun onResponse(
                                        p0: Call<CommonResponse?>,
                                        p1: Response<CommonResponse?>,
                                    ) {
                                        if (p1.isSuccessful) {
                                            val response = p1.body()!!
                                            if (!response.error) {
                                                showToast("$intent added Successfully")
                                                finish()
                                            } else {
                                                showToast(response.message)
                                            }
                                        } else {
                                            showToast("Response Failed")
                                        }
                                        bind.loadingBar.isVisible = false
                                        bind.driverLayout.isVisible = true
                                    }

                                    override fun onFailure(
                                        p0: Call<CommonResponse?>,
                                        p1: Throwable,
                                    ) {
                                        showToast(p1.message!!)
                                        bind.loadingBar.isVisible = false
                                        bind.driverLayout.isVisible = true
                                    }
                                })
                            }

                    }

                }
            }


        }
    }
}