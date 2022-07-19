package com.codetest.myweather

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codetest.myweather.room.CityLocation
import com.codetest.myweather.room.CityLocationRepository
import kotlinx.coroutines.launch

class AddLocationViewModel : ViewModel() {

    var repository: CityLocationRepository? = null

    private val _getLocationInformation = MutableLiveData<ResponseStatusCallbacks<LocationModel>>()
    val getLocationInformation: LiveData<ResponseStatusCallbacks<LocationModel>>
        get() = _getLocationInformation

    fun getLocationInfo(context: Context, latitude: String, longitude: String) {
        viewModelScope.launch {
            _getLocationInformation.value = ResponseStatusCallbacks(ResponseStatus.LOADING, null)
            GeoCoderUtil().execute(context, latitude, longitude, object :
                LoadDataCallback<LocationModel> {
                override fun onDataLoaded(response: LocationModel) {
                    _getLocationInformation.value =
                        ResponseStatusCallbacks(ResponseStatus.SUCCESS, response)
                }

                override fun onDataNotAvailable(errorCode: Int, reasonMsg: String) {
                    _getLocationInformation.value =
                        ResponseStatusCallbacks(ResponseStatus.ERROR, null)
                }
            })
        }
    }

    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    fun insert(cityLocation: CityLocation) = viewModelScope.launch {
        repository?.insert(cityLocation)
    }
}
