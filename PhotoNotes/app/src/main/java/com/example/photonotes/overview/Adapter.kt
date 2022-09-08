package com.example.photonotes.overview


import android.graphics.BitmapFactory
import android.view.*
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.photonotes.database.PhotoNotes
import com.example.photonotes.databinding.RecyclerviewItemsBinding

class Adapter(private val listener: OnItemCLickListener): ListAdapter<PhotoNotes, Adapter.ViewHolder>(NotesDiffCallback()) {

    interface OnItemCLickListener {
        fun onItemClick(position: Int)
        fun onItemLongClick(position: Int)
    }

    inner class ViewHolder(private val binding: RecyclerviewItemsBinding/*itemView: View*/) : RecyclerView.ViewHolder(binding.root/*itemView*/), View.OnClickListener, View.OnLongClickListener{
        val title = binding.textViewTitle
        val description = binding.textViewDescription
        val date = binding.textViewDate
        val photo = binding.imageviewRecycler

        init {
            itemView.setOnClickListener(this)
            itemView.setOnLongClickListener(this)
        }

        override fun onLongClick(v: View?): Boolean {
            listener.onItemClick(adapterPosition)
            return true
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onItemClick(position)
            }
        }
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = RecyclerviewItemsBinding.inflate(layoutInflater, parent, false)
        //val view = layoutInflater.inflate(R.layout.recyclerview_items, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.title.text = item.title
        holder.description.text = item.description
        holder.date.text = item.date
        //holder.photo.setImageBitmap(BitmapFactory.decodeByteArray(item.photo, 20, item.photo.size))
        val options = BitmapFactory.Options().apply {
            inJustDecodeBounds = true
            inSampleSize = 5//calculateInSampleSize()
        }
        //holder.photo.setImageBitmap(BitmapFactory.decodeByteArray(item.photo,0,item.photo.size,options))
    }



}

class NotesDiffCallback: DiffUtil.ItemCallback<PhotoNotes>(){
    override fun areItemsTheSame(oldItem: PhotoNotes, newItem: PhotoNotes): Boolean {
        return oldItem.photoID == newItem.photoID
    }

    override fun areContentsTheSame(oldItem: PhotoNotes, newItem: PhotoNotes): Boolean {
        return oldItem == newItem
    }

}