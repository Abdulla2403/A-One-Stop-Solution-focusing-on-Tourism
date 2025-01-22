package com.project.onestop.adapter

import android.app.DatePickerDialog
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.project.onestop.R
import com.project.onestop.databinding.RequestLayoutBinding
import com.project.onestop.model.Entries
import com.project.onestop.utils.spanned
import kotlinx.coroutines.selects.select
import java.util.Calendar

class ListOutAdapter(
    private val context: Context,
    private var requestsList: List<Entries>,
    private val onItemClick: (Entries,String,String) -> Unit,
    private val onvierccclik: (Entries) -> Unit,
    private val intent1: String
) : RecyclerView.Adapter<ListOutAdapter.ViewHolder>() {
    var date = ""
    var message = ""

    inner class ViewHolder(val binding: RequestLayoutBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RequestLayoutBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = requestsList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val request = requestsList[position]
        with(holder.binding) {

            message = editTextText2.text.toString()
            // Debugging role for each position
            Log.d("ListOutAdapter", "Binding item at position $position with role: ${request.role}")

            // Set basic information
            tvPetName.text = spanned("<b>${intent1} Name:</b> ${request.name}")
            tvPetAge.text = spanned("<b>Mobile:</b> ${request.mobile}")

            // Load image using Glide
            Glide.with(holder.itemView.context)
                .load(request.image)
                .placeholder(R.drawable.baseline_upload_24)
                .error(R.drawable.ic_launcher_foreground)
                .into(ivProduct)

            // Role-based visibility logic
            when (request.role) {
                "Hotel" -> {
                    ivNewIcon.visibility = View.VISIBLE
                    editTextText2.visibility = View.VISIBLE // Hidden for Hotel

                    // Date Picker for Hotel
                    ivNewIcon.setOnClickListener {

                        showDatePickerDialog(context) { selectedDate ->
                            date = selectedDate
                            Toast.makeText(
                                context,
                                "Selected Date for ${request.name}: $selectedDate",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
                "Food Hub" -> {
                    ivNewIcon.visibility = View.GONE
                    editTextText2.visibility = View.VISIBLE
                    message = editTextText2.text.toString()
                }
                else -> {
                    ivNewIcon.visibility = View.GONE
                    editTextText2.visibility = View.GONE
                }
            }

            // Handle clicks
            ivCall.setOnClickListener {
                Toast.makeText(context, "Calling ${request.mobile}", Toast.LENGTH_SHORT).show()
            }

            btnSendRequest.setOnClickListener {
                message = editTextText2.text.toString()
                onItemClick(request, date, message)
                editTextText2.text.clear()
            }
        }
    }


    fun updateList(newList: List<Entries>) {
        requestsList = newList
        notifyDataSetChanged()
    }

    private fun showDatePickerDialog(context: Context, onDateSelected: (String) -> Unit) {
        val calendar = Calendar.getInstance()

        val datePickerDialog = DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                val selectedDate = "$dayOfMonth/${month + 1}/$year"
                onDateSelected(selectedDate)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        datePickerDialog.datePicker.minDate = System.currentTimeMillis()
        datePickerDialog.show()
    }
}
