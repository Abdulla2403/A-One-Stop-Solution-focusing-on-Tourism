package com.project.onestop.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.project.onestop.R
import com.project.onestop.databinding.RequestLayoutBinding
import com.project.onestop.model.Requests
import com.project.onestop.utils.spanned

class NgoAdapter(
    private val context: Context,
    private val requestsList: List<Requests>,
    private val onItemClick: (Requests) -> Unit,
    var Intent1:String
) : RecyclerView.Adapter<NgoAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: RequestLayoutBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RequestLayoutBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = requestsList.size

    @SuppressLint("QueryPermissionsNeeded")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val request = requestsList[position]
        with(holder.binding) {
            val details = request.requestsDetails.split(",")
            val name = details.getOrNull(0) ?: "Unknown"
            val age = details.getOrNull(1) ?: "N/A"

            tvPetName.text = spanned("<b>${Intent1} Name:</b> $name")
            tvPetAge.text = spanned("<b>P Age:</b> $age")

            Glide.with(holder.itemView.context)
                .load(request.image)
                .placeholder(R.drawable.baseline_upload_24)
                .error(R.drawable.ic_launcher_foreground)
                .into(ivProduct)

            btnSendRequest.setOnClickListener {
                onItemClick(request)
            }

            ivCall.setOnClickListener {
                val userMobile = request.ngoMobile
                if (userMobile.isNotEmpty()) {
                    val dialIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$userMobile"))
                    dialIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    context.startActivity(dialIntent)
                } else {
                    Toast.makeText(context, "Phone number is unavailable.", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }
}
