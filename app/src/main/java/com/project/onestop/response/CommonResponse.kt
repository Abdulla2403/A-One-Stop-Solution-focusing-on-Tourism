package com.project.onestop.response

import com.project.onestop.model.Entries

data class CommonResponse(
    var error:Boolean,
    var message:String,
    var data: ArrayList<Entries>

)
