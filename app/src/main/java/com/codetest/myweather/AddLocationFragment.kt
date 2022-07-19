package com.codetest.myweather

import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.codetest.myweather.databinding.AddLocationFragmentBinding
import com.codetest.myweather.room.CityLocation
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng

class AddLocationFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnCameraIdleListener {

    private lateinit var binding: AddLocationFragmentBinding
    private lateinit var mapFragment: SupportMapFragment
    private lateinit var mGoogleMap: GoogleMap
    private lateinit var viewModel: AddLocationViewModel
    private var fusedLocationClient: FusedLocationProviderClient? = null
    private var latLng: LatLng = LatLng(32.7767, -96.7970)
    private var currentCityLocation: CityLocation? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = AddLocationFragmentBinding.inflate(layoutInflater)

        fusedLocationClient = context?.let { LocationServices.getFusedLocationProviderClient(it) }
        fusedLocationClient?.let { locationClient ->
            locationClient.lastLocation.addOnSuccessListener { location: Location? ->
                location?.let { latLng = LatLng(it.latitude, it.longitude) }
            }
        }

        viewModel = ViewModelProvider.NewInstanceFactory().create(AddLocationViewModel::class.java)
        viewModel.repository = (activity?.application as MyWeatherApplication).repository
        viewModel.getLocationInformation.observe(viewLifecycleOwner) {
            it?.let {
                when (it.status) {
                    ResponseStatus.ERROR -> {
                        binding.addressEditText.setText("Location not found!")
                        currentCityLocation = null
                    }
                    ResponseStatus.LOADING -> {
                        binding.addressEditText.setText("Searching...")
                        currentCityLocation = null
                    }
                    ResponseStatus.SUCCESS -> {
                        it.data?.let { model ->
                            binding.addressEditText.setText(model.locationAddress)
                            currentCityLocation = CityLocation(
                                model.locationAddress,
                                latLng.latitude,
                                latLng.longitude
                            )
                        }
                    }
                }
            }
        }

        binding.addButton.setOnClickListener {
            currentCityLocation?.let {
                viewModel.insert(it)
                Toast.makeText(context, getString(R.string.location_added, it.city_location), Toast.LENGTH_SHORT).show()
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mGoogleMap = googleMap
        mGoogleMap.clear()
        mGoogleMap.setOnCameraIdleListener(this)
        animateCamera()
    }

    override fun onCameraIdle() {
        latLng = mGoogleMap.cameraPosition.target
        context?.let {
            viewModel.getLocationInfo(
                it,
                latLng.latitude.toString(),
                latLng.longitude.toString()
            )
        }
    }

    private fun animateCamera() {
        mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10F))
    }
}
