package com.codetest.myweather

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CityFragmentViewModel : ViewModel() {

    private val _getWeatherInformation = MutableLiveData<ResponseStatusCallbacks<CityWeatherModel>>()
    val getWeatherInformation: LiveData<ResponseStatusCallbacks<CityWeatherModel>>
        get() = _getWeatherInformation

    fun getWeatherInfo(context: Context, latitude: String, longitude: String) {
        _getWeatherInformation.value = ResponseStatusCallbacks(ResponseStatus.LOADING, null)
        viewModelScope.launch(Dispatchers.IO) {
            OpenWeatherUtil().execute(latitude, longitude, object :
                LoadDataCallback<CityWeatherModel> {
                override fun onDataLoaded(response: CityWeatherModel) {
                    viewModelScope.launch {
                        _getWeatherInformation.value =
                            ResponseStatusCallbacks(ResponseStatus.SUCCESS, response)
                    }
                }

                override fun onDataNotAvailable(errorCode: Int, reasonMsg: String) {
                    viewModelScope.launch {
                        _getWeatherInformation.value =
                            ResponseStatusCallbacks(ResponseStatus.ERROR, null)
                    }
                }
            })
        }
    }
}
