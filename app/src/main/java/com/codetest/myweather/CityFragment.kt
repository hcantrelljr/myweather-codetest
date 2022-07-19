package com.codetest.myweather

import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.codetest.myweather.databinding.CityFragmentBinding
import com.codetest.myweather.room.CityLocation
import kotlinx.coroutines.launch

class CityFragment : Fragment() {

    private lateinit var binding: CityFragmentBinding
    private lateinit var cityLocation: CityLocation
    private lateinit var viewModel: CityFragmentViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = CityFragmentBinding.inflate(layoutInflater)

        val safeArgs: CityFragmentArgs by navArgs()
        cityLocation = safeArgs.cityLocation

        viewModel = ViewModelProvider.NewInstanceFactory().create(CityFragmentViewModel::class.java)
        viewModel.getWeatherInformation.observe(viewLifecycleOwner) {
            it?.let {
                when (it.status) {
                    ResponseStatus.ERROR -> {

                    }
                    ResponseStatus.LOADING -> {

                    }
                    ResponseStatus.SUCCESS -> {
                        it.data?.let { model ->
                            binding.temperature.text = model.today.temperature + "°"
                            binding.humidity.text = Html.fromHtml("Humidity: <b>" + model.today.humidity + "%</b>")
                            binding.windSpeed.text = Html.fromHtml("Wind: <b>" + model.today.windSpeed + " miles/hour</b>")
                            Glide.with(this).load(model.today.weatherIconUrl).into(binding.weatherIcon)

                            binding.temperature1.text = model.forecast.get(0).temperature + "°"
                            binding.humidity1.text = Html.fromHtml("Humidity: <b>" + model.forecast.get(0).humidity + "%</b>")
                            binding.windSpeed1.text = Html.fromHtml("Wind: <b>" + model.forecast.get(0).windSpeed + " miles/hour</b>")
                            Glide.with(this).load(model.forecast.get(0).weatherIconUrl).into(binding.weatherIcon1)

                            binding.temperature2.text = model.forecast.get(1).temperature + "°"
                            binding.humidity2.text = Html.fromHtml("Humidity: <b>" + model.forecast.get(1).humidity + "%</b>")
                            binding.windSpeed2.text = Html.fromHtml("Wind: <b>" + model.forecast.get(1).windSpeed + " miles/hour</b>")
                            Glide.with(this).load(model.forecast.get(1).weatherIconUrl).into(binding.weatherIcon2)

                            binding.temperature3.text = model.forecast.get(2).temperature + "°"
                            binding.humidity3.text = Html.fromHtml("Humidity: <b>" + model.forecast.get(2).humidity + "%</b>")
                            binding.windSpeed3.text = Html.fromHtml("Wind: <b>" + model.forecast.get(2).windSpeed + " miles/hour</b>")
                            Glide.with(this).load(model.forecast.get(2).weatherIconUrl).into(binding.weatherIcon3)

                            binding.temperature4.text = model.forecast.get(3).temperature + "°"
                            binding.humidity4.text = Html.fromHtml("Humidity: <b>" + model.forecast.get(3).humidity + "%</b>")
                            binding.windSpeed4.text = Html.fromHtml("Wind: <b>" + model.forecast.get(3).windSpeed + " miles/hour</b>")
                            Glide.with(this).load(model.forecast.get(3).weatherIconUrl).into(binding.weatherIcon4)

                            binding.temperature5.text = model.forecast.get(4).temperature + "°"
                            binding.humidity5.text = Html.fromHtml("Humidity: <b>" + model.forecast.get(4).humidity + "%</b>")
                            binding.windSpeed5.text = Html.fromHtml("Wind: <b>" + model.forecast.get(4).windSpeed + " miles/hour</b>")
                            Glide.with(this).load(model.forecast.get(4).weatherIconUrl).into(binding.weatherIcon5)
                        }
                    }
                }
            }
        }
        context?.let {
            lifecycleScope.launch {
                viewModel.getWeatherInfo(
                    it,
                    cityLocation.latitude.toString(),
                    cityLocation.longitude.toString()
                )
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).supportActionBar?.title = cityLocation.city_location
    }
}
