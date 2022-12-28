package com.peterstev.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.peterstev.R
import com.peterstev.databinding.FragmentDetailBinding
import com.peterstev.domain.model.Movie
import com.peterstev.injection.AppComponent
import com.peterstev.viewmodel.DetailViewModel
import com.peterstev.viewmodel.DetailViewModel.ExclusionState.OnExclude
import com.peterstev.viewmodel.DetailViewModel.ExclusionState.OnInclude
import com.peterstev.viewmodel.DetailViewModel.FavouriteState.OnFavouriteAdded
import com.peterstev.viewmodel.DetailViewModel.FavouriteState.OnFavouriteRemoved
import javax.inject.Inject

class DetailFragment : Fragment(R.layout.fragment_detail) {

    companion object {
        private const val MOVIE_KEY = "movie"
    }

    @Inject
    lateinit var viewModel: DetailViewModel
    lateinit var binding: FragmentDetailBinding
    lateinit var movie: Movie

    override fun onAttach(context: Context) {
        super.onAttach(context)
        AppComponent.create(this).inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return if (::binding.isInitialized) binding.root else {
            binding = FragmentDetailBinding.inflate(layoutInflater)
            binding.root
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        movie = requireArguments().getSerializable(MOVIE_KEY) as Movie
        observeExclusionState()
        observeFavouriteState()
        viewModel.getSingleMovie(movie)
        setupViews()
    }

    private fun setupViews() {
        with(binding) {
            val backdropImage = when {
                movie.backdropPath.isNotEmpty() -> movie.backdropPath
                movie.posterPath.isNotEmpty() -> movie.posterPath
                else -> ""
            }

            val posterImage = when {
                movie.posterPath.isNotEmpty() -> movie.posterPath
                movie.backdropPath.isNotEmpty() -> movie.backdropPath
                else -> ""
            }
            backdropImage.isNotEmpty().let { if (it) loadImage(backdropImage, backdrop) }
            posterImage.isNotEmpty().let { if (it) loadImage(posterImage, avatar) }
            title.text = movie.title
            overview.text = movie.overview
            rating.text = movie.voteAverage.toString()

            binding.btFavourite.text = if (movie.isFavourite)
                getString(R.string.remove_favourite)
            else getString(R.string.add_favourite)

            btFavourite.setOnClickListener {
                if (btFavourite.text.toString() == getString(R.string.add_favourite)) {
                    viewModel.onAddFavClick(movie)
                } else viewModel.onRemoveFavClick(movie)
            }
            btExclude.setOnClickListener {
                if (btExclude.text.toString() == getString(R.string.exclude_movie)) {
                    viewModel.onExcludeClick(movie)
                } else viewModel.onIncludeClick(movie)
            }
        }
    }

    private fun observeExclusionState() {
        viewModel.exclusionLiveData.observe(viewLifecycleOwner) {
            when (it) {
                OnExclude -> binding.btExclude.text = getString(R.string.include_movie)
                OnInclude -> binding.btExclude.text = getString(R.string.exclude_movie)
            }
        }
    }

    private fun observeFavouriteState() {
        viewModel.favouriteLiveData.observe(viewLifecycleOwner) {
            when (it) {
                OnFavouriteAdded -> binding.btFavourite.text = getString(R.string.remove_favourite)
                OnFavouriteRemoved -> binding.btFavourite.text = getString(R.string.add_favourite)
            }
        }
    }

    private fun loadImage(url: String, view: ImageView) {
        Glide.with(requireContext()).load(url).into(view)
    }
}
