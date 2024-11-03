package com.example.ecommercefront_end.utils

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import io.ktor.websocket.Frame


@OptIn(ExperimentalPermissionsApi::class)
@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@Composable
fun RequestStoragePermission(onPermissionGranted: @Composable () -> Unit) {
    val permissionState = rememberPermissionState(permission = Manifest.permission.READ_MEDIA_IMAGES)

    LaunchedEffect(Unit) {
        // Log to see if the permission is granted on launch
        Log.d("REQUEST_PERMISSION", "Checking permission: ${permissionState.status.isGranted}")
    }

    when {
        permissionState.status.isGranted -> {
            Log.d("REQUEST_PERMISSION", "isGranted")
            onPermissionGranted()
        }
        permissionState.status.shouldShowRationale -> {
            Column {
                Text("We need access to your storage to upload images.")
                Button(onClick = {
                    Log.d("RequestStoragePermission", "Requesting permission")
                    permissionState.launchPermissionRequest()
                }) {
                    Text("Grant Permission")
                }
            }
        }
        else -> {
            Button(onClick = {
                Log.d("RequestStoragePermission", "Requesting permission")
                permissionState.launchPermissionRequest()
            }) {
                Text("Request Storage Permission")
            }
        }
    }
}