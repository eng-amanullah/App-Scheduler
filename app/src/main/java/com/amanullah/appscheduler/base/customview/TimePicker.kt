package com.amanullah.appscheduler.base.customview

import android.app.TimePickerDialog
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import java.util.Calendar

@Composable
fun ShowTimePickerDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onTimeSelected: (Long, Int, Int) -> Unit
) {
    if (showDialog) {
        val context = LocalContext.current
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        LaunchedEffect(key1 = Unit) {
            val timePickerDialog = TimePickerDialog(
                context,
                { _, selectedHour, selectedMinute ->
                    val selectedCalendar = Calendar.getInstance().apply {
                        set(Calendar.HOUR_OF_DAY, selectedHour)
                        set(Calendar.MINUTE, selectedMinute)
                        set(Calendar.SECOND, 0)
                        set(Calendar.MILLISECOND, 0)
                    }
                    onTimeSelected(selectedCalendar.timeInMillis, selectedHour, selectedMinute)
                    onDismiss()
                },
                hour, minute, true
            )
            timePickerDialog.setCanceledOnTouchOutside(false)
            timePickerDialog.setOnCancelListener {
                onDismiss()
            }
            timePickerDialog.show()
        }
    }
}