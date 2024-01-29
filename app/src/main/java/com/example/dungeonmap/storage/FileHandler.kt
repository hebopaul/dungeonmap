package com.example.dungeonmap.storage

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import java.io.File
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


    fun saveTokenToInternal(bmp: Bitmap, fileName: String) :Boolean {
        return try {
            context.openFileOutput("tokens/$fileName.jpg", Context.MODE_PRIVATE).use { stream ->
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

}





