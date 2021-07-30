package com.robusta.weatherphotocapture.presentation

import android.Manifest
import android.annotation.SuppressLint
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.robusta.weatherphotocapture.R
import com.robusta.weatherphotocapture.extensions.allPermissionsGranted
import com.robusta.weatherphotocapture.presentation.capture.KEY_CITY_NAME
import kotlinx.android.synthetic.main.main_fragment.view.*
import java.util.*

class MainFragment : Fragment() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var addresses: List<Address>

    companion object {
        private const val REQUEST_CODE_PERMISSIONS = 30
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        if (REQUIRED_PERMISSIONS.allPermissionsGranted(requireActivity())) {
            getLocationAdress()
        } else {
            ActivityCompat.requestPermissions(
                requireActivity(),
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.main_fragment, container, false)
        setLayout(view)
        return view
    }


    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (REQUIRED_PERMISSIONS.allPermissionsGranted(requireContext())) {
                getLocationAdress()
            } else {
                Toast.makeText(
                    requireContext(), "Permissions not granted by the user.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }


    @SuppressLint("MissingPermission")
    private fun getLocationAdress() {
        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            val long: Double = location?.longitude ?: 0.0
            val lat: Double = location?.latitude ?: 0.0
            val geoCoder = Geocoder(requireContext(), Locale.US)
            addresses = geoCoder.getFromLocation(lat, long, 1)
        }
    }

    private fun setLayout(view: View) {
        view.btn_go_camera.setOnClickListener {
            val bundle = Bundle()
            bundle.putString(KEY_CITY_NAME, addresses[0]?.countryName?:"Egypt")
            findNavController().navigate(R.id.dest_captureFragment, bundle)
        }

        view.btn_go_gallery.setOnClickListener {
            findNavController().navigate(R.id.dest_galleryFragment)
        }
    }


}