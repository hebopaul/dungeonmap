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
    context: Context
) {
/*
    companion object{
        lateinit var internalStorageTokenList: MutableList<InternalStorageImage>
        lateinit var internalStorageMapList: MutableList<InternalStorageImage>
    }
*/

    private val ctx = context.applicationContext

    init {
        //create the tokens and maps folders if they don't exist
        listOf("tokens", "map")
            .map { File(ctx.filesDir, it) }
            .forEach { if (!it.exists()) { it.mkdirs() } }
    }

    fun getFileList(): List<File> {
        val files = File(ctx.filesDir, "").listFiles()
        files?.forEach {
            Log.d("File Read:", it.absolutePath.toString())
        }
        return files?.toList()?: listOf()
    }

    fun getInternalStorageMapList(): List<InternalStorageImage>  {
        val files = File(ctx.filesDir, "maps").listFiles()
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
        return out
    }

    fun getInternalStorageTokenList(): List<InternalStorageImage>  {
        val files = File(ctx.filesDir, "tokens").listFiles()
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
        return out
    }


    private fun saveTokenToInternal(bmp: Bitmap, fileName: String) {
        val file = File(ctx.filesDir.absolutePath, "tokens/$fileName.png")
        //file.createNewFile()
        FileOutputStream(file).use {
            if (!bmp.compress(Bitmap.CompressFormat.PNG, 100, it)) {
                throw IOException("Could not save image")
            }

        }
    }

    private fun saveMapToInternal(bmp: Bitmap, fileName: String) {
        val file = File(ctx.filesDir.absolutePath, "maps/$fileName.jpg")
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
                MediaStore.Images.Media.getBitmap(ctx.contentResolver, imageUri)
            val fileName: String = imageUri.lastPathSegment.toString()
            saveTokenToInternal(bitmap, fileName)
        }
    }

    fun importMapFromDevice(imageUri: Uri?) {
        if (imageUri != null) {
            val bitmap: Bitmap =
                MediaStore.Images.Media.getBitmap(ctx.contentResolver, imageUri)
            val fileName: String = imageUri.lastPathSegment.toString()
            saveMapToInternal(bitmap, fileName)
        }
    }

}





