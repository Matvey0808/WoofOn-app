package com.woofon.data.viewmodels

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import com.woofon.data.viewmodels.models.KeyModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ManageKeysViewModel : ViewModel() {
    private val _textTitleKey = MutableStateFlow("")
    private val _isActiveDialog = MutableStateFlow(false)
    private val _listKeys = MutableStateFlow<List<KeyModel>>(emptyList())
    private val _isError = MutableStateFlow(false)
    var listFilesUri = MutableStateFlow<List<Uri?>>(emptyList())
    val textTitleKey: StateFlow<String> = _textTitleKey.asStateFlow()
    val isActiveDialog: StateFlow<Boolean> = _isActiveDialog.asStateFlow()
    val listKeys: StateFlow<List<KeyModel>> = _listKeys.asStateFlow()
    val isError: StateFlow<Boolean> = _isError.asStateFlow()

    fun textInField(titleKey: String) {
        _textTitleKey.value = titleKey
        _isError.value = false
    }

    fun toggleDialog() {
        _isActiveDialog.value = !_isActiveDialog.value
        _textTitleKey.value = ""
    }

    fun addKey(key: String, title: String) {
        if (_textTitleKey.value.isEmpty()) {
            _isError.value = true
        } else {
            _listKeys.value += KeyModel(title, key)
            _isActiveDialog.value = false
            _textTitleKey.value = ""
            Log.d("ADD", "${_listKeys.value}")
            Log.d("KEY", "${listFilesUri.value}")
        }
    }

    fun cancelKey() {
        listFilesUri.update { list ->
            list.dropLast(1)
        }
        Log.d("CANCEL", "${listFilesUri.value}")
    }
}