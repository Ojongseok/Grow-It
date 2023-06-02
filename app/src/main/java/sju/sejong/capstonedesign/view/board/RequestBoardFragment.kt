package sju.sejong.capstonedesign.view.board

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import sju.sejong.capstonedesign.databinding.FragmentRequestBoardBinding
import sju.sejong.capstonedesign.model.board.ContentList
import sju.sejong.capstonedesign.util.Constants.LOGIN_STATUS
import sju.sejong.capstonedesign.util.SeggeredGridSpaceItemDecoration
import sju.sejong.capstonedesign.viewmodel.BoardViewModel
import sju.sejong.capstonedesign.adapter.BoardPostAdapter
import sju.sejong.capstonedesign.databinding.DialogLoginBinding
import java.lang.Exception

@AndroidEntryPoint
class RequestBoardFragment: Fragment() {
    private var _binding: FragmentRequestBoardBinding? = null
    private val binding get() = _binding!!
    private lateinit var requestBoardPostAdapter: BoardPostAdapter
    private val  viewModel by viewModels<BoardViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentRequestBoardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initDataSettings()
        setObserver()
    }

    private fun setObserver() {
        viewModel.PostListResponse.observe(viewLifecycleOwner) {
            setRvPost(it.content)
            binding.pbRequestBoard.visibility = View.GONE
        }
    }

    private fun initDataSettings() {
        try {
            viewModel.getAllPost("QUESTION")
        } catch (e: Exception) {
            binding.tvBoardError.visibility = View.VISIBLE
        }
    }

    private fun setRvPost(requestBoardList: List<ContentList>) {
        requestBoardPostAdapter =
            BoardPostAdapter(
                requireContext(),
                requestBoardList
            )

        binding.rvRequestPost.apply {
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            adapter = requestBoardPostAdapter
            addItemDecoration(SeggeredGridSpaceItemDecoration(requireContext(),2))
        }

        requestBoardPostAdapter.setItemClickListener(object : BoardPostAdapter.OnItemClickListener {
            override fun onClick(v: View, position: Int) {
                if (LOGIN_STATUS) {
                    val action = BoardFragmentDirections.actionFragmentBoardToFragmentPostDetail(requestBoardList[position].boardId)
                    findNavController().navigate(action)
                } else {
                    setLoginDialog()
                }

            }
        })
    }

    private fun setLoginDialog() {
        val loginDialog = Dialog(requireContext())
        val binding = DialogLoginBinding.inflate(LayoutInflater.from(requireContext()))

        loginDialog.setContentView(binding.root)
        loginDialog.window!!.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT)
        loginDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        loginDialog.setCanceledOnTouchOutside(false)
        loginDialog.show()

        binding.btnDialogLogin.setOnClickListener {
            loginDialog.dismiss()

            val action = BoardFragmentDirections.actionFragmentBoardToFragmentLogin()
            findNavController().navigate(action)
        }

        binding.btnDialogLoginClose.setOnClickListener {
            loginDialog.dismiss()
        }
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}