package com.project.onestop.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.project.onestop.databinding.ActivityAdminDashboardBinding

import com.project.onestop.utils.SessionManager


class AdminDashboard : AppCompatActivity() {
    private val bind by lazy { ActivityAdminDashboardBinding.inflate(layoutInflater) }
    private val shared by lazy { SessionManager(this) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(bind.root)

        bind.logout.setOnClickListener {
            MaterialAlertDialogBuilder(this)
                .setTitle("Logout")
                .setMessage("Are you sure you want to logout?")
                .setPositiveButton("Confirm") { _, _ ->
                    shared.clearLoginState()
                    finishAffinity()
                    startActivity(Intent(this@AdminDashboard, LoginActivity::class.java))
                }
                .setNegativeButton("Dismiss") { dialog, _ ->
                    dialog.dismiss()
                }
                .create()
                .show()
        }

        bind.addCabs.setOnClickListener {
            startActivity(Intent(this, AddCommonDataActivity::class.java).apply {
                putExtra( "role","Cab")
            })
        }
        bind.addEvents.setOnClickListener {
            startActivity(Intent(this, AddCommonDataActivity::class.java).apply {
                putExtra( "role","Event")
            })
        }
        bind.addHotel.setOnClickListener {
            startActivity(Intent(this, AddCommonDataActivity::class.java).apply {
                putExtra( "role","Hotel")
            })
        }
        bind.addFoodHub.setOnClickListener {
            startActivity(Intent(this, AddCommonDataActivity::class.java).apply {
                putExtra( "role","Food Hub")
            })
        }
        bind.viewEvents.setOnClickListener {
            startActivity(Intent(this, ViewList::class.java).apply {
                putExtra( "role","Event")
            })
        }


        bind.viewCabs.setOnClickListener {
            startActivity(Intent(this, ViewList::class.java).apply {
                putExtra( "role","Cab")
            })
        }
        bind.viewfoodhub.setOnClickListener {
            startActivity(Intent(this, ViewList::class.java).apply {
                putExtra( "role","Food Hub")
            })
        }
        bind.viewHotel.setOnClickListener {
            startActivity(Intent(this, ViewList::class.java).apply {
                putExtra( "role","Hotel")
            })
        }


    }
}