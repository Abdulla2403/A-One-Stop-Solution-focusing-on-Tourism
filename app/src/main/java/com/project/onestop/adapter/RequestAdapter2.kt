package com.project.onestop.adapter

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.icu.util.Calendar
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.project.onestop.databinding.RequestLayoutBinding
import com.project.onestop.model.Requests
import com.project.onestop.utils.spanned

class RequestAdapter2(
    private val itemOnClick: (Requests) -> Unit,
    private val statusOnClick: (Requests) -> Unit,
    private val role: String
) : ListAdapter<Requests, RequestAdapter2.RequestViewHolder>(RequestDiffCallback()) {
    var mobile = ""

    inner class RequestViewHolder(val binding: RequestLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(request: Requests) {
            val details = request.requestsDetails.split(",")
            binding.tvPetName.text = spanned("<b>Name:</b> ${request.ngoName}")
            binding.tvPetAge.text = spanned("<b>Mobile:</b> ${request.ngoMobile}")
            binding.tvStatus.text = spanned("<b>Status:</b> ${request.status}")




            Glide.with(binding.ivProduct.context)
                .load(request.image)
                .centerCrop()
                .into(binding.ivProduct)

            if (role != "User") {
                binding.btnSendRequest.visibility = View.VISIBLE
                mobile = request.userMobile



                when (request.status) {
                    "Requested" -> {
                        binding.btnSendRequest.text = "Approve Request"
                        binding.tvStatus.visibility = View.GONE
                    }

                    "Approved" -> {
                        binding.btnSendRequest.text = "Completed"
                        binding.tvStatus.visibility = View.GONE
                    }

                    "Completed" -> {
                        binding.btnSendRequest.visibility = View.GONE
                        binding.tvStatus.text = spanned("<b>Status:</b> ${request.status}")
                    }

                    else -> {
                        binding.btnSendRequest.visibility = View.GONE

                        binding.tvPetName.text = spanned("<b>Name:</b> ${request.ngoName}")
                        binding.tvPetAge.text = spanned("<b>Mobile:</b> ${request.ngoMobile}")
                        binding.tvStatus.text = spanned("<b>Status:</b> ${request.status}")

                    }
                }
            } else {
                binding.btnSendRequest.visibility = View.GONE
                mobile = request.ngoMobile

                when (request.status) {
                    "Requested" -> {
                        binding.tvStatus.text = "Pending for approval"
                        binding.tvStatus.visibility = View.VISIBLE
                    }

                    "Approved" -> {
                        binding.tvStatus.text = "Request has been approved"
                        binding.tvStatus.visibility = View.VISIBLE
                    }

                    "Completed" -> {
                        binding.tvStatus.text = spanned("<b>Status:</b> ${request.status}")
                        binding.tvStatus.visibility = View.VISIBLE
                    }

                    else -> {
                        binding.tvStatus.visibility = View.GONE
                    }
                }
            }

            binding.root.setOnClickListener {
                itemOnClick(request)
            }

            binding.ivCall.setOnClickListener {
                val userMobile = request.userMobile
                if (userMobile.isNotEmpty()) {
                    val dialIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$userMobile"))
                    binding.root.context.startActivity(dialIntent)
                } else {
                    Toast.makeText(
                        binding.root.context,
                        "Phone number is unavailable.",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            }

            binding.btnSendRequest.setOnClickListener {
                statusOnClick(request)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RequestViewHolder {
        val binding =
            RequestLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RequestViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RequestViewHolder, position: Int) {
        holder.bind(getItem(position))

    }

    class RequestDiffCallback : DiffUtil.ItemCallback<Requests>() {
        override fun areItemsTheSame(oldItem: Requests, newItem: Requests): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Requests, newItem: Requests): Boolean {
            return oldItem == newItem
        }
    }


}