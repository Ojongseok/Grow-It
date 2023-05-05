package sju.sejong.capstonedesign.view.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import sju.sejong.capstonedesign.R
import sju.sejong.capstonedesign.adapter.DiseaseGeneratedMonthlyAdapter
import sju.sejong.capstonedesign.adapter.RegionGeneratedAdapter
import sju.sejong.capstonedesign.databinding.FragmentHomeBinding
import sju.sejong.capstonedesign.repository.LoginRepository
import sju.sejong.capstonedesign.util.Constants.ACCESS_TOKEN
import sju.sejong.capstonedesign.util.Constants.LOGIN_STATUS
import sju.sejong.capstonedesign.util.Constants.MEMBER_ID
import sju.sejong.capstonedesign.util.GridSpaceItemDecoration
import sju.sejong.capstonedesign.viewmodel.LoginViewModel
import sju.sejong.capstonedesign.viewmodel.factory.LoginViewModelFactory
import sju.sejong.capstonedesign.viewmodel.OpenApiViewModel

class HomeFragment: Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var diseaseGeneratedMonthlyAdapter1: DiseaseGeneratedMonthlyAdapter
    private lateinit var diseaseGeneratedMonthlyAdapter2: DiseaseGeneratedMonthlyAdapter
    private lateinit var diseaseGeneratedMonthlyAdapter3: DiseaseGeneratedMonthlyAdapter
    private lateinit var diseaseGeneratedRegionAdapter: RegionGeneratedAdapter
    private val viewModel: OpenApiViewModel by viewModels()
    private lateinit var loginViewModel: LoginViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initDataSettings()
        setObserver()
        setRv()

        binding.btnHomeWebview.setOnClickListener {
            startActivity(Intent(requireContext(), HomeWebViewActivity::class.java))
        }
    }

    private fun setRv() {
        binding.rvHomeAlertMonth2.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
            adapter = diseaseGeneratedMonthlyAdapter2
        }
        diseaseGeneratedMonthlyAdapter2.setItemClickListener(object : DiseaseGeneratedMonthlyAdapter.OnItemClickListener {
            override fun onClick(v: View, position: Int) {
                val pair = diseaseGeneratedMonthlyAdapter2.getDiseaseName(position)
                viewModel.getSickKey(pair.first, pair.second)
            }
        })

        binding.rvHomeAlertMonth3.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
            adapter = diseaseGeneratedMonthlyAdapter3
        }
        diseaseGeneratedMonthlyAdapter3.setItemClickListener(object : DiseaseGeneratedMonthlyAdapter.OnItemClickListener {
            override fun onClick(v: View, position: Int) {
                val pair = diseaseGeneratedMonthlyAdapter3.getDiseaseName(position)
                viewModel.getSickKey(pair.first, pair.second)
            }
        })

        binding.rvHomeRegion.apply {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = diseaseGeneratedRegionAdapter
            addItemDecoration(GridSpaceItemDecoration(requireContext(), 2))
        }
        diseaseGeneratedRegionAdapter.setItemClickListener(object : RegionGeneratedAdapter.OnItemClickListener {
            override fun onClick(v: View, position: Int) {
                Toast.makeText(requireContext(), position.toString(), Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setObserver() {
        viewModel.diseaseGeneratedMonthly2.observe(viewLifecycleOwner) {
            diseaseGeneratedMonthlyAdapter2.setData(it)
        }

        viewModel.diseaseGeneratedMonthly3.observe(viewLifecycleOwner) {
            diseaseGeneratedMonthlyAdapter3.setData(it)
        }

        viewModel.singleSickKey.observe(viewLifecycleOwner) {
            if (it == "null") {
                Toast.makeText(requireContext(), "등록되지 않은 질병입니다.", Toast.LENGTH_SHORT).show()
            } else {
                val action = HomeFragmentDirections.actionFragmentHomeToFragmentDiseaseDetail(it!!)
                findNavController().navigate(action)
            }
        }

        viewModel.pbHome.observe(viewLifecycleOwner) {
            if (it) {
                binding.pbHomeAlertMonth.visibility = View.GONE
            }
        }
    }

    private fun initDataSettings() {
        val repository = LoginRepository(requireContext())
        val factory = LoginViewModelFactory(repository)
        loginViewModel = ViewModelProvider(this, factory)[LoginViewModel::class.java]

        binding.viewmodel = viewModel
        binding.lifecycleOwner = this

        viewModel.setDiseaseGeneratedMonthly()
        if (loginViewModel.getAccessToken().isEmpty()) {
            LOGIN_STATUS = false
            ACCESS_TOKEN = ""
            MEMBER_ID = 0
        } else {
            LOGIN_STATUS = true
            ACCESS_TOKEN = loginViewModel.getAccessToken()
            MEMBER_ID = loginViewModel.getMemberId()
        }
        Log.d("tag", loginViewModel.getAccessToken())

        diseaseGeneratedMonthlyAdapter2 = DiseaseGeneratedMonthlyAdapter(requireContext(), 2)
        diseaseGeneratedMonthlyAdapter3 = DiseaseGeneratedMonthlyAdapter(requireContext(), 3)
        diseaseGeneratedRegionAdapter = RegionGeneratedAdapter(requireContext())
    }
}