package sju.sejong.capstonedesign.view.main.pesticide

import android.content.Context
import android.content.Intent
import android.graphics.Point
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import sju.sejong.capstonedesign.databinding.FragmentPesticideDialogBinding
import sju.sejong.capstonedesign.model.openapi.PesticideDetailResponse
import sju.sejong.capstonedesign.viewmodel.OpenApiViewModel

class PesticideDialogFragment: DialogFragment() {
    private var _binding: FragmentPesticideDialogBinding? = null
    private val binding get() = _binding!!
    private val viewModel: OpenApiViewModel by viewModels()
    private val args by navArgs<PesticideDialogFragmentArgs>()
    private lateinit var size: Point

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentPesticideDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initDataSettings()
        setRecyclerView()
        setObserver()

        binding.btnPestiDalogClose.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.button.setOnClickListener {
            startActivity(Intent(requireActivity(), MapActivity::class.java))
        }
    }

    private fun setObserver() {
        viewModel.pesticideDetailInfoResult.observe(viewLifecycleOwner) {
            setView(it)
        }
    }

    private fun setRecyclerView() {

    }

    private fun setView(item: PesticideDetailResponse) {
        binding.tvPesticideDialogTag.text = item.useName
        binding.tvPestiDialogName.text = item.pestiKorName
        binding.tvPestiDialogPestiKor.text = item.pestiBrandName
        binding.tvPestiDialogPestiEng.text = item.pestiEngName
        binding.tvPestiDialogInfo1.text = "회사명: ${item.compName}"
        binding.tvPestiDialogInfo2.text = "인축독성/어독성: ${item.toxicGubun}/${item.fishToxicGubun}"
        binding.tvPestiDialogInfo3.text = "주성분함량: ${item.regCpntQnty}"
        binding.tvPestiDialogInfo4.text = "사용시기: ${item.pestiUse}"
        binding.tvPestiDialogInfo5.text = "사용기한: ${item.useSuittime}"
        binding.tvPestiDialogInfo6.text = "사용횟수: ${item.useNum}"
    }

    private fun initDataSettings() {
        isCancelable = false
        viewModel.getPesticideDetailInfo(args.pestiCode,args.diseaseUseSeq)

        val windowManager = requireContext().getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = windowManager.defaultDisplay
        size = Point()
        display.getSize(size)
    }

    override fun onResume() {
        super.onResume()

        val params: ViewGroup.LayoutParams? = dialog?.window?.attributes
        val deviceWidth = size.x
        params?.width = (deviceWidth * 0.9).toInt()
        dialog?.window?.attributes = params as WindowManager.LayoutParams
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}