package br.com.tiago.vanguarda_messaging.network.request

import com.google.gson.annotations.SerializedName

data class TranslateBody(
    @field:SerializedName("phrase") val phrase:String
)