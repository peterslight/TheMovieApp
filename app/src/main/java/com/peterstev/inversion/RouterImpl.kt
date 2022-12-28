package com.peterstev.inversion

import android.app.Activity
import androidx.annotation.IdRes
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import com.peterstev.domain.model.Movie
import com.peterstev.domain.routing.Router
import com.peterstev.fragment.MainFragmentDirections
import javax.inject.Inject

class RouterImpl @Inject constructor(
    private val hostActivity: Activity,
    @IdRes private val navHostId: Int,
) : Router {

    private val navController: NavController
        get() = hostActivity.findNavController(navHostId)

    private fun navigate(directions: NavDirections) {
        navController.navigate(directions)
    }

    override fun toDetail(movie: Movie) {
        navigate(MainFragmentDirections.toDetails(movie))
    }
}
