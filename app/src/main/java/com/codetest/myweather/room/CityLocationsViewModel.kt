package com.codetest.myweather.room

import androidx.lifecycle.*
import kotlinx.coroutines.launch

class CityLocationsViewModel(private val repository: CityLocationRepository) : ViewModel() {

    // Using LiveData and caching what allCityLocations returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.
    val allCityLocations: LiveData<List<CityLocation>> = repository.allCityLocations.asLiveData()

    /**
     * Launching a new coroutine to delete the data in a non-blocking way
     */
    fun delete(cityLocation: CityLocation) = viewModelScope.launch {
        repository.delete(cityLocation)
    }
}

class CityLocationsViewModelFactory(private val repository: CityLocationRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CityLocationsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CityLocationsViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
