package com.example.dungeonmap.storage

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import com.example.dungeonmap.data.InternalStorageImage
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

    fun getInternalMapList(): MutableList<InternalStorageImage> {
        val files = File(context.filesDir, "maps").listFiles()
        var out: MutableList<InternalStorageImage> = mutableListOf()
        files?.forEach {
            Log.d("File Read:", it.absolutePath.toString())
            out.add(
                InternalStorageImage(
                    name = it.name.toString(),
                    uri = it.absolutePath.toString(),
                    bmp = BitmapFactory.decodeFile(it.absolutePath.toString())
                )
            )
        }
        return out
    }

    fun getInternalTokenList(): MutableList<InternalStorageImage> {
        val files = File(context.filesDir, "tokens").listFiles()
        var out: MutableList<InternalStorageImage> = mutableListOf()
        files?.forEach {
            Log.d("File Read:", it.absolutePath.toString())
            out.add(
                InternalStorageImage(
                    name = it.name.toString(),
                    uri = it.absolutePath.toString(),
                    bmp = BitmapFactory.decodeFile(it.absolutePath.toString())
                )
            )
        }
        return out
    }


    private fun saveTokenToInternal(bmp: Bitmap, fileName: String) {
        val file = File(context.filesDir, "tokens/$fileName.jpg")
        FileOutputStream(file).use {
            if (!bmp.compress(Bitmap.CompressFormat.JPEG, 100, it)) {
                throw IOException("Could not save image")
            }

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





