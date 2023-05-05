package sju.sejong.capstonedesign.view.board

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import sju.sejong.capstonedesign.R
import sju.sejong.capstonedesign.databinding.FragmentTipBoardBinding
import sju.sejong.capstonedesign.model.board.ContentList
import sju.sejong.capstonedesign.repository.BoardRepository
import sju.sejong.capstonedesign.util.Constants.LOGIN_STATUS
import sju.sejong.capstonedesign.util.SeggeredGridSpaceItemDecoration
import sju.sejong.capstonedesign.viewmodel.BoardViewModel
import sju.sejong.capstonedesign.viewmodel.factory.BoardViewModelFactory
import kotlinx.android.synthetic.main.dialog_login.*
import sju.sejong.capstonedesign.adapter.BoardPostAdapter

class TipBoardFragment: Fragment() {
    private var _binding: FragmentTipBoardBinding? = null
    private val binding get() = _binding!!
    private lateinit var tipBoardPostAdapter: BoardPostAdapter
    private lateinit var viewModel: BoardViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentTipBoardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val repository = BoardRepository()
        val factory = BoardViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[BoardViewModel::class.java]

        initDataSettings()
        setObserver()
    }

    private fun setObserver() {
        viewModel.PostListResponse.observe(viewLifecycleOwner) {
            setRvPost(it.content)
            binding.pbTipBoard.visibility = View.GONE
        }
    }

    private fun initDataSettings() {
        viewModel.getAllPost("KNOWHOW")
    }

    private fun setRvPost(tipBoardList: List<ContentList>) {
        tipBoardPostAdapter =
            BoardPostAdapter(
                requireContext(),
                tipBoardList
            )

        binding.rvTipPost.apply {
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            adapter = tipBoardPostAdapter
            addItemDecoration(SeggeredGridSpaceItemDecoration(requireContext(),2))
        }

        tipBoardPostAdapter.setItemClickListener(object :BoardPostAdapter.OnItemClickListener {
            override fun onClick(v: View, position: Int) {
                if (LOGIN_STATUS) {
                    val action = BoardFragmentDirections.actionFragmentBoardToFragmentPostDetail(tipBoardList[position].boardId)
                    findNavController().navigate(action)
                } else {
                    setLoginDialog()
                }
            }
        })
    }

    private fun setLoginDialog() {
        val loginDialog = Dialog(requireContext())

        loginDialog.setContentView(R.layout.dialog_login)
        loginDialog.window!!.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT)
        loginDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        loginDialog.setCanceledOnTouchOutside(false)
        loginDialog.show()

        loginDialog.btn_dialog_login.setOnClickListener {
            loginDialog.dismiss()

            val action = BoardFragmentDirections.actionFragmentBoardToFragmentLogin()
            findNavController().navigate(action)
        }

        loginDialog.btn_dialog_login_close.setOnClickListener {
            loginDialog.dismiss()
        }
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}