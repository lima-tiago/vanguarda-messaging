package br.com.tiago.vanguarda_messaging.network.model

import com.google.gson.annotations.SerializedName

data class SignNetworkEntity(
    @SerializedName("letter") val letter: String? = null,
    @SerializedName("url") val url: String? = null
)
