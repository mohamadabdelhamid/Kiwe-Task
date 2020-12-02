package com.mabdelhamid.kiwetask.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import com.mabdelhamid.kiwetask.utils.Constants.KEY_VENUES
import com.mabdelhamid.kiwetask.adapters.VenuesAdapter
import com.mabdelhamid.kiwetask.data.models.Venue
import com.mabdelhamid.kiwetask.databinding.FragmentVenuesBinding

class VenuesFragment : Fragment() {

    companion object {
        fun newInstance(venues: List<Venue>) = VenuesFragment().apply {
            arguments = bundleOf(KEY_VENUES to venues)
        }
    }

    private var _binding: FragmentVenuesBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: VenuesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVenuesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecycler()
        arguments?.getParcelableArrayList<Venue>(KEY_VENUES)?.let {
            adapter.items = it
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initRecycler() {
        adapter = VenuesAdapter()
        binding.rvVenues.apply {
            adapter = this@VenuesFragment.adapter
            setHasFixedSize(true)
        }
    }
}