package com.app.weatherapp.mvvm.ui.SettingsFragment

import android.location.Geocoder
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.app.weatherapp.R
import com.app.weatherapp.mvvm.data.model.Settings

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsFragment2 : Fragment(), GoogleMap.OnMapClickListener {

    private lateinit var mMap: GoogleMap
    private lateinit var geocoder: Geocoder

    private val callback = OnMapReadyCallback { googleMap ->
        this.mMap = googleMap
        mMap.setOnMapClickListener(this)
        val sydney = LatLng(31.037933, 31.037933)
        googleMap.addMarker(MarkerOptions().position(sydney).title("Marker in Mansoura City"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as? AppCompatActivity)?.supportActionBar?.title = "Map"
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
        geocoder = Geocoder(context)

    }

    override fun onMapClick(p0: LatLng?) {
        try {
            val addresses = geocoder.getFromLocation(p0!!.latitude, p0.longitude, 1)
            if (addresses.size > 0 || addresses != null) {
                val address = addresses.get(0)
                val stAddress: String = address.getAddressLine(0)
                mMap.addMarker(MarkerOptions().position(p0).title(stAddress))
                val latLonBundle = bundleOf("latLng" to p0)
                Settings.mapLat2 = p0.latitude.toString()
                Settings.mapLong2 = p0.longitude.toString()
                val st = stAddress.split(",")
                val sst = st.get(1)
                Settings.mapcity2 = sst
                findNavController().navigate(R.id.action_mapsFragment2_to_weatherFragment)
            } else {
                Toast.makeText(requireActivity(), "Invilde Address", Toast.LENGTH_LONG).show()
            }
        } catch (e: Exception) {
            Log.i("MAPMAP", "onMapClick: " + e.message)
        }

    }
}