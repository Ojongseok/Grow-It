package com.example.capstonedesign.view.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.capstonedesign.adapter.CropDetailInfoAdapter
import com.example.capstonedesign.databinding.FragmentCropDetailInfoBinding
import com.example.capstonedesign.viewmodel.OpenApiViewModel

class CropDetailInfoFragment: Fragment() {
    private var _binding: FragmentCropDetailInfoBinding? = null
    private val binding get() = _binding!!
    private val args by navArgs<CropDetailInfoFragmentArgs>()
    private val viewModel: OpenApiViewModel by viewModels()
    private lateinit var cropDetailInfoAdapter: CropDetailInfoAdapter
    private var cropName = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentCropDetailInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initData()
        setObserver()
        setRv()

        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setObserver() {
        viewModel.cropDetailInfo.observe(viewLifecycleOwner) {
            binding.tvCropDetailName.text = cropName
            binding.tvCropDetailDisease.text = "병: ${it.totalCount.toString()}종"

            if (it.list?.item != null) {
                cropDetailInfoAdapter.setData(it.list.item)
            }
        }

        viewModel.pbCropDetailInfo.observe(viewLifecycleOwner) {
            if (it) {
                binding.pbCropDetailInfo.visibility = View.GONE
            }
        }
    }

    private fun setRv() {
        binding.rvCropDetailDisease.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
            adapter = cropDetailInfoAdapter
        }
        cropDetailInfoAdapter.setItemClickListener(object : CropDetailInfoAdapter.OnItemClickListener {
            override fun onClick(v: View, position: Int) {
                Toast.makeText(requireContext(), position.toString(),Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun initData() {
        cropName = args.cropName
        viewModel.searchDetailCropInfo(cropName)
        cropDetailInfoAdapter = CropDetailInfoAdapter(requireContext())
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}