package com.example.dungeonmap.storage

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import com.example.dungeonmap.data.InternalStorageImage
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class FileHandler(
    private val context: Context
) {
    companion object{
        lateinit var internalStorageTokenList: MutableList<InternalStorageImage>
        lateinit var internalStorageMapList: MutableList<InternalStorageImage>
    }

    init {
        //create the tokens and maps folders if they don't exist
        if (!File(context.filesDir, "tokens").exists())
            File(context.filesDir, "tokens").mkdirs()

        if (!File(context.filesDir, "maps").exists())
            File(context.filesDir, "maps").mkdirs()

        updateInternalMapList()
        updateInternalTokenList()
    }

    fun getFileList(): List<File> {
        val files = File(context.filesDir, "").listFiles()
        files?.forEach {
            Log.d("File Read:", it.absolutePath.toString())
        }
        return files?.toList()?: listOf()
    }
    fun updateInternalMapList()  {
        val files = File(context.filesDir, "maps").listFiles()
        val out: MutableList<InternalStorageImage> = mutableListOf()
        files?.forEach {
            Log.d("File Read:", it.absolutePath.toString())
            val bmp = BitmapFactory.decodeFile(it.absolutePath.toString())
            out.add(
                InternalStorageImage(
                    name = it.name.toString(),
                    uri = it.absolutePath.toString(),
                    painter = BitmapPainter(bmp.asImageBitmap())
                )
            )
        }
        internalStorageMapList = out
    }

    fun updateInternalTokenList()  {
        val files = File(context.filesDir, "tokens").listFiles()
        val out: MutableList<InternalStorageImage> = mutableListOf()
        files?.forEach {
            Log.d("File Read:", it.absolutePath.toString())
            val bmp = BitmapFactory.decodeFile(it.absolutePath.toString())
            out.add(
                InternalStorageImage(
                    name = it.name.toString(),
                    uri = it.absolutePath.toString(),
                    painter = BitmapPainter(bmp.asImageBitmap())
                )
            )
        }
        internalStorageTokenList = out
    }


    private fun saveTokenToInternal(bmp: Bitmap, fileName: String) {
        val file = File(context.filesDir.absolutePath, "token/$fileName.png")
        //file.createNewFile()
        FileOutputStream(file).use {
            if (!bmp.compress(Bitmap.CompressFormat.PNG, 100, it)) {
                throw IOException("Could not save image")
            }

        }
    }

    private fun saveMapToInternal(bmp: Bitmap, fileName: String) {
        val file = File(context.filesDir.absolutePath, "maps/$fileName.jpg")
        //file.createNewFile()
        FileOutputStream(file).use {
            if (!bmp.compress(Bitmap.CompressFormat.JPEG, 100, it)) {
                throw IOException("Could not save image")
            }

        }
    }

    fun deleteImageFromInternalStorage(fileUri: String) {
        try {
            val file = File(fileUri)
            if ( file.canRead() && file.canWrite() && file.exists()) {
                file.delete()
                Log.d("Delete", fileUri)
                internalStorageMapList.forEachIndexed { i, image -> if (image.uri == fileUri) internalStorageMapList.removeAt(i)}
                internalStorageMapList.forEachIndexed { i, image -> if (image.uri == fileUri) internalStorageMapList.removeAt(i)}

            } else {
                throw IOException()
            }
        } catch (e: Exception) {
            e.printStackTrace()

        }
    }

    fun importTokenFromDevice(imageUri: Uri?) {
        if (imageUri != null) {
            val bitmap: Bitmap =
                MediaStore.Images.Media.getBitmap(context.contentResolver, imageUri)
            val fileName: String = imageUri.lastPathSegment.toString()
            saveTokenToInternal(bitmap, fileName)
            updateInternalTokenList()

        }
    }

    fun importMapFromDevice(imageUri: Uri?) {
        if (imageUri != null) {
            val bitmap: Bitmap =
                MediaStore.Images.Media.getBitmap(context.contentResolver, imageUri)
            val fileName: String = imageUri.lastPathSegment.toString()
            saveMapToInternal(bitmap, fileName)
            updateInternalMapList()

        }
    }

}





