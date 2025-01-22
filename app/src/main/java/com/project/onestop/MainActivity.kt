package com.project.onestop

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.project.onestop.databinding.ActivityMainBinding
import com.project.onestop.ui.LoginActivity

class MainActivity : AppCompatActivity() {
    private val bind by lazy { ActivityMainBinding.inflate(layoutInflater) }
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(bind.root)

        val image = bind.yuiop
        val anim = AnimationUtils.loadAnimation(this, R.anim.image_animation)

        image.startAnimation(anim)

        anim.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {}

            override fun onAnimationEnd(animation: Animation?) {
                startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                finish()
            }

            override fun onAnimationRepeat(animation: Animation?) {}
        })


    }
}