package com.example.woofon.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.woofon.data.viewmodels.ManageKeysViewModel
import com.example.woofon.data.viewmodels.SSHViewModel
import com.example.woofon.data.viewmodels.TagTextFieldSSH

data class SSHFieldData(
    val label: String,
    val state: String,
    val tag: TagTextFieldSSH,
    val error: Int
)

@Composable
fun SSHDialog(viewModel: SSHViewModel, viewModelManageKeys: ManageKeysViewModel) {
    val isEditing = viewModel.editingDevice.collectAsState().value != null
    val fields = listOf(
        SSHFieldData(
            label = "name",
            state = viewModel.textDeviceName.collectAsState().value,
            tag = TagTextFieldSSH.DEVICE,
            error = 0
        ),
        SSHFieldData(
            label = "username",
            state = viewModel.textUsername.collectAsState().value,
            tag = TagTextFieldSSH.USERNAME,
            error = 1
        ),
        SSHFieldData(
            label = "host",
            state = viewModel.textHost.collectAsState().value,
            tag = TagTextFieldSSH.HOST,
            error = 2
        ),
        SSHFieldData(
            label = "port",
            state = viewModel.textPort.collectAsState().value,
            tag = TagTextFieldSSH.PORT,
            error = 3
        )
    )
    Dialog(onDismissRequest = { viewModel.toggleDialog("add", viewModel.currentId.value) }) {
        Card(
            modifier = Modifier.width(300.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.background,
            )
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = if (isEditing) "Edit Host" else "Add Host",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    modifier = Modifier.padding(vertical = 14.dp)
                )
                fields.forEach { field ->
                    SSHField(
                        viewModel,
                        field.label,
                        field.state,
                        field.tag,
                        field.error
                    )
                }
                Spacer(Modifier.padding(vertical = 6.dp))
                SelectedKeyCard(viewModel)
                KeysDropdownMenu(viewModel, viewModelManageKeys)
                Button(
                    onClick = {
                        viewModel.sshAdd(
                            username = viewModel.textUsername.value,
                            deviceName = viewModel.textDeviceName.value,
                            host = viewModel.textHost.value,
                            port = viewModel.textPort.value,
                            privateKey = viewModel.selectedKey.value?.content,
                            password = null
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    shape = RoundedCornerShape(6.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                        contentColor = MaterialTheme.colorScheme.onSurface
                    )
                ) {
                    Text(text = if (isEditing) "Save" else "Add")
                }
            }
        }
    }
}