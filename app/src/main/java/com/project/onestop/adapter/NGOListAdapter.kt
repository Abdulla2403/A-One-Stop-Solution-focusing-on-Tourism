package com.project.onestop.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.project.onestop.databinding.ViewNgoBinding
import com.project.onestop.model.Entries
import com.project.onestop.utils.spanned

class NGOListAdapter(var list: List<Entries>, var onclick: (Entries) -> Unit) :
    RecyclerView.Adapter<NGOListAdapter.ViewNGO>() {
    class ViewNGO(val binding: ViewNgoBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(entries: Entries) {
            binding.sellerName.text = spanned("<b>Title:</b> ${entries.name}")
            binding.sellerLocation.text = spanned("<b>Mobile:</b> ${entries.mobile}")
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewNGO {
        val view = ViewNgoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewNGO(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewNGO, position: Int) {
        holder.bind(list[position])
        holder.binding.root.setOnClickListener {
            onclick(list[position])
        }
    }
}