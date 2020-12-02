package com.mabdelhamid.kiwetask.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.mabdelhamid.kiwetask.R
import com.mabdelhamid.kiwetask.databinding.FragmentTermsAndConditionsBinding
import java.io.InputStream

class TermsAndConditionsFragment : Fragment() {

    private var _binding: FragmentTermsAndConditionsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTermsAndConditionsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAppBar()
        try {
            val inputStream: InputStream = resources.openRawResource(R.raw.terms_and_conditions)
            val byteArray = ByteArray(inputStream.available())
            inputStream.read(byteArray)
            binding.tvTermsAndConditions.text = String(byteArray)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setAppBar() {
        with(binding.appBar) {
            ivBack.setOnClickListener { requireActivity().supportFragmentManager.popBackStack() }
            tvTitle.text = getString(R.string.terms_conditions)
        }
    }
}