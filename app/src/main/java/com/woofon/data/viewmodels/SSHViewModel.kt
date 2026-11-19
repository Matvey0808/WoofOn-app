package com.woofon.data.viewmodels

import android.app.Application
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.woofon.data.viewmodels.db.SSHDataBase
import com.woofon.data.viewmodels.models.KeyModel
import com.woofon.data.viewmodels.models.SSHModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.collections.minus
import kotlin.collections.plus
import kotlin.time.ExperimentalTime

enum class TagTextFieldSSH {
    DEVICE,
    USERNAME,
    HOST,
    PORT,
    PASSWORD
}

@OptIn(ExperimentalTime::class)
class SSHViewModel(application: Application) : AndroidViewModel(application) {
    private val _isActiveDialog = MutableStateFlow(false)
    private val _isActivePasswordDialog = MutableStateFlow(false)
    private val _textDeviceName = MutableStateFlow("")
    private val _textUsername = MutableStateFlow("")
    private val _textHost = MutableStateFlow("")
    private val _textPort = MutableStateFlow("")
    private val _textPassword = MutableStateFlow("")
    private val _isError = MutableStateFlow(mutableStateListOf(false, false, false, false))
    private val _expanded = MutableStateFlow(false)
    private val _selectedKey = MutableStateFlow<KeyModel?>(null)
    private val db = SSHDataBase.getInstance(application)
    private val dao = db.getSSHDao()
    private val _currentId = MutableStateFlow(0)
    private val _listDevices = MutableStateFlow<List<SSHModel>>(emptyList())
    private val _editingDevice = MutableStateFlow<SSHModel?>(null)
    private val _isSelect = MutableStateFlow(false)
    val ssh: StateFlow<List<SSHModel>> = dao.getSSH()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    val isActiveDialog: StateFlow<Boolean> = _isActiveDialog.asStateFlow()
    val isActivePasswordDialog: StateFlow<Boolean> = _isActivePasswordDialog.asStateFlow()
    val textDeviceName: StateFlow<String> = _textDeviceName.asStateFlow()
    val textUsername: StateFlow<String> = _textUsername.asStateFlow()
    val textHost: StateFlow<String> = _textHost.asStateFlow()
    val textPort: StateFlow<String> = _textPort.asStateFlow()
    val textPassword: StateFlow<String> = _textPassword.asStateFlow()
    val isError: StateFlow<List<Boolean>> = _isError.asStateFlow()
    val expanded: StateFlow<Boolean> = _expanded.asStateFlow()
    val selectedKey: StateFlow<KeyModel?> = _selectedKey.asStateFlow()
    val currentId: StateFlow<Int> = _currentId.asStateFlow()
    val listDevices: StateFlow<List<SSHModel>> = _listDevices.asStateFlow()
    val isSelect: StateFlow<Boolean> = _isSelect.asStateFlow()
    val editingDevice: StateFlow<SSHModel?> = _editingDevice.asStateFlow()

    fun toggleDialog(dialog: String, id: Int) {
        if (dialog == "add") {
            _isActiveDialog.value = !_isActiveDialog.value
            _textDeviceName.value = ""
            _textUsername.value = ""
            _textHost.value = ""
            _textPort.value = "22"
            _isError.value.fill(false)
            _selectedKey.value = null
        } else if (dialog == "password") {
            _isActivePasswordDialog.value = !_isActivePasswordDialog.value
            _textPassword.value = ""
            _currentId.value = id
        }
    }

    fun textInField(
        tag: TagTextFieldSSH,
        device: String,
        username: String,
        host: String,
        port: String,
        password: String
    ) {
        when (tag) {
            TagTextFieldSSH.DEVICE -> {
                _textDeviceName.value = device
                _isError.value[0] = false
            }

            TagTextFieldSSH.USERNAME -> {
                _textUsername.value = username
                _isError.value[1] = false
            }

            TagTextFieldSSH.HOST -> {
                _textHost.value = host
                _isError.value[2] = false
            }

            TagTextFieldSSH.PORT -> {
                _textPort.value = port
                _isError.value[3] = false
            }

            TagTextFieldSSH.PASSWORD -> {
                _textPassword.value = password
            }
        }
    }

    fun editSSHCard(model: SSHModel) {
        _editingDevice.value = model
        _textDeviceName.value = model.deviceName
        _textUsername.value = model.username
        _textHost.value = model.host
        _textPort.value = model.port
        _selectedKey.value = null
        _isActiveDialog.value = true
    }

    fun sshAdd(
        username: String,
        deviceName: String,
        host: String,
        port: String,
        privateKey: String?,
        password: String?
    ) {
        val textStateList =
            listOf(_textDeviceName.value, _textUsername.value, _textHost.value, _textPort.value)
        for ((i, j) in _isError.value.indices.zip(textStateList)) {
            if (j.isEmpty()) {
                _isError.value[i] = true
            } else {
                _isError.value[i] = false
            }
        }

        if (_isError.value.all { !it }) {
            viewModelScope.launch {
                if (_listDevices.value.isEmpty()) {
                    if (_editingDevice.value == null) {
                        dao.addSSH(ssh = SSHModel(
                            id = 0,
                            username = username,
                            deviceName = deviceName,
                            host = host,
                            port = port,
                            privateKey = privateKey,
                            password = password
                        )
                        )
                    }
                } else {
                    Log.d("ID", "${_currentId.value}")
                    dao.updateSSH(
                        ssh = SSHModel(
                            id = _editingDevice.value!!.id,
                            username = _textUsername.value,
                            deviceName = _textDeviceName.value,
                            host = _textHost.value,
                            port = _textPort.value,
                            privateKey = _selectedKey.value?.content,
                            password = password
                        )
                    )
                    _listDevices.value = emptyList()
                    _isSelect.value = false
                }
            }
            _isActiveDialog.value = false
        }
    }

    fun passwordAdd(id: Int) {
        viewModelScope.launch {
            if (_textPassword.value.isNotEmpty()) {
                dao.updateSSHPassword(id, _textPassword.value)
            } else {
                dao.updateSSHPassword(id, "empty")
            }
        }
        _isActivePasswordDialog.value = false
    }

    fun toggleMenu() {
        _expanded.value = !_expanded.value
    }

    fun selectKeyInMenu(
        key: KeyModel = KeyModel("Null_title", "Null_content"),
        notKeys: Boolean = false
    ) {
        if (!notKeys) {
            _selectedKey.value = key
        } else {
            _selectedKey.value = null
        }
        _expanded.value = false
    }

    fun isSelectDeviceCard(isBack: Boolean = false) {
        if (isBack) {
            _listDevices.value = emptyList()
        }
        if (_listDevices.value.isNotEmpty()) {
            _isSelect.value = true
        } else {
            _isSelect.value = false
        }
    }

    fun addDeviceToList(device: SSHModel) {
        if (!_listDevices.value.contains(device)) {
            _listDevices.value += device
        } else {
            _listDevices.value -= device
        }
        isSelectDeviceCard()
        Log.d("LIST", "${_listDevices.value}, ${_listDevices.value.size}")
    }

    fun deleteDevice(model: List<SSHModel>) {
        viewModelScope.launch {
            dao.deleteSSH(model)
            _listDevices.value = emptyList()
            _isSelect.value = false
        }
    }
}