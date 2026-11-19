package com.woofon

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Devices
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.SettingsRemote
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.woofon.data.viewmodels.HomeViewModel
import com.woofon.data.viewmodels.MainViewModel
import com.woofon.data.viewmodels.ManageKeysViewModel
import com.woofon.data.viewmodels.Pages
import com.woofon.data.viewmodels.SSHViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavPage(
    mainViewModel: MainViewModel = viewModel(),
    viewModelHome: HomeViewModel = viewModel(),
    viewModelSSH: SSHViewModel = viewModel(),
    viewModelManageKeys: ManageKeysViewModel = viewModel(),
    toContent: () -> Unit
) {
    val id by mainViewModel.idPage.collectAsState()
    val pages = listOf<@Composable () -> Unit>(
        { HomePage(viewModel = viewModelHome) },
        { SSHPage(viewModelSSH, viewModelManageKeys) }
    )
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val isSelectHome by viewModelHome.isSelect.collectAsState()
    val isSelectSSH by viewModelSSH.isSelect.collectAsState()

    NavigationMenu(
        drawerState = drawerState,
        mainViewModel = mainViewModel
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        AnimatedVisibility(
                            visible = !isSelectHome && !isSelectSSH,
                            enter = expandHorizontally(
                                animationSpec = tween(durationMillis = 300),
                                expandFrom = Alignment.Start
                            ),
                            exit = shrinkHorizontally(
                                animationSpec = tween(durationMillis = 300),
                                shrinkTowards = Alignment.Start
                            )
                        ) {
                            Text("WoofOn", fontWeight = FontWeight.Bold)
                        }
                    },
                    navigationIcon = {
                        AnimatedVisibility(
                            visible = !isSelectHome && !isSelectSSH,
                            enter = expandHorizontally(
                                animationSpec = tween(durationMillis = 300),
                                expandFrom = Alignment.Start
                            ),
                            exit = shrinkHorizontally(
                                animationSpec = tween(durationMillis = 300),
                                shrinkTowards = Alignment.Start
                            )
                        ) {
                            IconButton(
                                {
                                    scope.launch {
                                        if (drawerState.isClosed) {
                                            drawerState.open()
                                        } else {
                                            drawerState.close()
                                        }
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Menu,
                                    contentDescription = null
                                )
                            }
                        }
                        AnimatedVisibility(
                            visible = isSelectHome || isSelectSSH,
                            enter = expandHorizontally(
                                animationSpec = tween(durationMillis = 300),
                                expandFrom = Alignment.Start
                            ),
                            exit = shrinkHorizontally(
                                animationSpec = tween(durationMillis = 300),
                                shrinkTowards = Alignment.Start
                            )
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                IconButton({
                                    viewModelHome.isSelectDeviceCard(isBack = true)
                                    viewModelSSH.isSelectDeviceCard(isBack = true)
                                }) {
                                    Icon(
                                        imageVector = Icons.Default.ArrowBack,
                                        contentDescription = null
                                    )
                                }
                                Spacer(Modifier.padding(horizontal = 8.dp))
                                Text(
                                    text = if (isSelectHome) "${viewModelHome.listDevices.collectAsState().value.size}" else if (isSelectSSH) "${viewModelSSH.listDevices.collectAsState().value.size}" else "0",
                                    fontSize = 20.sp
                                )
                            }
                        }
                    },
                    actions = {
                        AnimatedVisibility(
                            visible = isSelectHome || isSelectSSH,
                            enter = expandHorizontally(
                                animationSpec = tween(durationMillis = 300),
                                expandFrom = Alignment.Start
                            ),
                            exit = shrinkHorizontally(
                                animationSpec = tween(durationMillis = 300),
                                shrinkTowards = Alignment.Start
                            )
                        ) {
                            Row {
                                IconButton(
                                    onClick = {
                                        viewModelHome.listDevices.value.singleOrNull()
                                            ?.let(viewModelHome::editDeviceCard)
                                        viewModelSSH.listDevices.value.singleOrNull()
                                            ?.let (viewModelSSH::editSSHCard)
                                    },
                                    enabled = viewModelHome.listDevices.collectAsState().value.size == 1 || viewModelSSH.listDevices.collectAsState().value.size == 1
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Edit,
                                        contentDescription = "Edit"
                                    )
                                }
                                IconButton(
                                    {
                                        viewModelHome.deleteDeviceCard(viewModelHome.listDevices.value.map { it.id })
                                        viewModelSSH.deleteDevice(viewModelSSH.listDevices.value)
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = null
                                    )
                                }
                            }
                        }
                        if (mainViewModel.idPage.collectAsState().value == 1) {
                            IconButton({
                                toContent()
                            }) {
                                Icon(
                                    imageVector = Icons.Default.Key,
                                    contentDescription = null
                                )
                            }
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                    ),
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    {
                        if (id == 0) {
                            viewModelHome.toggleDialog()
                        } else if (id == 1) {
                            viewModelSSH.toggleDialog("add", viewModelSSH.currentId.value)
                        }
                    },
                    containerColor = MaterialTheme.colorScheme.surface,
                    elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 2.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add"
                    )
                }
            }
        ) { innerPadding ->
            Column(modifier = Modifier.padding(innerPadding)) {
                pages[id]()
            }
        }
    }
}

@Composable
fun NavigationMenu(
    drawerState: DrawerState,
    mainViewModel: MainViewModel,
    content: @Composable () -> Unit
) {
    val selected by mainViewModel.selected.collectAsState()
    val scope = rememberCoroutineScope()
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier.width(240.dp),
                drawerContainerColor = MaterialTheme.colorScheme.background,
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    NavigationDrawerItem(
                        modifier = Modifier.width(220.dp),
                        label = { Text("WOL") },
                        icon = { Icon(Icons.Default.Devices, contentDescription = null) },
                        selected = selected == Pages.WOL,
                        onClick = {
                            mainViewModel.navigationPage(0)
                            scope.launch {
                                drawerState.close()
                            }
                        },
                        colors = NavigationDrawerItemDefaults.colors(
                            selectedContainerColor = MaterialTheme.colorScheme.surface
                        )
                    )
                    NavigationDrawerItem(
                        modifier = Modifier.width(220.dp),
                        label = { Text("SSH") },
                        icon = { Icon(Icons.Default.SettingsRemote, contentDescription = null) },
                        selected = selected == Pages.SSH,
                        onClick = {
                            mainViewModel.navigationPage(1)
                            scope.launch {
                                drawerState.close()
                            }
                        },
                        colors = NavigationDrawerItemDefaults.colors(
                            selectedContainerColor = MaterialTheme.colorScheme.surface
                        )
                    )
                }
            }
        }
    ) {
        content()
    }
}