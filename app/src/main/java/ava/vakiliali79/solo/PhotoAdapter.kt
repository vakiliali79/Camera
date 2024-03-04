package ava.vakiliali79.solo

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ava.vakiliali79.solo.databinding.ItemPhotoBinding
import com.bumptech.glide.Glide
import java.io.File

class PhotoAdapter(private val photos: List<File>) : RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder>() {

    // Create a new ViewHolder instance by inflating the item_photo.xml layout
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val binding = ItemPhotoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PhotoViewHolder(binding)
    }

    // Bind data to the ViewHolder at the specified position
    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        holder.bind(photos[position])
    }

    // Return the total number of items in the data set
    override fun getItemCount(): Int = photos.size

    // ViewHolder class to hold references to the views in each list item
    inner class PhotoViewHolder(private val binding: ItemPhotoBinding) : RecyclerView.ViewHolder(binding.root) {

        // Bind the photo to the ImageView using Glide
        fun bind(photo: File) {
            // Use Glide to load the image into the ImageView
            Glide.with(binding.root.context)
                .load(photo)
                .into(binding.photoImageView)
        }
    }
}
