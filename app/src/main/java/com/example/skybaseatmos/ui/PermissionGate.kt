package com.example.skybaseatmos.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PermissionGate (permissions: List<String> = emptyList(), onPermissionAllowed:@Composable () -> Unit={}){
    if(permissions.isEmpty()){
        onPermissionAllowed()
        return
    }
    val permissionState = rememberMultiplePermissionsState(permissions = permissions)
    if(permissionState.allPermissionsGranted){
        onPermissionAllowed()
    }else{
        if (permissionState.shouldShowRationale){
            val isDialogueVisible= remember{mutableStateOf(true)}
            if(isDialogueVisible.value){
                PermissionDialog(
                    title = "Permission required",
                    message = "Permission required to show map ",
                    onDismiss = {},
                    onConfirm = {
                        permissionState.launchMultiplePermissionRequest()
                    }
                )
            }

        }else{
            SideEffect {
                permissionState.launchMultiplePermissionRequest()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PermissionDialog(
    title: String="Title",
    message: String="Message",
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
){
    BasicAlertDialog(
        onDismissRequest = onDismiss,
    ) {
        ElevatedCard {
            Column(
                modifier = Modifier.padding(16.dp),
            ){
                Text(text = title,style= MaterialTheme.typography.titleMedium)
                Text(text = message)
                Row(modifier = Modifier.fillMaxWidth().padding(top = 16.dp), horizontalArrangement = Arrangement.End) {
                    OutlinedButton(onClick = onDismiss) {
                        Text(text = "Cancel")
                    }
                    Button(onClick = onConfirm, modifier = Modifier.padding(start = 8.dp)   ) { }
                }

            }
        }
    }
}