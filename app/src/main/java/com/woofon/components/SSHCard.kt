package com.woofon.components

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.woofon.data.viewmodels.SSHViewModel
import com.woofon.data.viewmodels.models.SSHModel

@Composable
fun SSHCard(
    model: SSHModel,
    viewModel: SSHViewModel
) {
    val scope = rememberCoroutineScope()
    OutlinedCard(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .padding(horizontal = 8.dp, vertical = 6.dp)
            .combinedClickable(
                onClick = {
//                    scope.launch(Dispatchers.IO) {
//                        shutdownComputer(model)
//                    }
                    if (viewModel.listDevices.value.isNotEmpty()) {
                        viewModel.addDeviceToList(model)
                    } else {
                        if (model.password == null) {
                            viewModel.toggleDialog(dialog = "password", id = model.id)
                        } else {
                            Log.d("SSHCARD", "$model")
                        }
                    }
                },
                onLongClick = {
                    viewModel.addDeviceToList(model)
                }
            ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = if (viewModel.listDevices.collectAsState().value.contains(model)) BorderStroke(
            2.dp,
            MaterialTheme.colorScheme.onPrimary
        ) else BorderStroke(2.dp, Color.Transparent),
        shape = RoundedCornerShape(6.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(model.deviceName)
            Text("${model.username}@${model.host}")
        }
    }
}