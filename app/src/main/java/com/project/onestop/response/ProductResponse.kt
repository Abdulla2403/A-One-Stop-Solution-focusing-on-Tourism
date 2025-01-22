package com.project.onestop.response

import com.project.onestop.model.Requests


data class ProductResponse(
    var error: Boolean,
    var message: String,
    var data6: ArrayList<Requests>? = arrayListOf()
)