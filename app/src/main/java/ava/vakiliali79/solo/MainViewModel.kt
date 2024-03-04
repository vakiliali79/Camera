package ava.vakiliali79.solo

import android.app.Application
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

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
        currentPhotos.add(photo)
        _photos.value = currentPhotos.reversed()

        // Save the new list to storage
        savePhotosToStorage(currentPhotos)
    }

    // Save the list of photos to storage in JPG format
    private fun savePhotosToStorage(photosList: List<File>) {
        // Save the list of photos to storage in JPG format
        // You may want to implement logic to save them in a specific order if needed
        // This is just a basic example
        storageDir.mkdirs()
        photosList.forEachIndexed { index, file ->
            convertBitmapToJPG(file, file)
        }
    }

    // Convert a bitmap to JPG format and save it to the specified output file
    private fun convertBitmapToJPG(inputFile: File, outputFile: File) {
        try {
            val bitmap = BitmapFactory.decodeFile(inputFile.absolutePath)
            val outputStream = FileOutputStream(outputFile)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            outputStream.flush()
            outputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}
