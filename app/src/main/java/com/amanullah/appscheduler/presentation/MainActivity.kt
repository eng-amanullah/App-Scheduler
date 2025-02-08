package com.amanullah.appscheduler.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.amanullah.appscheduler.base.navgraph.AppNavGraph
import com.amanullah.appscheduler.base.permission.RequestExactAlarmPermission
import com.amanullah.appscheduler.base.theme.AppSchedulerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppSchedulerTheme {
                var showUI by remember { mutableStateOf(value = false) }

                if (showUI) {
                    AppNavGraph()
                } else {
                    RequestExactAlarmPermission(
                        callBack = {
                            showUI = it
                        }
                    )
                }
            }
        }
    }
}