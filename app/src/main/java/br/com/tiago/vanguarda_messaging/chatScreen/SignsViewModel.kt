package br.com.tiago.vanguarda_messaging.chatScreen

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.tiago.vanguarda_messaging.data.model.Sign
import br.com.tiago.vanguarda_messaging.data.model.TranslatedSigns
import br.com.tiago.vanguarda_messaging.domain.UseCase
import br.com.tiago.vanguarda_messaging.network.model.SignNetworkEntity
import br.com.tiago.vanguarda_messaging.network.request.TranslateBody
import br.com.tiago.vanguarda_messaging.network.responses.TranslationResponse
import br.com.tiago.vanguarda_messaging.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.ArrayList
import javax.inject.Inject

@HiltViewModel
class SignsViewModel @ExperimentalCoroutinesApi
@Inject constructor(
    private val useCase: UseCase
) : ViewModel() {

    private val _translated = MutableLiveData<List<List<Sign>>>(listOf())

    val translated: MutableLiveData<List<List<Sign>>> = _translated
    val loadError = mutableStateOf("")
    val isLoading = mutableStateOf(false)

    init {

    }

    fun translate(translateBody: TranslateBody) {
        viewModelScope.launch {
            isLoading.value = true
//            val result = useCase. .translate(translateBody)
//            when (result) {
//                is Resource.Success -> {
//                    val translatedRes = result.data?.results?.map()!!
//
//                    loadError.value = ""
//                    isLoading.value = false
//                    translated.value = translatedRes
//                    Timber.d(translated.toString(), "")
//                }
//
//                is Resource.Error -> {
//                    loadError.value = result.message!!
//                    isLoading.value = false
//                }
//            }
        }
    }

    fun clear() {
        viewModelScope.launch {
            _translated.value = listOf()
        }
    }

    fun translateAddMessage(translateBody: TranslateBody) {
        isLoading.value = true
        viewModelScope.launch {
            val result = async {
                useCase(translateBody)
            }
            result.await().catch { }
                .collect {
                    when (it) {
                        is Resource.Success<*> -> {
                            val translatedRes =
                                (it.data as ArrayList<ArrayList<SignNetworkEntity>>).map()

                            loadError.value = ""
                            isLoading.value = false
                            _translated.value = translatedRes
//                            param.invoke(translatedRes)
                            Timber.tag("SignsViewModel").d(translated.toString(), "")
                        }

                        is Resource.Error -> {
                            loadError.value = it.message.toString()
                            isLoading.value = false
                        }
                    }
                }
        }
    }

    private fun ArrayList<ArrayList<SignNetworkEntity>>.map(): List<List<Sign>> {
        val listParent = mutableListOf<List<Sign>>()
        this.forEach {
            val listChild = mutableListOf<Sign>()
            it.forEach { sign ->
                listChild.add(sign.map())
            }
            listParent.add(listChild)
        }
        return listParent
    }

    private fun SignNetworkEntity.map(): Sign {
        return Sign(
            letter = letter.toString(),
            url = url.toString()
        )

    }
}

private fun ArrayList<ArrayList<SignNetworkEntity>>?.map(): MutableList<List<SignNetworkEntity>> {
    val listParent = mutableListOf<List<SignNetworkEntity>>()
    this?.forEach {
        listParent.add(it.toList())
    }
    return listParent
}
