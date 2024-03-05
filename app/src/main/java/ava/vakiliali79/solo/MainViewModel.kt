package ava.vakiliali79.solo

import android.app.Application
import android.graphics.Bitmap
import android.os.Environment
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainViewModel(application: Application) : AndroidViewModel(application) {

    // LiveData to hold the list of photos
    private val _photos = MutableLiveData<List<File>>()
    val photos: LiveData<List<File>> get() = _photos

    // Directory paths
    private val downloadDirectoryPath = Environment.getExternalStoragePublicDirectory(
        Environment.DIRECTORY_DOWNLOADS
    ).absolutePath
    private val storageDir: File = File("$downloadDirectoryPath/Captured photos")

    init {
        loadPhotos()
    }

    // Load photos from storage and update the LiveData
    private fun loadPhotos() {
        val photosList = storageDir.listFiles()?.toList() ?: emptyList()
        _photos.value = photosList.reversed() // Reverse the list for display
    }

    // Add a new photo to the list and save it to storage
    fun addPhoto(photo: File) {
        val currentPhotos = _photos.value.orEmpty().toMutableList()
        Log.e("TAG", "addPhoto: "+currentPhotos )
        currentPhotos.add(0,photo)
        _photos.value = currentPhotos
    }

    // Save the bitmap to a file with a timestamp in the app's external files directory
    fun saveBitmap(bitmap: Bitmap): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())

        // Save a copy of the image to the Downloads/Captured photos directory
        val downloadDirectoryPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).absolutePath
        val directory = File("$downloadDirectoryPath/Captured photos")
        if (!directory.exists()) {
            directory.mkdirs()
        }

        val imageFile = File(directory, "JPEG_${timeStamp}.jpg")

        try {
            // Save the bitmap to the directory
            val outputStream: OutputStream = FileOutputStream(imageFile)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            outputStream.flush()
            outputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return imageFile
    }
}
