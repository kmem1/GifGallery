package com.example.gifgallery.presentation

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.PagingData
import androidx.recyclerview.widget.GridLayoutManager
import com.example.gifgallery.R
import com.example.gifgallery.databinding.FragmentGalleryBinding
import com.example.gifgallery.domain.models.GifModel
import com.example.gifgallery.presentation.recycler.GifsAdapter
import com.example.gifgallery.presentation.recycler.GifsLoaderStateAdapter
import com.example.gifgallery.utils.base.BaseBindingFragment
import com.example.gifgallery.utils.recycler.SimpleSpacesGridItemDecoration
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class GalleryFragment :
    BaseBindingFragment<FragmentGalleryBinding, GalleryViewModel>(R.layout.fragment_gallery) {

    override val vm: GalleryViewModel by viewModels()

    private var gifsAdapter: GifsAdapter? = null
    private val footerAdapter = GifsLoaderStateAdapter()

    private var collectingJob: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onInitBinding(binding: FragmentGalleryBinding, savedInstanceState: Bundle?) {
        super.onInitBinding(binding, savedInstanceState)

        setupRecycler()
        setupViewModel()
    }

    private fun setupRecycler() {
        gifsAdapter = GifsAdapter()
        binding.rvGifs.apply {
            val gridLayoutManager = GridLayoutManager(context, RECYCLER_SPAN_COUNT)
            layoutManager = gridLayoutManager
            gifsAdapter?.let {
                adapter = it.withLoadStateFooter(footerAdapter)
            }

            // To put loading(or error) indicator in the center
            gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return if (position == gifsAdapter?.itemCount && footerAdapter.itemCount > 0) {
                        2
                    } else {
                        1
                    }
                }
            }

            addItemDecoration(SimpleSpacesGridItemDecoration(includeEdge = true))
        }
    }

    private fun setupViewModel() {
        vm.setTrendingGifs()
        lifecycleScope.launchWhenCreated {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                vm.gifsFlow.collectLatest { pagingDataFlow ->
                    collectDataFromFlow(pagingDataFlow)
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.fragment_gallery, menu)

        val searchItem: MenuItem = menu.findItem(R.id.menu_item_search)
        val searchView = searchItem.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(queryText: String): Boolean {
                return false
            }

            override fun onQueryTextChange(queryText: String): Boolean {
                vm.searchGifs(queryText)
                return false
            }
        })
    }

    private fun collectDataFromFlow(flow: Flow<PagingData<GifModel>>) {
        collectingJob?.cancel()
        collectingJob = lifecycleScope.launchWhenCreated {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                flow.collectLatest { pagingData ->
                    setNewAdapter()
                    gifsAdapter?.submitData(pagingData)
                }
            }
        }
    }

    private fun setNewAdapter() {
        gifsAdapter = GifsAdapter()
        binding.rvGifs.apply {
            gifsAdapter?.let {
                adapter = it.withLoadStateFooter(footerAdapter)
            }
        }
    }

    companion object {
        private const val RECYCLER_SPAN_COUNT = 2
    }
}