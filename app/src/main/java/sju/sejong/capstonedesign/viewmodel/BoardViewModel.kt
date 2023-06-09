package sju.sejong.capstonedesign.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import sju.sejong.capstonedesign.repository.BoardRepository
import sju.sejong.capstonedesign.util.Constants.MEMBER_ID
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import sju.sejong.capstonedesign.model.board.*
import javax.inject.Inject

@HiltViewModel
class BoardViewModel @Inject constructor(private val repository: BoardRepository): ViewModel() {
    val PostListResponse = MutableLiveData<AllPostResponse>()    // 게시글 전체 조회
    val postDetailResponse = MutableLiveData<PostDetailInfoResponse>()    // 게시글 상세 조회
    val writePostResultCode = MutableLiveData<Int>()    // 게시글 작성 결과
    val postUpdateResultCode = MutableLiveData<Int>()    // 게시글 수정 결과
    val postDeleteResultCode = MutableLiveData<Int>()    // 게시글 삭제 결과
    val getAllCommentsResponse = MutableLiveData<AllCommentResponse>()    // 댓글 전체 조회
    val writeCommentsResultCode = MutableLiveData<Int>()    // 댓글 작성 결과
    val popularPostResponse = MutableLiveData<PopularPostResponse>()

    // 게시글 작성
    fun writePost(jsonBody: RequestBody, file: MultipartBody.Part?) = viewModelScope.launch {
        val response = repository.writePost(jsonBody, file)
        writePostResultCode.postValue(response.body()?.code)
    }

    // 게시글 전체 조회
    fun getAllPost(tag: String) = viewModelScope.launch {
        val response = repository.getAllPost(tag)
        PostListResponse.postValue(response.body())
    }

    // 게시글 상세 조회
    fun getPostDetailInfo(boardId: Long) = viewModelScope.launch {
        val response = repository.getPostDetailInfo(boardId)
        postDetailResponse.postValue(response.body())
    }

    // 게시글 수정
//    fun updatePost(boardId: Long) = viewModelScope.launch {
//        val response = repository.updatePost(boardId)
//        postUpdateResultCode.postValue(response.body()?.code)
//    }

    // 게시글 삭제
    fun deletePost(boardId: Long) = viewModelScope.launch {
        val response = repository.deletePost(boardId)
        postDeleteResultCode.postValue(response.body()?.code)

        getPostDetailInfo(boardId)
    }

    // 게시글별 댓글 조회
    fun getAllComments(boardId: Long) = viewModelScope.launch {
        val response = repository.getAllComments(boardId)
        getAllCommentsResponse.postValue(response.body())
    }

    // 게시글 좋아요/취소
    fun postLike(boardId: Long) = viewModelScope.launch {
        if (postDetailResponse.value?.result?.likeMemberIds?.contains(MEMBER_ID)!!) {
            repository.postLikeCancel(boardId)
        } else {
            repository.postLike(boardId)
        }
        getPostDetailInfo(boardId)
        getAllComments(boardId)
    }

    // 댓글 작성
    fun writeComments(boardId: Long, content: String, parentId: Long? = null) = viewModelScope.launch {
        val response = repository.writeComments(boardId, content, parentId)
        writeCommentsResultCode.postValue(response.body()?.code)

        getPostDetailInfo(boardId)
        getAllComments(boardId)
    }

    // 댓글 수정
    fun modifyComment(boardId: Long, commentId: Long, content: String) = viewModelScope.launch {
        repository.modifyComment(boardId,commentId, content)

        getAllComments(boardId)
    }

    // 댓글 삭제
    fun deleteComment(boardId: Long, commentId: Long) = viewModelScope.launch {
        repository.deleteComment(boardId, commentId)

        getPostDetailInfo(boardId)
        getAllComments(boardId)
    }

    // 게시글 해결완료
    fun postSolve(boardId: Long, solveRequest: SolveRequest) = viewModelScope.launch {
        repository.postSolve(boardId, solveRequest)

        getPostDetailInfo(boardId)
    }

    // 인기 게시물 조회
    fun getPopularPost() = viewModelScope.launch {
        popularPostResponse.postValue(repository.getPopularPost().body())
    }
}