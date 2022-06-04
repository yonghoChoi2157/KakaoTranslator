package com.yh.kakaotranslator

import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yh.kakaotranslator.data.repo.KakaoRepository
import com.yh.kakaotranslator.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: KakaoRepository) : ViewModel() {

    private val _mainViewStateLiveData = MutableLiveData<MainViewState>()
    val mainViewStateLiveData: LiveData<MainViewState>
        get() = _mainViewStateLiveData

    val inputTextLiveData = MutableLiveData<String>()
    val langObservableField = ObservableField<String>()

    fun translate() {
        CoroutineScope(Dispatchers.IO).launch {
            if (!inputTextLiveData.value.isNullOrEmpty()) {
                when (val result = repository.getText(
                    query = inputTextLiveData.value!!,
                    srcLang = "kr",
                    targetLang = langObservableField.get()!!,
                )) {
                    is Result.Success -> {
                        viewModelScope.launch {
                            _mainViewStateLiveData.value =
                                MainViewState.GetText(result.data.translated_text)
                        }
                    }
                    is Result.Error -> {
                        viewModelScope.launch {
                            _mainViewStateLiveData.value =
                                MainViewState.Error("에러가 발생.")
                        }
                    }
                }
            }
        }
    }
}