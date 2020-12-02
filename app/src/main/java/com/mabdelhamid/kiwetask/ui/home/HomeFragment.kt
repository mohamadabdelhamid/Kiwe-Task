package com.mabdelhamid.kiwetask.ui.home

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.location.*
import com.mabdelhamid.kiwetask.R
import com.mabdelhamid.kiwetask.adapters.PagerAdapter
import com.mabdelhamid.kiwetask.data.models.Venue
import com.mabdelhamid.kiwetask.databinding.FragmentHomeBinding
import com.mabdelhamid.kiwetask.ui.activities.MainActivity
import com.mabdelhamid.kiwetask.utils.makeGone
import com.mabdelhamid.kiwetask.utils.makeVisible

interface HomeView {
    fun showLoading(shouldShow: Boolean)
    fun onSuccess(venues: List<Venue>?)
    fun onError()
}

class HomeFragment : Fragment(), HomeView {

    companion object {
        private const val FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION
        private const val COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION
        private const val RC_LOCATION_PERMISSION = 101
    }

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var presenter: HomePresenter

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private var currentLocation: Location? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
        createPresenter()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                super.onLocationResult(locationResult)
                fusedLocationClient.removeLocationUpdates(this)
                if (locationResult != null && locationResult.locations.isNotEmpty()) {
                    presenter.getVenues(locationResult.lastLocation)
                    currentLocation = locationResult.lastLocation
                }
            }
        }
        getCurrentLocation()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onPause() {
        super.onPause()
        stopLocationUpdates()
    }

    override fun onStop() {
        super.onStop()
        presenter.clear()
    }

    override fun showLoading(shouldShow: Boolean) {
        if (shouldShow) binding.progressBar.makeVisible()
        else binding.progressBar.makeGone()

    }

    override fun onSuccess(venues: List<Venue>?) {
        venues?.let {
            if (it.isNotEmpty()) {
                val pagerAdapter = PagerAdapter(childFragmentManager)
                pagerAdapter.apply {
                    addFragment(
                        VenuesFragment.newInstance(it),
                        getString(R.string.venues)
                    )
                    addFragment(
                        VenuesMapFragment.newInstance(it, currentLocation),
                        getString(R.string.map)
                    )
                }
                with(binding) {
                    vpHome.adapter = pagerAdapter
                    tabsHome.setupWithViewPager(vpHome)
                    tabsHome.makeVisible()
                }
            }
        }
    }

    override fun onError() = Toast.makeText(
        requireContext(),
        getString(R.string.something_went_wrong),
        Toast.LENGTH_SHORT
    ).show()

    private fun setListeners() {
        with(binding) {
            ivMenu.setOnClickListener { (requireActivity() as MainActivity).openDrawer() }
        }
    }

    private fun createPresenter() {
        presenter = HomePresenter(this)
    }

    private fun getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                requireContext(),
                COURSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(FINE_LOCATION, COURSE_LOCATION),
                RC_LOCATION_PERMISSION
            )
            return
        }

        showLoading(true)
        val locationRequest = LocationRequest()
        locationRequest.apply {
            interval = 10000
            fastestInterval = 3000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }

    private fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == RC_LOCATION_PERMISSION) {
            if (grantResults.isNotEmpty()) {
                for (element in grantResults) {
                    if (element != PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(
                            requireContext(),
                            getString(R.string.location_denied),
                            Toast.LENGTH_SHORT
                        )
                            .show()
                        break
                    } else getCurrentLocation()

                }
            }
        }
    }
}