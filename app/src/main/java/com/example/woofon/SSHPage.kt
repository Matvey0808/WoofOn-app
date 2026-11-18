package com.example.woofon

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.woofon.components.PasswordDialog
import com.example.woofon.components.SSHCard
import com.example.woofon.components.SSHDialog
import com.example.woofon.data.viewmodels.ManageKeysViewModel
import com.example.woofon.data.viewmodels.SSHViewModel
import com.example.woofon.data.viewmodels.TagTextFieldSSH

@Composable
fun SSHPage(viewModel: SSHViewModel, viewModelManageKeys: ManageKeysViewModel) {
    val sshList by viewModel.ssh.collectAsState()
    LazyColumn {
        items(sshList) { element ->
            SSHCard(element, viewModel)
        }
    }
    when {
        viewModel.isActiveDialog.collectAsState().value -> SSHDialog(viewModel, viewModelManageKeys)
        viewModel.isActivePasswordDialog.collectAsState().value -> PasswordDialog(
            viewModel,
            TagTextFieldSSH.PASSWORD
        )
    }
}