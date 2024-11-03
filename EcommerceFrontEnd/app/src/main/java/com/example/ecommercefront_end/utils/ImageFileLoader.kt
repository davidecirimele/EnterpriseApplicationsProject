package com.example.ecommercefront_end.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.Log
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import java.io.File
import java.io.FileOutputStream
import java.util.UUID

object ImageFileLoader {
    fun loadImage(context: Context, uri: Uri, onImageLoaded: (File?) -> Unit) {
        Glide.with(context)
            .asBitmap()
            .load(uri)
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    val file = saveBitmapToFile(context, resource)
                    onImageLoaded(file)
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                    // Placeholder
                }
            })
    }

    private fun saveBitmapToFile(context: Context, bitmap: Bitmap): File {
        val uniqueFileName = "image_${UUID.randomUUID()}.png"
        val file = File(context.cacheDir, uniqueFileName)
        FileOutputStream(file).use { out ->
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
        }
        return file
    }
}