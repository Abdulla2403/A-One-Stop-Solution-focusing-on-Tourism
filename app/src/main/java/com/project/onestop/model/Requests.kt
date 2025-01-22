package com.project.onestop.model

import androidx.core.app.NotificationCompat.MessagingStyle.Message

data class Requests (
    var id:Int,
    var ngoMobile:String,
    var userMobile:String,
    var userName:String,
    var image:String,
    var userid:String,
    var ngoid:String,
    var type:String,
    var ngoName:String,
    var requestsDetails:String,
    var time:String,
    var status:String,
    var date:String,
    var message: String
)