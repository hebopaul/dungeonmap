package com.example.dungeonmap.storage

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.LifecycleCoroutineScope
import com.example.dungeonmap.MainActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException

class ImageHandler(
    private val context: Context
) {

    private suspend fun loadImagesFromInternalStorage(): List<InternalStorageImage> {

        return withContext(Dispatchers.IO) {
            val files = context.filesDir.listFiles()
             files?.filter {
                 it.canRead() &&
                 it.isFile &&
                 (it.name.endsWith(".jpg") or it.name.endsWith(".png"))}?.map {
                val bytes = it.readBytes()
                val bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                 InternalStorageImage(it.name, bmp)
            } ?: listOf()
        }
    }

    fun saveImageToInternalStorage(fileName: String, bitmap: Bitmap):  Boolean {
        return try {
            with(context) {
                openFileOutput(fileName, MODE_PRIVATE).use { outputStream ->
                    if (!bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream))
                        throw IOException("Couldn't save image")
                }
                true
            }
        } catch (e: IOException) {
            e.printStackTrace()
             false
        }
    }

    private fun deleteImageFromInternalStorage(fileName: String): Boolean {
        return try {
            with(context) {
                deleteFile(fileName)
            }
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

}





