package com.example.capstonedesign.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.capstonedesign.databinding.ItemCommentBinding
import com.example.capstonedesign.databinding.ItemRequestPostBinding
import com.example.capstonedesign.model.board.AllCommentResult
import com.example.capstonedesign.model.board.ContentList
import com.example.capstonedesign.util.Constants.MEMBER_ID
import kotlinx.android.synthetic.main.item_comment.view.*

class CommentAdapter(private val context: Context) : RecyclerView.Adapter<CommentAdapter.CustomViewHolder>() {
    private lateinit var itemClickListener: OnItemClickListener
    private var commentsList = listOf<AllCommentResult>()

    inner class CustomViewHolder(private val binding: ItemCommentBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: AllCommentResult) {
            binding.tvItemCommentNickname.text = item.nickname
            binding.tvItemCommentContents.text = item.content
            binding.tvItemCommentDate.text = item.createdDate.removeRange(16,19)

            if (item.memberId == MEMBER_ID) {
                binding.ltItemCommentMenu.visibility = View.VISIBLE
            } else {
                binding.ltItemCommentMenu.visibility = View.GONE
            }
        }
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.bind(commentsList[position])

        holder.itemView.btn_item_comment_modify.setOnClickListener {
            itemClickListener.onClickModify(it, position)
        }
        holder.itemView.btn_item_comment_delete.setOnClickListener {
            itemClickListener.onClickDelete(it, position)
        }
        holder.itemView.btn_item_comment_re_comment.setOnClickListener {
            itemClickListener.onClickCommentRe(it, position)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val view = ItemCommentBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return CustomViewHolder(view)
    }

    interface OnItemClickListener {
        fun onClickDelete(v: View, position: Int)
        fun onClickModify(v: View, position: Int)
        fun onClickCommentRe(v: View, position: Int)
    }

    fun setItemClickListener(onItemClickListener: OnItemClickListener) {
        this.itemClickListener = onItemClickListener
    }

    fun setData(list: List<AllCommentResult>) {
        commentsList = list
        notifyDataSetChanged()
    }

    override fun getItemCount()= commentsList.size
}