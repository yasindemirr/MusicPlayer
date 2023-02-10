package com.demir.musicplayer.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.demir.musicplayer.R
import com.demir.musicplayer.SongViewModel
import com.demir.musicplayer.adapter.SongAdapter
import com.demir.musicplayer.databinding.FragmentFavoritesBinding
import com.google.android.material.snackbar.Snackbar

class FavoritesFragment : Fragment() {
    private lateinit var binding:FragmentFavoritesBinding
    private lateinit var songAdapter: SongAdapter
    private lateinit var songViewModel: SongViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentFavoritesBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.setActionBar(binding.toolbar)
        songViewModel=ViewModelProvider(this)[SongViewModel::class.java]
        setUpRecyclerView()
        observeData()
        deleteSavedSongs(view)
        songAdapter.onlick={
            val bundle=Bundle().apply {
                putParcelable("song",it)
            }
            findNavController().navigate(R.id.action_favoritesFragment_to_playMusicFragment,bundle)
        }
    }

    private fun deleteSavedSongs(view:View) {
        val itemTouchHelperCallBack=object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true

            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position=viewHolder.adapterPosition
                val song= songAdapter.differ.currentList[position]
                songViewModel.deleteSong(song)
                Snackbar.make(view,"Deleted song successfully", Snackbar.LENGTH_SHORT).apply {
                    setAction("undo"){
                        songViewModel.upsert(song)
                    }
                    show()
                }
            }

        }
        ItemTouchHelper(itemTouchHelperCallBack).apply {
            attachToRecyclerView(binding.rvFavoriteSongList)
        }
    }

    private fun observeData() {
        songViewModel.readAllSongs().observe(viewLifecycleOwner, Observer { songList->
            songList?.let {
                songAdapter.differ.submitList(it)

            }
        })
    }

    private fun setUpRecyclerView() {
        songAdapter = SongAdapter()
        binding.rvFavoriteSongList.apply {
            layoutManager = LinearLayoutManager(activity)
            setHasFixedSize(true)
            adapter = songAdapter
            addItemDecoration(object : DividerItemDecoration(
                activity, LinearLayout.VERTICAL
            ) {})
        }
    }

}