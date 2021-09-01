package br.com.tiago.vanguarda_messaging.network.responses

import br.com.tiago.vanguarda_messaging.network.model.SignNetworkEntity
import com.google.gson.annotations.SerializedName

data class SignsResponse(
    @SerializedName("results") val results: List<SignNetworkEntity>? = null
)


data class TranslationResponse(
    @SerializedName("results") val results: List<List<SignNetworkEntity>>? = null
)