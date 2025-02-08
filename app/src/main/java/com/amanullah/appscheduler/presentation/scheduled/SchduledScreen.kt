package com.amanullah.appscheduler.presentation.scheduled

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.amanullah.appscheduler.base.customview.MyAlertDialog
import com.amanullah.appscheduler.base.customview.ShowTimePickerDialog
import com.amanullah.appscheduler.data.local.entity.Alarm
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduledScreen(viewModel: ScheduledViewModel = hiltViewModel()) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    var alarm by remember { mutableStateOf<Alarm?>(value = null) }

    var alarms by remember { mutableStateOf<List<Alarm>>(value = emptyList()) }

    var showTimeDialog by remember { mutableStateOf(value = false) }

    val scope = rememberCoroutineScope()

    var getData by remember { mutableStateOf(value = true) }

    var deleteDialog by remember { mutableStateOf(value = false) }

    // Fetch the alarms from the database
    LaunchedEffect(key1 = getData) {
        viewModel.fetchScheduledAlarms()
        alarms = viewModel.scheduledAlarm.value
        getData = false
    }

    // Observe lifecycle changes
    DisposableEffect(key1 = lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                // Trigger the data refresh when the app resumes
                getData = true
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    // UI Layout
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = "Scheduled")
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues = innerPadding)
        ) {
            if (alarms.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "No alarms scheduled.")
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 16.dp)
                ) {
                    items(items = alarms) { item ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(
                                    modifier = Modifier
                                        .weight(weight = 1F)
                                        .padding(all = 16.dp)
                                ) {
                                    Text(
                                        text = "App: ${item.appName}",
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = "Scheduled Time: ${item.time}",
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }

                                IconButton(
                                    onClick = {
                                        alarm = item
                                        showTimeDialog = true
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Edit,
                                        contentDescription = null
                                    )
                                }

                                IconButton(
                                    onClick = {
                                        alarm = item
                                        deleteDialog = true
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = null
                                    )
                                }
                            }
                        }
                    }
                }
            }

            ShowTimePickerDialog(
                showDialog = showTimeDialog,
                onDismiss = { showTimeDialog = false },
                onTimeSelected = { diffMillis, hour, minute ->
                    scope.launch {
                        alarm?.let { item ->
                            if (diffMillis >= 0) {
                                val newAlarm = Alarm(
                                    requestCode = item.requestCode,
                                    appName = item.appName,
                                    time = "$hour:$minute"
                                )

                                viewModel.insertOrUpdateAlarm(alarm = newAlarm)

                                viewModel.editSchedule(alarm = item, time = diffMillis)

                                Toast.makeText(context, "Schedule Edited", Toast.LENGTH_SHORT)
                                    .show()

                                getData = true
                                showTimeDialog = false
                            }
                        }
                    }
                }
            )

            MyAlertDialog(
                showDialog = deleteDialog,
                showDialogCallBack = {
                    deleteDialog = false
                },
                dialogCallBack = {
                    if (it) {
                        getData = true
                        alarm?.let { item ->
                            // Cancel the scheduled alarm with the corresponding package name and request code
                            viewModel.cancelSchedule(requestCode = item.requestCode)
                            viewModel.deleteAlarm(alarm = item)
                        }
                        Toast.makeText(context, "Schedule Deleted", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            )
        }
    }
}


@Preview
@Composable
private fun Preview() {
    ScheduledScreen()
}