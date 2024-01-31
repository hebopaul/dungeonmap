package com.example.dungeonmap.storage

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class FileHandler(
    private val context: Context
) {

    init {
        //create the tokens and maps folders if they don't exist
        if (!File(context.filesDir, "tokens").exists())
            File(context.filesDir, "tokens").mkdirs()

        if (!File(context.filesDir, "maps").exists())
            File(context.filesDir, "maps").mkdirs()
    }

    fun getInternalMapList(): List<File> {
        val files = File(context.filesDir, "maps").listFiles()
        files?.forEach {
            Log.d("File Read:", it.absolutePath.toString())
        }
        return files?.toList()?: listOf()
    }

    fun getInternalTokenList(): List<File> {
        val files = File(context.filesDir, "tokens").listFiles()
        files?.forEach {
            Log.d("File Read:", it.absolutePath.toString())
        }
        return files?.toList()?: listOf()
    }


    private fun saveTokenToInternal(bmp: Bitmap, fileName: String) :Boolean {
        return try {
            context.openFileOutput("tokens/$fileName.png", Context.MODE_PRIVATE).use { stream ->
                if(!bmp.compress(Bitmap.CompressFormat.PNG, 100, stream)) {
                    throw IOException("Could not save image")
                }
                true
            }
        } catch (e: IOException) {
            e.printStackTrace()

            false
        }
    }


    private fun saveMapToInternal(bmp: Bitmap, fileName: String) {
        val file = File(context.filesDir, "maps/$fileName.jpg")
        FileOutputStream(file).use {
            if (!bmp.compress(Bitmap.CompressFormat.JPEG, 100, it)) {
                throw IOException("Could not save image")
            }

        }
    }

    private fun deleteImageFromInternalStorage(fileUri: String): Boolean {
        return try {
            with(context) {
                deleteFile(fileUri)
            }
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    fun importTokenFromDevice(imageUri: Uri?) {
        if (imageUri != null) {
            val bitmap: Bitmap =
                MediaStore.Images.Media.getBitmap(context.contentResolver, imageUri)
            val fileName: String = imageUri.lastPathSegment.toString()
            saveTokenToInternal(bitmap, fileName)

        }
    }

    fun importMapFromDevice(imageUri: Uri?) {
        if (imageUri != null) {
            val bitmap: Bitmap =
                MediaStore.Images.Media.getBitmap(context.contentResolver, imageUri)
            val fileName: String = imageUri.lastPathSegment.toString()
            saveMapToInternal(bitmap, fileName)

        }
    }

}





