package br.com.tiago.vanguarda_messaging.network

import br.com.tiago.vanguarda_messaging.data.model.Sign
import br.com.tiago.vanguarda_messaging.network.model.SignNetworkEntity
import br.com.tiago.vanguarda_messaging.network.remote.SignsApi
import br.com.tiago.vanguarda_messaging.network.request.TranslateBody
import br.com.tiago.vanguarda_messaging.network.responses.TranslationResponse
import br.com.tiago.vanguarda_messaging.util.Resource
import dagger.hilt.android.scopes.ActivityScoped
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import retrofit2.Response
import javax.inject.Inject

@ActivityScoped
class SignsRepository @Inject constructor(
    private val api: SignsApi
) {

    suspend fun translate(translateBody: TranslateBody?): Flow<Resource<*>> {
        return channelFlow {

            val response = api.translate(translateBody)

            when {
                response.isSuccessful -> {
                    val translatedObj = response.body()?.results
                    channel.trySend(Resource.Success(translatedObj)).isSuccess
                }
                else ->{
                    val error = response.message()
                    channel.trySend(Resource.Error(message = error,data = null)).isFailure
                }
            }
            awaitClose {  }
        }
    }
}