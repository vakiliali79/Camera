package ava.vakiliali79.solo

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Environment
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import ava.vakiliali79.solo.databinding.ActivityMainBinding
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var photoFile: File

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize ViewModel
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        // Set up the RecyclerView
        binding.showImageRecyclerView.layoutManager = GridLayoutManager(this, 3)
        viewModel.photos.observe(this) { photos ->
            // Update RecyclerView with the list of photos
            val adapter = PhotoAdapter(photos)
            binding.showImageRecyclerView.adapter = adapter
        }

        // Set a click listener for the camera button
        binding.cameraFab.setOnClickListener {
            openCamera()
        }
    }

    // Open the camera to capture a photo
    private fun openCamera() {
        val takePictureIntent = Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE)
        resultLauncher.launch(takePictureIntent)
    }

    // Handle the result of the camera activity
    private val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            // Photo was taken successfully, update the list
            val imageBitmap = result.data?.extras?.get("data") as? Bitmap
            imageBitmap?.let {
                photoFile = saveBitmap(it)
                viewModel.addPhoto(photoFile)
            }
        }
    }

    // Save the bitmap to a file with a timestamp in the app's external files directory
    private fun saveBitmap(bitmap: Bitmap): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir = File(application.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "photos")

        if (!storageDir.exists()) {
            storageDir.mkdirs()
        }

        var imageFile = File(storageDir, "JPEG_${timeStamp}.jpg")

        try {
            // Save the bitmap to the file
            val stream: OutputStream = FileOutputStream(imageFile)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            stream.flush()
            stream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        // Save a copy of the image to the Downloads/Captured photos directory
        val downloadDirectoryPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).absolutePath
        val directory = File("$downloadDirectoryPath/Captured photos")
        if (!directory.exists()) {
            directory.mkdirs()
        }

        imageFile = File(directory, "JPEG_${timeStamp}.jpg")

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
