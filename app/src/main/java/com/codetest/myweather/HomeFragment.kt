package com.codetest.myweather

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.codetest.myweather.databinding.HomeFragmentBinding
import com.codetest.myweather.room.CityLocationsViewModel
import com.codetest.myweather.room.CityLocationsViewModelFactory
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private lateinit var binding: HomeFragmentBinding

    private val cityLocationsViewModel: CityLocationsViewModel by viewModels {
        CityLocationsViewModelFactory((activity?.application as MyWeatherApplication).repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)

        binding = HomeFragmentBinding.inflate(layoutInflater)

        val adapter = CityLocationListAdapter()
        adapter.setItemListener(object : RecyclerClickListener {

            // Tap the 'X' to delete the item
            override fun onItemRemoveClick(position: Int) {
                // Get the list of city locations
                val cityLocations = adapter.currentList.toMutableList()
                val removeCityLocation = cityLocations[position]

                lifecycleScope.launch {
                    cityLocationsViewModel.delete(removeCityLocation)
                }

                Toast.makeText(context, getString(R.string.location_removed, removeCityLocation.city_location), Toast.LENGTH_SHORT).show()
            }

            // Tap the item to view
            override fun onItemClick(position: Int) {
                // Get the list of city locations
                val cityLocations = adapter.currentList.toMutableList()
                val openCityLocation = cityLocations[position]

                val action = HomeFragmentDirections.actionCity(openCityLocation)
                findNavController().navigate(action)
            }
        })

        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(context)

        cityLocationsViewModel.allCityLocations.observe(viewLifecycleOwner) { cityLocations ->
            // Update the cached copy of the words in the adapter.
            cityLocations.let { adapter.submitList(it) }
        }

        binding.addLocationButton.setOnClickListener {
            findNavController().navigate(R.id.action_add_location)
        }

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.home_menu, menu)
    }
}
