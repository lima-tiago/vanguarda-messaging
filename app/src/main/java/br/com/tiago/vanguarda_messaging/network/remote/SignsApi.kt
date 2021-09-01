package br.com.tiago.vanguarda_messaging.network.remote

import br.com.tiago.vanguarda_messaging.network.request.TranslateBody
import br.com.tiago.vanguarda_messaging.network.responses.SignsResponse
import br.com.tiago.vanguarda_messaging.network.responses.TranslationResponse
import br.com.tiago.vanguarda_messaging.util.Resource
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface SignsApi {

    @GET("signs")
    suspend fun getSigns(): SignsResponse

    @POST("translate")
    suspend fun translate(
        @Body body: TranslateBody?
    ): Response<TranslationResponse>
}