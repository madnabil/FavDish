package com.muhmmad.favdish.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.muhmmad.favdish.application.FavDishApplication
import com.muhmmad.favdish.databinding.FragmentFavoriteDishesBinding
import com.muhmmad.favdish.model.entities.FavDish
import com.muhmmad.favdish.view.activities.MainActivity
import com.muhmmad.favdish.view.adapters.FavDishAdapter
import com.muhmmad.favdish.viewmodel.FavDishViewModel
import com.muhmmad.favdish.viewmodel.FavDishViewModelFactory

class FavoriteDishesFragment : Fragment() {

    private var binding: FragmentFavoriteDishesBinding? = null
    private val favDishViewModel: FavDishViewModel by viewModels {
        FavDishViewModelFactory((requireActivity().application as FavDishApplication).repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentFavoriteDishesBinding.inflate(inflater, container, false)


        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        favDishViewModel.favoriteDishes.observe(viewLifecycleOwner) { dishes ->
            dishes.let {

                binding!!.rvFavoriteDishesList.layoutManager =
                    GridLayoutManager(requireActivity(), 2)

                val adapter = FavDishAdapter(this)
                binding!!.rvFavoriteDishesList.adapter = adapter

                if (it.isNotEmpty()) {
                    binding!!.rvFavoriteDishesList.visibility = View.VISIBLE
                    binding!!.tvNoFavoriteDishesAvailable.visibility = View.GONE
                    adapter.dishesList(it)
                } else {
                    binding!!.rvFavoriteDishesList.visibility = View.GONE
                    binding!!.tvNoFavoriteDishesAvailable.visibility = View.VISIBLE
                }
            }
        }

    }

    fun dishDetails(favDish: FavDish) {
        findNavController().navigate(
            FavoriteDishesFragmentDirections.actionFavoriteDishesToDishDetails(
                favDish
            )
        )
        if (requireActivity() is MainActivity) {
            (activity as MainActivity).hideBottomNavigationView()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    override fun onResume() {
        super.onResume()
        if (requireActivity() is MainActivity) {
            (activity as MainActivity).showBottomNavigationView()
        }
    }

}