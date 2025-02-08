package com.amanullah.appscheduler.base.customview

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun MyAlertDialog(
    showDialog: Boolean = true,
    showDialogCallBack: () -> Unit,
    dialogCallBack: (Boolean) -> Unit
) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialogCallBack() },
            title = { Text(text = "Delete Scheduled App") },
            text = { Text(text = "Are you sure, you want to delete this schedule time?") },
            confirmButton = {
                Button(
                    onClick = {
                        showDialogCallBack()
                        dialogCallBack(true)
                    }
                ) {
                    Text(text = "Yes")
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        showDialogCallBack()
                        dialogCallBack(false)
                    }
                ) {
                    Text(text = "No")
                }
            }
        )
    }
}