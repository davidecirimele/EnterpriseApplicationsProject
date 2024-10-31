package com.example.ecommercefront_end.utils

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import java.io.File

@Composable
fun FileUploadButton(onFileSelected: (File?) -> Unit) {
    val context = LocalContext.current
    var fileUri by remember { mutableStateOf<Uri?>(null) }

    val filePickerLauncher =  rememberLauncherForActivityResult(
        contract = PickVisualMedia()
    ) { uri: Uri? ->
        fileUri = uri
        Log.d("FileUploadButton", "URI $fileUri")

        if (uri != null) {
            ImageFileLoader.loadImage(context, uri) { file ->
                file?.let {
                    onFileSelected(file)
                }
            }
        }
    }

    Button(onClick = {
        filePickerLauncher.launch(
            PickVisualMediaRequest(PickVisualMedia.ImageOnly)
        )
    }, modifier = Modifier.fillMaxWidth()) {
        Text("Upload Cover Image")
    }
}