package com.mabdelhamid.kiwetask.ui.home

import android.app.AlertDialog
import android.location.Location
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import com.bumptech.glide.Glide
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.mabdelhamid.kiwetask.utils.Constants.KEY_CURRENT_LOCATION
import com.mabdelhamid.kiwetask.utils.Constants.KEY_VENUES
import com.mabdelhamid.kiwetask.R
import com.mabdelhamid.kiwetask.data.models.Venue
import com.mabdelhamid.kiwetask.databinding.DialogVenueDetailsBinding
import com.mabdelhamid.kiwetask.databinding.FragmentVenuesMapBinding
import com.mabdelhamid.kiwetask.utils.makeGone
import com.mabdelhamid.kiwetask.utils.makeVisible

class VenuesMapFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    companion object {
        fun newInstance(venues: List<Venue>, currentLocation: Location?) =
            VenuesMapFragment().apply {
                arguments = bundleOf(KEY_VENUES to venues, KEY_CURRENT_LOCATION to currentLocation)
            }
    }

    private var _binding: FragmentVenuesMapBinding? = null
    private val binding get() = _binding!!

    private lateinit var map: GoogleMap
    private lateinit var venues: List<Venue>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVenuesMapBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initMap()
        arguments?.getParcelableArrayList<Venue>(KEY_VENUES)?.let {
            venues = it
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        map = googleMap!!
        map.setOnMarkerClickListener(this)
        showVenuesOnMap()
    }

    override fun onMarkerClick(marker: Marker?): Boolean {
        for (venue in venues) {
            if (venue.markerId == marker!!.id) {
                showVenueDetailsDialog(venue)
                break
            }
        }
        return true
    }

    private fun initMap() {
        val mapFragment: SupportMapFragment =
            childFragmentManager.findFragmentById(R.id.fragmentMap) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    private fun showVenuesOnMap() {
        map.clear()
        if (venues.isNotEmpty()) {
            for (venue in venues) {
                val latLong = LatLng(venue.location!!.lat!!, venue.location.lng!!)
                val marker = map.addMarker(
                    MarkerOptions().position(latLong).title(venue.name)
                )
                venue.markerId = marker.id
            }
            arguments?.getParcelable<Location>(KEY_CURRENT_LOCATION)?.let {
                moveCamera(LatLng(it.latitude, it.longitude))
            }
        }
    }

    private fun moveCamera(latLng: LatLng) = map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10f))

    private fun showVenueDetailsDialog(venue: Venue) {
        val dialogBinding = DialogVenueDetailsBinding.inflate(LayoutInflater.from(requireContext()))
        val dialogBuilder = AlertDialog.Builder(requireContext()).setView(dialogBinding.root)
        val dialog = dialogBuilder.show()
        with(dialogBinding) {
            ivClose.setOnClickListener { dialog.dismiss() }
            with(venue.name) {
                if (this.isNullOrBlank()) dialogBinding.tvName.makeGone()
                else {
                    dialogBinding.tvName.makeVisible()
                    dialogBinding.tvName.text = this
                }
            }

            with(venue.location?.address) {
                if (this.isNullOrBlank()) dialogBinding.tvAddress.makeGone()
                else {
                    dialogBinding.tvAddress.makeVisible()
                    dialogBinding.tvAddress.text = this
                }
            }

            venue.categories?.let {
                if (it.isNotEmpty()) {

                    Glide.with(binding.root.context)
                        .load("${it[0].icon!!.prefix}bg_32${it[0].icon!!.suffix}")
                        .into(dialogBinding.ivCategoryImage)

                    with(it[0].name) {
                        if (this.isNullOrBlank()) dialogBinding.tvCategoryName.makeGone()
                        else {
                            dialogBinding.tvCategoryName.makeVisible()
                            dialogBinding.tvCategoryName.text = this
                        }
                    }
                }
            }
        }
    }
}
