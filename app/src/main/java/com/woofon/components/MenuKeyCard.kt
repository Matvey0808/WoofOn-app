package com.woofon.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.woofon.data.viewmodels.ManageKeysViewModel
import com.woofon.data.viewmodels.SSHViewModel

@Composable
fun SelectedKeyCard(viewModel: SSHViewModel) {
    OutlinedCard(
        modifier = Modifier
            .fillMaxWidth()
            .height(55.dp)
            .padding(horizontal = 26.dp)
            .clickable(
                onClick = { viewModel.toggleMenu() },
                interactionSource = null,
                indication = null
            ),
        colors = CardDefaults.outlinedCardColors(
            containerColor = MaterialTheme.colorScheme.background
        ),
        border = BorderStroke(2.dp, MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(6.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                viewModel.selectedKey.collectAsState().value?.title ?: "Do not use keys",
                modifier = Modifier.padding(horizontal = 12.dp)
            )
            Icon(
                imageVector = if (viewModel.expanded.collectAsState().value) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown,
                contentDescription = null,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
        }
    }
}

@Composable
fun KeysDropdownMenu(viewModel: SSHViewModel, viewModelManageKey: ManageKeysViewModel) {
    val keyList by viewModelManageKey.listKeys.collectAsState()
    Box(
        modifier = Modifier.padding(16.dp),
    ) {
        DropdownMenu(
            expanded = viewModel.expanded.collectAsState().value,
            onDismissRequest = { viewModel.toggleMenu() },
            modifier = Modifier.width(248.dp),
            offset = DpOffset((-123).dp, (-16).dp),
            containerColor = MaterialTheme.colorScheme.background
        ) {
            DropdownMenuItem(
                text = { Text("Do not use keys") },
                onClick = { viewModel.selectKeyInMenu(notKeys = true) }
            )
            keyList.forEach { element ->
                DropdownMenuItem(
                    text = { Text(element.title) },
                    onClick = { viewModel.selectKeyInMenu(element) }
                )
            }
        }
    }
}