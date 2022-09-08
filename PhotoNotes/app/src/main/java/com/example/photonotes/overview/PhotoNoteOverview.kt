package com.example.photonotes.overview

import android.app.Activity
import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.SearchView
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.view.MotionEventCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.photonotes.MainActivity
import com.example.photonotes.R
import com.example.photonotes.database.PhotoDatabase
import com.example.photonotes.databinding.FragmentPhotoNoteOverviewBinding


class PhotoNoteOverview : Fragment(), Adapter.OnItemCLickListener {
    private lateinit var viewModel: OverviewViewModel
    private lateinit var searchQuery : String
    private lateinit var adapter: Adapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        //val layout = inflater.inflate(R.layout.fragment_photo_note_overview, container, false)
        val binding: FragmentPhotoNoteOverviewBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_photo_note_overview, container, false)
        activity?.title = "Photo Notes"
        setHasOptionsMenu(true)

        val application = requireNotNull(this.activity).application
        val dataSource = PhotoDatabase.getInstance(application).photoDatabaseDao
        val viewModelFactory = OverviewViewModelFactory(application)
        viewModel = ViewModelProvider(this, viewModelFactory).get(OverviewViewModel::class.java)
        val recyclerView: RecyclerView = binding.recyclerView
        adapter = Adapter(this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)

        viewModel.allNotes.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })

        binding.floatingActionButton.setOnClickListener(View.OnClickListener {
            this.findNavController().navigate(PhotoNoteOverviewDirections.actionPhotoNoteOverviewToAddNotes())
        })


        val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT){
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                viewModel.allNotes.value?.get(viewHolder.adapterPosition)?.let {
                    viewModel.delete(it) }
            }
        })

        itemTouchHelper.attachToRecyclerView(binding.recyclerView)

        return binding.root
    }

    override fun onItemClick(position: Int) {
        val photo = viewModel.allNotes.value?.get(position)
        val primaryKey = photo?.photoID
        if (primaryKey != null){
            this.findNavController().navigate(PhotoNoteOverviewDirections.actionPhotoNoteOverviewToAddNotes(primaryKey))
        }

        //Toast.makeText(context, "deleted", Toast.LENGTH_SHORT).show()
    }

    override fun onItemLongClick(position: Int) {
        Toast.makeText(context, position.toString(), Toast.LENGTH_SHORT).show()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.search_view, menu)
        // Associate searchable configuration with the SearchView
        val item = menu.findItem(R.id.search)
        val searchView = SearchView((context as MainActivity).supportActionBar?.themedContext)
        item.actionView = searchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    viewModel.setQuery(query)
                }
                viewModel.searchNotes.observe(viewLifecycleOwner, Observer {
                    it?.let {
                        adapter.submitList(it)
                    }
                })
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
                    viewModel.setQuery(newText)
                }
                viewModel.searchNotes.observe(viewLifecycleOwner, Observer {
                    it?.let {
                        adapter.submitList(it)
                    }
                })
                return true
            }

        })
    }


}

