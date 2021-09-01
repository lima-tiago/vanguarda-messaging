package br.com.tiago.vanguarda_messaging.domain

import br.com.tiago.vanguarda_messaging.data.CoroutineDispatcherProvider
import br.com.tiago.vanguarda_messaging.network.SignsRepository
import br.com.tiago.vanguarda_messaging.network.request.TranslateBody
import br.com.tiago.vanguarda_messaging.network.responses.TranslationResponse
import br.com.tiago.vanguarda_messaging.util.Resource
import dagger.hilt.android.scopes.ActivityScoped
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.channelFlow
import javax.inject.Inject
import javax.inject.Provider

@ExperimentalCoroutinesApi
@ActivityScoped
class UseCase @Inject constructor(
    private val repository: SignsRepository,
    dispatcherProvider: CoroutineDispatcherProvider
) : FlowUseCase<TranslateBody,Any>(dispatcherProvider.io){
    override suspend fun execute(parameters: TranslateBody?): Flow<Resource<Any>> {
        return channelFlow {
            repository.translate(translateBody = parameters).collect {
                val result = when (it){
                    is Resource.Success -> it as Resource<TranslationResponse>
                    is Resource.Error -> it
                    else -> null
                }
                channel.trySend(result as Resource<Any>)
            }
        }
    }
}