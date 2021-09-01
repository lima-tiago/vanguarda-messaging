package br.com.tiago.vanguarda_messaging.domain

import br.com.tiago.vanguarda_messaging.util.Resource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn

abstract class FlowUseCase<in P, R>(private val coroutineDispatcher: CoroutineDispatcher) {

    suspend operator fun invoke(parameters: P?): Flow<Resource<R>> {
        return execute(parameters)
            .catch { e -> emit(Resource.Error(message = Exception(e).message.toString())) }
            .flowOn(coroutineDispatcher)
    }

    abstract suspend fun execute(parameters: P?): Flow<Resource<R>>
}