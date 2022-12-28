package com.peterstev.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.peterstev.R
import com.peterstev.adapter.MainAdapter
import com.peterstev.adapter.MovieItemClickListener
import com.peterstev.adapter.SearchAdapter
import com.peterstev.databinding.FragmentMainBinding
import com.peterstev.domain.model.Movie
import com.peterstev.injection.AppComponent
import com.peterstev.util.hide
import com.peterstev.util.hideKeyboard
import com.peterstev.util.networkIsAvailable
import com.peterstev.util.show
import com.peterstev.viewmodel.MainViewModel
import javax.inject.Inject

class MainFragment : Fragment(R.layout.fragment_main), MovieItemClickListener {

    @Inject
    lateinit var viewModel: MainViewModel
    private lateinit var binding: FragmentMainBinding
    private lateinit var searchAdapter: SearchAdapter
    private lateinit var mainAdapter: MainAdapter
    private var totalPages = 0
    private var movieList = mutableListOf<Movie>()


    override fun onAttach(context: Context) {
        super.onAttach(context)
        AppComponent.create(this).inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return if (::binding.isInitialized) binding.root else {
            binding = FragmentMainBinding.inflate(layoutInflater)
            binding.root
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupBackPressedListeners()
        observeMovieList()
        observeFavList()
        setupViews()
        viewModel.getFavourites()
    }

    private fun setupViews() {
        mainAdapter = MainAdapter(emptyList<Movie>().toMutableList(), this)

        with(binding) {
            searchRecyclerView.layoutManager = LinearLayoutManager(requireContext())
            searchRecyclerView.setHasFixedSize(true)
            searchAdapter = SearchAdapter(
                movieList,
                this@MainFragment,
                binding.searchRecyclerView
            )
            searchRecyclerView.adapter = searchAdapter

            recyclerView.layoutManager = LinearLayoutManager(requireContext())
            recyclerView.setHasFixedSize(true)
            recyclerView.adapter = mainAdapter

            emptyFavourite.emptySearch.setOnClickListener { searchView.show() }

            searchView
                .editText
                .setOnEditorActionListener { _, _, _ ->
                    cleanUpSearch()
                    val text = searchView.text.toString()
                    when {
                        networkIsAvailable() && text.trim().isNotEmpty() -> {
                            searchBar.text = text
                            hideKeyboard()
                            cleanUpSearch()
                            viewModel.searchMovie(text.trim())
                            true
                        }
                        text.trim().isEmpty() -> {
                            Toast.makeText(requireContext(), getString(R.string.enter_text_to_search), Toast.LENGTH_SHORT).show()
                            true
                        }
                        else -> {
                            onEmptySearchResult(true, getString(R.string.no_internet), true)
                            true
                        }
                    }
                }
        }
    }

    private fun observeMovieList() {
        viewModel.movieLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is MainViewModel.MovieState.Error -> {
                    val message = if (it.throwable.message.toString().contains(getString(R.string.network_error), true)
                        || it.throwable.message.toString().contains(getString(R.string.network_connect), true)
                    )
                        getString(R.string.no_internet) else it.throwable.message.toString()
                    onEmptySearchResult(true, message, isError = true)
                    binding.progressLoadMore.root.hide()
                    onProgress(false)
                }
                is MainViewModel.MovieState.LoadMoreProgress -> when {
                    it.isLoading -> binding.progressLoadMore.root.show()
                    else -> binding.progressLoadMore.root.hide()
                }
                is MainViewModel.MovieState.Loading -> {
                    if (it.isLoading) onEmptySearchResult(false)
                    onProgress(it.isLoading)
                }
                is MainViewModel.MovieState.Success -> {
                    movieList.addAll(it.data.movies)
                    if (movieList.isEmpty()) {
                        onEmptySearchResult(true, binding.searchView.text.toString())
                        return@observe
                    }
                    totalPages = it.data.totalPages
                    searchAdapter.updateList(
                        it.data.movies.toMutableList()
                    ).apply {
                        searchAdapter.stopLoading()
                        viewModel.incrementPage()
                    }
                }
            }
        }
    }

    private fun onEmptySearchResult(shouldShow: Boolean, message: String = "", isError: Boolean = false) {
        if (shouldShow) binding.emptySearchLayout.root.show()
        else binding.emptySearchLayout.root.hide()

        if (isError) binding.emptySearchLayout.itemText.text = message
        else binding.emptySearchLayout.itemText.text = getString(
            R.string.we_found_no_results_for_try_searching_another_movie,
            message
        )
    }

    private fun onProgress(shouldShow: Boolean) {
        if (shouldShow) binding.progress.root.show()
        else binding.progress.root.hide()
    }

    private fun observeFavList() {
        viewModel.favLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is MainViewModel.FavouriteState.OnError ->
                    Toast.makeText(requireContext(), it.throwable.message.toString(), Toast.LENGTH_SHORT).show()
                is MainViewModel.FavouriteState.OnDelete ->
                    Toast.makeText(requireContext(), getString(R.string.removed), Toast.LENGTH_SHORT).show()
                is MainViewModel.FavouriteState.OnLoad -> {
                    mainAdapter.updateList(it.data)
                    if (it.data.isNotEmpty()) binding.emptyFavourite.root.hide()
                    else binding.emptyFavourite.root.show()
                }
            }
        }
    }

    override fun onItemClick(movie: Movie) {
        viewModel.onListItemClick(movie)
    }

    override fun onFavouriteClick(movie: Movie) {
        viewModel.onFavouriteClick(movie)
    }


    override fun loadMore() {
        if (morePagesAvailable()) {
            viewModel.loadMore()
        } else Toast.makeText(requireContext(), getString(R.string.all_caught_up), Toast.LENGTH_SHORT).show()
    }

    private fun cleanUpSearch() {
        movieList.clear()
        searchAdapter.reset()
        viewModel.resetPage()
    }

    private fun setupBackPressedListeners() {
        val callback = object : OnBackPressedCallback(false) {
            override fun handleOnBackPressed() = binding.searchView.hide()
        }

        requireActivity()
            .onBackPressedDispatcher
            .addCallback(viewLifecycleOwner, callback)

        binding.searchView.addTransitionListener { searchView, _, _ ->
            callback.isEnabled = searchView.isShowing
        }
    }

    private fun morePagesAvailable(): Boolean {
        return totalPages >= viewModel.getCurrentPage()
    }
}
