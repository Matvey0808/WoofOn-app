package com.woofon

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import com.woofon.data.viewmodels.ManageKeysViewModel

object NavPage
object KeyPage

@Composable
fun AppNav(viewModelManageKeys: ManageKeysViewModel = viewModel()) {
    val backStack = remember { mutableStateListOf<Any>(NavPage) }

    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryProvider = { key ->
            when (key) {
                is NavPage -> NavEntry(key) {
                    NavPage(toContent = {
                        if (!backStack.contains(KeyPage)) {
                            backStack.add(KeyPage)
                        }
                    })
                }

                is KeyPage -> NavEntry(key) {
                    ManageKeysPage(
                        toContent = {
                            backStack.remove(KeyPage)
                        },
                        viewModel = viewModelManageKeys
                    )
                }

                else -> NavEntry(Unit) { Text("Unknown route") }
            }
        }
    )
}