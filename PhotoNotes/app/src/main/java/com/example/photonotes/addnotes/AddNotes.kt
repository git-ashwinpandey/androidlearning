package com.example.photonotes.addnotes

import android.Manifest
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.graphics.drawable.toBitmap
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.photonotes.database.PhotoNotes
import com.example.photonotes.databinding.FragmentAddNotesBinding
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import androidx.test.core.app.ApplicationProvider.getApplicationContext

import androidx.core.content.FileProvider
import androidx.test.core.app.ApplicationProvider
import com.example.photonotes.*
import java.io.OutputStream
import java.lang.Exception


class AddNotes : Fragment() {
    lateinit var binding: FragmentAddNotesBinding
    private lateinit var addViewModel: AddViewModel
    lateinit var photoNotes: PhotoNotes
    private lateinit var takePicture: ActivityResultLauncher<Uri>
    private var providerFile: Uri? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_notes, container, false)
        val arguments = AddNotesArgs.fromBundle(requireArguments())
        val application = requireNotNull(this.activity).application

        val viewModelFactory = AddViewModelFactory(arguments.noteKey, application)
        addViewModel = ViewModelProvider(this, viewModelFactory).get(AddViewModel::class.java)
        val primaryKey = arguments.noteKey
        setHasOptionsMenu(true)
        addViewModel.selectedPhoto.observe(viewLifecycleOwner, Observer {
            loadFromDatabase(it)
        })
        if (primaryKey == -1L) {
            activity?.title = "Add Notes"
        } else {
            activity?.title = "Edit Notes"
        }

        val requestPermission =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) {
                if (it == true) {
                    //val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    //takePicture.launch(intent)
                    //val photoFile: File = createImageFile()
                    //providerFile = FileProvider.getUriForFile(requireContext(), "com.example.photonotes.fileprovider", photoFile)
                    saveToGallery()
                    takePicture.launch(providerFile)
                }
                val temp = it.toString()
                Toast.makeText(context, "Permission $temp", Toast.LENGTH_SHORT).show()
            }

//        takePicture = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
//            if (it != null) {
//                val temp: Bundle? = it.data?.extras
//                val bitmap: Bitmap = temp?.get("data") as Bitmap
//                binding.imageView.setImageBitmap(bitmap)
//            }
//
//        }

        takePicture = registerForActivityResult(ActivityResultContracts.TakePicture()) {
            if (it == true) {
                binding.imageView.setImageURI(providerFile)
                //saveToGallery(binding.imageView.drawable.toBitmap())
            }
        }

        binding.imageView.setOnClickListener(View.OnClickListener {
            requestPermission.launch(Manifest.permission.CAMERA)
        })
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.add_save, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.save_option -> {
                saveToDataBase()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun loadFromDatabase(photoNotes: PhotoNotes) {
        binding.edittextTitle.setText(photoNotes.title)
        binding.edittextDescription.setText(photoNotes.description)
        binding.dateView.text = String.format(R.string.date_created.toString(), photoNotes.date)
        binding.imageView.setImageBitmap(
            BitmapFactory.decodeByteArray(
                photoNotes.photo,
                0,
                photoNotes.photo.size
            )
        )
    }

    private fun saveToDataBase() {
        val title = binding.edittextTitle.text.toString()
        val description = binding.edittextDescription.text.toString()
        val photo = imageToBitmap(binding.imageView.drawable.toBitmap()) //imageToBitmap(MediaStore.Images.Media.getBitmap(requireContext().contentResolver, providerFile))//
        val date = getCurrentDateTime().toString("dd/MM/yyyy")
        addViewModel.saveToDatabase(title, description, date , photo)
        this.findNavController().navigate(AddNotesDirections.actionAddNotesToPhotoNoteOverview())
    }

    lateinit var currentPhotoPath: String

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(Date())
        val storageDir: File? = activity?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile("JPEG_${timeStamp}_", /* prefix */".jpg", /* suffix */storageDir /* directory */)
            .apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }
    }

    private fun saveToGallery() {
        val resolver: ContentResolver = requireActivity().contentResolver
        val contentValues: ContentValues = ContentValues()
        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, "Image_"+ ".jpg")
        contentValues.put(MediaStore.MediaColumns.MIME_TYPE,"image/jpg")
        contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES+ File.separator + "Test")
        Log.i("content15192", MediaStore.MediaColumns.RELATIVE_PATH + Environment.DIRECTORY_PICTURES+ File.separator)
        providerFile = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,contentValues)
        //bitmap.compress(Bitmap.CompressFormat.JPEG, 100 , fos)
        Log.i("content15192", providerFile?.path.toString())
        var test: Uri = Uri.fromFile(File(contentValues.get(MediaStore.MediaColumns.RELATIVE_PATH).toString()))
        Log.i("content15192", test.path.toString())

    }


}