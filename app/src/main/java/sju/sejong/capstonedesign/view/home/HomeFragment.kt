package sju.sejong.capstonedesign.view.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import sju.sejong.capstonedesign.R
import sju.sejong.capstonedesign.adapter.DiseaseGeneratedMonthlyAdapter
import sju.sejong.capstonedesign.adapter.PopularPostAdapter
import sju.sejong.capstonedesign.adapter.AllRegionGeneratedAdapter
import sju.sejong.capstonedesign.databinding.FragmentHomeBinding
import sju.sejong.capstonedesign.dialog.LoginDialog
import sju.sejong.capstonedesign.util.Constants.ACCESS_TOKEN
import sju.sejong.capstonedesign.util.Constants.LOGIN_STATUS
import sju.sejong.capstonedesign.util.Constants.MEMBER_ID
import sju.sejong.capstonedesign.util.GridSpaceItemDecoration
import sju.sejong.capstonedesign.view.board.BoardFragmentDirections
import sju.sejong.capstonedesign.viewmodel.BoardViewModel
import sju.sejong.capstonedesign.viewmodel.LoginViewModel
import sju.sejong.capstonedesign.viewmodel.OpenApiViewModel

@AndroidEntryPoint
class HomeFragment: Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var diseaseGeneratedMonthlyAdapter1: DiseaseGeneratedMonthlyAdapter
    private lateinit var diseaseGeneratedMonthlyAdapter2: DiseaseGeneratedMonthlyAdapter
    private lateinit var diseaseGeneratedMonthlyAdapter3: DiseaseGeneratedMonthlyAdapter
    private lateinit var diseaseGeneratedRegionAdapter: AllRegionGeneratedAdapter
    private lateinit var popularPostAdapter: PopularPostAdapter
    private val viewModel  by viewModels<OpenApiViewModel>()
    private val loginViewModel by viewModels<LoginViewModel>()
    private val boardViewModel by viewModels<BoardViewModel>()

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
        binding.rvHomeAlertMonth1.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
            adapter = diseaseGeneratedMonthlyAdapter1
        }
        diseaseGeneratedMonthlyAdapter1.setItemClickListener(object : DiseaseGeneratedMonthlyAdapter.OnItemClickListener {
            override fun onClick(v: View, position: Int) {
                val pair = diseaseGeneratedMonthlyAdapter1.getDiseaseName(position)
                viewModel.getSickKey(pair.first, pair.second)
            }
        })
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
        diseaseGeneratedRegionAdapter.setItemClickListener(object : AllRegionGeneratedAdapter.OnItemClickListener {
            override fun onClick(v: View, position: Int) {
                Toast.makeText(requireContext(), loginViewModel.allRegionDisease.value?.result?.get(position)?.diseaseCount.toString(), Toast.LENGTH_SHORT).show()
            }
        })

        binding.rvHomePopularPost.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
            adapter = popularPostAdapter
        }

        popularPostAdapter.setItemClickListener(object : PopularPostAdapter.OnItemClickListener{
            override fun onClick(v: View, position: Int) {
                val boardId = boardViewModel.popularPostResponse.value?.result!![position].boardId
                val loginDialog = LoginDialog(requireContext())

                if (LOGIN_STATUS) {
                    val action = HomeFragmentDirections.actionFragmentHomeToFragmentPostDetail(boardId)
                    findNavController().navigate(action)
                } else {
                    loginDialog.showLoginDialog()

                    loginDialog.setItemClickListener(object : LoginDialog.OnItemClickListener {
                        override fun onCompleteClick(v: View) {
                            val action = HomeFragmentDirections.actionFragmentHomeToFragmentLogin()
                            findNavController().navigate(action)
                        }
                    })
                }
            }
        })
    }

    private fun setObserver() {
        viewModel.diseaseGeneratedMonthly1.observe(viewLifecycleOwner) {
            diseaseGeneratedMonthlyAdapter1.setData(it)
        }

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
        boardViewModel.popularPostResponse.observe(viewLifecycleOwner) {
            popularPostAdapter.setData(it.result)
        }

        loginViewModel.allRegionDisease.observe(viewLifecycleOwner) {
            diseaseGeneratedRegionAdapter.setData(it.result)
        }
    }

    private fun initDataSettings() {
        binding.viewmodel = viewModel
        binding.lifecycleOwner = this

        boardViewModel.getPopularPost()
        viewModel.setDiseaseGeneratedMonthly()
        loginViewModel.getAllRegionDisease()

        if (loginViewModel.getAccessToken().isEmpty()) {
            LOGIN_STATUS = false
            ACCESS_TOKEN = ""
            MEMBER_ID = 0
        } else {
            LOGIN_STATUS = true
            ACCESS_TOKEN = loginViewModel.getAccessToken()
            MEMBER_ID = loginViewModel.getMemberId()
        }

        diseaseGeneratedMonthlyAdapter1 = DiseaseGeneratedMonthlyAdapter(requireContext(), 1)
        diseaseGeneratedMonthlyAdapter2 = DiseaseGeneratedMonthlyAdapter(requireContext(), 2)
        diseaseGeneratedMonthlyAdapter3 = DiseaseGeneratedMonthlyAdapter(requireContext(), 3)
        diseaseGeneratedRegionAdapter = AllRegionGeneratedAdapter(requireContext())
        popularPostAdapter = PopularPostAdapter(requireContext())
    }
}