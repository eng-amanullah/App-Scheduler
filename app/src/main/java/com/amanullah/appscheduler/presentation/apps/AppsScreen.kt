package com.amanullah.appscheduler.presentation.apps

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.amanullah.appscheduler.base.customview.SearchBar
import com.amanullah.appscheduler.base.customview.ShowTimePickerDialog
import com.amanullah.appscheduler.base.utils.extensions.keyboardState
import com.amanullah.appscheduler.base.utils.logger.Logger
import com.amanullah.appscheduler.data.local.entity.Alarm
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppsScreen(viewModel: AppsViewModel = hiltViewModel()) {
    var defaultText by remember { mutableStateOf(value = "") }

    var listOfApps by remember { mutableStateOf<List<String>?>(value = null) }
    var apps by remember { mutableStateOf<List<String>?>(value = null) }
    var selectedApp by remember { mutableStateOf<String?>(value = null) }
    var scheduleTime by remember { mutableStateOf<Long?>(value = null) }

    var showTimeDialog by remember { mutableStateOf(value = false) }

    val isKeyboardOpen by keyboardState()

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = showTimeDialog) {
        Logger.d("ShowTimeDialog", showTimeDialog.toString())
    }

    LaunchedEffect(key1 = Unit) {
        listOfApps = viewModel.getApps()
        apps = listOfApps
    }

    LaunchedEffect(key1 = defaultText) {
        apps = if (defaultText.isEmpty()) {
            listOfApps
        } else {
            listOfApps?.filter { app ->
                app.contains(other = defaultText, ignoreCase = true)
            }
        }
    }

    Scaffold(
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                CenterAlignedTopAppBar(
                    title = {
                        Text(text = "Home")
                    }
                )

                SearchBar(
                    modifier = Modifier
                        .padding(
                            start = 16.dp,
                            end = 16.dp,
                            bottom = 16.dp
                        ),
                    hint = "Search App",
                    defaultValue = defaultText,
                    inputCallback = { text ->
                        defaultText = text
                    }
                )
            }
        }
    ) { paddingValue ->
        Column(
            modifier = Modifier
                .padding(paddingValues = paddingValue)
                .fillMaxSize()
        ) {
            if (apps.isNullOrEmpty()) {
                if (defaultText.isNotEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(all = 16.dp),
                        contentAlignment = if (isKeyboardOpen) Alignment.TopCenter else Alignment.Center
                    ) {
                        Text(text = "No app found")
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(all = 16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    apps?.let {
                        items(it.size) { index ->
                            Text(
                                text = it[index],
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        selectedApp = it[index]
                                        showTimeDialog = true
                                    }
                                    .padding(all = 16.dp)
                            )
                        }
                    }
                }
            }

            ShowTimePickerDialog(
                showDialog = showTimeDialog,
                onDismiss = { showTimeDialog = false },
                onTimeSelected = { diffMillis, hour, minute ->
                    scheduleTime = diffMillis
                    showTimeDialog = false
                    defaultText = ""

                    scope.launch {
                        selectedApp?.let {
                            scheduleTime?.let { time ->
                                if (time >= 0) {
                                    viewModel.getAlarmByTime(time = "$hour:$minute")
                                    val oldAlarm = viewModel.alarmByTime.value

                                    if (oldAlarm != null) {
                                        val newAlarm = Alarm(
                                            requestCode = oldAlarm.requestCode,
                                            appName = oldAlarm.appName,
                                            time = "$hour:$minute"
                                        )
                                        viewModel.insertOrUpdateAlarm(alarm = newAlarm)

                                        viewModel.editSchedule(
                                            alarm = oldAlarm,
                                            time = diffMillis
                                        )

                                        Toast.makeText(
                                            context,
                                            "Schedule Edited",
                                            Toast.LENGTH_SHORT
                                        )
                                            .show()
                                    } else {
                                        // Generate a unique request code for the alarm
                                        val requestCode = viewModel.getUniqueRequestCode()

                                        val newAlarm = Alarm(
                                            requestCode = requestCode,
                                            appName = it,
                                            time = "$hour:$minute"
                                        )
                                        viewModel.insertOrUpdateAlarm(newAlarm)

                                        // Schedule the alarm for the app with a unique request code
                                        viewModel.scheduleApp(alarm = newAlarm, time = time)

                                        Toast.makeText(context, "Scheduled", Toast.LENGTH_SHORT)
                                            .show()
                                    }
                                }
                            }
                        }
                    }
                }
            )
        }
    }
}

@Preview
@Composable
private fun Preview() {
    AppsScreen()
}