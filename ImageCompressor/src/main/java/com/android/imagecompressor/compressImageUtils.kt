/*Copyright [2022] [Vinod Baste]

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.*/

@file:Suppress("DEPRECATION")

package com.android.imagecompressor

import android.content.Context
import android.graphics.*
import android.media.ExifInterface
import android.os.Build
import android.os.Environment
import android.util.Log
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.nio.file.Files
import kotlin.math.roundToInt

object ImageCompressUtils {

    //compress the image
    @JvmOverloads
    fun compressImage(
        context: Context,
        imagePath: String?,
        imageName: String?,
        imageQuality: Int = 50
    ): String {

        var filePath = ""
        try {
            var scaledBitmap: Bitmap? = null
            val options = BitmapFactory.Options()
            // by setting this field as true, the actual bitmap pixels are not loaded in the memory.
            // Just the bounds are loaded. If you try the use the bitmap here, you will get null.
            options.inJustDecodeBounds = true
            var actualHeight = options.outHeight
            var actualWidth = options.outWidth

            val imageFile = File(imagePath!!)
            val fileContent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Files.readAllBytes(imageFile.toPath())
            } else {
                File(imagePath).readBytes()
            }
            var bmp = BitmapFactory.decodeByteArray(fileContent, 0, fileContent.size, options)

            //max Height and width values of the compressed image is taken as 1024x912
            val maxHeight = 1024.0f
            val maxWidth = 912.0f
            var imgRatio = actualWidth / actualHeight.toFloat()
            val maxRatio = maxWidth / maxHeight

            //width and height values are set maintaining the aspect ratio of the image
            if (actualHeight > maxHeight || actualWidth > maxWidth) {
                when {
                    imgRatio < maxRatio -> {
                        imgRatio = maxHeight / actualHeight
                        actualWidth = (imgRatio * actualWidth).toInt()
                        actualHeight = maxHeight.toInt()
                    }
                    imgRatio > maxRatio -> {
                        imgRatio = maxWidth / actualWidth
                        actualHeight = (imgRatio * actualHeight).toInt()
                        actualWidth = maxWidth.toInt()
                    }
                    else -> {
                        actualHeight = maxHeight.toInt()
                        actualWidth = maxWidth.toInt()
                    }
                }
            }

            //setting inSampleSize value allows to load a scaled down version of the original image
            options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight)
            //inJustDecodeBounds set to false to load the actual bitmap
            options.inJustDecodeBounds = false
            //this removes the redundant quality of the image
            options.inPurgeable = true
            //this options allow android to claim the bitmap memory if it runs low on memory
            options.inInputShareable = true

            options.inTempStorage = ByteArray(16 * 1024)
            try {
                //load the bitmap from its path
                bmp = BitmapFactory.decodeFile(imagePath, options)
            } catch (exception: OutOfMemoryError) {
                exception.printStackTrace()
            }
            try {
                scaledBitmap =
                    Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888)
            } catch (exception: OutOfMemoryError) {
                exception.printStackTrace()
            }
            val ratioX = actualWidth / options.outWidth.toFloat()
            val ratioY = actualHeight / options.outHeight.toFloat()
            val middleX = actualWidth / 2.0f
            val middleY = actualHeight / 2.0f
            val scaleMatrix = Matrix()
            scaleMatrix.setScale(ratioX, ratioY, middleX, middleY)
            val canvas = Canvas(scaledBitmap!!)
            canvas.setMatrix(scaleMatrix)
            canvas.drawBitmap(
                bmp,
                middleX - bmp.width / 2,
                middleY - bmp.height / 2,
                Paint(Paint.FILTER_BITMAP_FLAG)
            )

            //check the rotation of the image and display it properly
            val exif: ExifInterface
            try {
                exif = ExifInterface(imagePath.toString())
                val orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, 0
                )
                Log.d("EXIF", "Exif: $orientation")
                val matrix = Matrix()
                when (orientation) {
                    6 -> {
                        matrix.postRotate(90f)
                        Log.d("EXIF", "Exif: $orientation")
                    }
                    3 -> {
                        matrix.postRotate(180f)
                        Log.d("EXIF", "Exif: $orientation")
                    }
                    8 -> {
                        matrix.postRotate(270f)
                        Log.d("EXIF", "Exif: $orientation")
                    }
                }
                scaledBitmap = Bitmap.createBitmap(
                    scaledBitmap, 0, 0,
                    scaledBitmap.width, scaledBitmap.height, matrix,
                    true
                )
            } catch (e: IOException) {
                e.printStackTrace()
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
            val out: FileOutputStream?
            filePath = imageName?.let { getOutputMediaFile(it, context) }!!.absolutePath
            try {
                out = FileOutputStream(filePath)

                //write the compressed bitmap at the destination specified by filename.
                scaledBitmap!!.compress(Bitmap.CompressFormat.JPEG, imageQuality, out)
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }

        return filePath
    }

}

private fun getOutputMediaFile(imageName: String, context: Context): File? {
    var imageFile1: File? = null
    try {
        imageFile1 = createImageFile(context, imageName)
    } catch (e: IOException) {
        e.printStackTrace()
    }
    if (imageFile1!!.exists()) imageFile1.delete()
    var imageNew: File? = null
    try {
        imageNew = createImageFile(context, imageName)
    } catch (e: IOException) {
        e.printStackTrace()
    }
    return imageNew
}

private fun calculateInSampleSize(
    options: BitmapFactory.Options,
    reqWidth: Int,
    reqHeight: Int
): Int {
    val height = options.outHeight
    val width = options.outWidth
    var inSampleSize = 1
    try {
        if (height > reqHeight || width > reqWidth) {
            val heightRatio =
                (height.toFloat() / reqHeight.toFloat()).roundToInt()
            val widthRatio =
                (width.toFloat() / reqWidth.toFloat()).roundToInt()
            inSampleSize = if (heightRatio < widthRatio) heightRatio else widthRatio
        }
        val totalPixels = width * height.toFloat()
        val totalReqPixelsCap = reqWidth * reqHeight * 2.toFloat()
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++
        }
    } catch (e: java.lang.Exception) {
        e.printStackTrace()
    }
    return inSampleSize
}

@Throws(IOException::class)
private fun createImageFile(context: Context, FileName: String): File {
    return File(
        context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            .toString() + File.separator + FileName + ".png"
    )
}