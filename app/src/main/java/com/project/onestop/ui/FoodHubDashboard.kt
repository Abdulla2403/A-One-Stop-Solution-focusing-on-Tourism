package com.project.onestop.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import com.project.onestop.databinding.ActivityCabDashboardBinding
import com.project.onestop.databinding.ActivityFoodDashboardBinding
import com.project.onestop.utils.SessionManager

class FoodHubDashboard : AppCompatActivity() {
    private val bind by lazy { ActivityFoodDashboardBinding.inflate(layoutInflater) }
    private val shared by lazy { SessionManager(applicationContext) }

    private lateinit var pickImageResult: ActivityResultLauncher<Intent>
    private var selectedImagePath: Uri? = null
    var id = ""
    var rating = ""
    var name = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(bind.root)

        id = shared.getUserId()!!
        val role = shared.getUserRole()!!
        rating = shared.getFarmerRating()!!
        name = shared.getUserName()!!
        bind.usetTxt.text = "Welcome ${shared.getUserName()}"

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



}