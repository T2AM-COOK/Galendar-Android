package com.example.galendar.feature.contests

import android.view.LayoutInflater
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.galendar.remote.ContestData
import com.example.galendar.R
import com.example.galendar.databinding.ItemContestBinding
import com.example.galendar.remote.BookmarkList

class ContestAdapter(
    private var items: MutableList<Any> = mutableListOf()  // ContestData나 BookmarkList를 모두 받을 수 있도록 Any 타입으로 변경
) : RecyclerView.Adapter<ContestAdapter.ViewHolder>() {

    private var onItemClickListener: ((Int) -> Unit)? = null
    private var onBookmarkClickListener: ((Int, Boolean) -> Unit)? = null
    private val bookmarkedItems = mutableSetOf<Int>()

    fun setOnBookmarkClickListener(listener: (Int, Boolean) -> Unit) {
        onBookmarkClickListener = listener
    }

    fun setOnItemClickListener(listener: (Int) -> Unit) {
        onItemClickListener = listener
    }

    class ViewHolder(private val binding: ItemContestBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(
            item: Any,  // ContestData 또는 BookmarkList를 받을 수 있음
            isBookmarked: Boolean,
            itemListener: ((Int) -> Unit)?,
            bookmarkListener: ((Int, Boolean) -> Unit)?
        ) {
            // item이 ContestData인지 BookmarkList인지 확인하고 적절히 처리
            val id = when (item) {
                is ContestData -> item.id
                is BookmarkList -> item.contestId
                else -> return
            }

            val title = when (item) {
                is ContestData -> item.title
                is BookmarkList -> item.title
                else -> return
            }

            val imgLink = when (item) {
                is ContestData -> item.imgLink
                is BookmarkList -> item.imgLink
                else -> return
            }

            val submitEndDate = when (item) {
                is ContestData -> item.submitEndDate
                is BookmarkList -> item.submitEndDate
                else -> return
            }

            val contestStartDate = when (item) {
                is ContestData -> item.contestStartDate
                is BookmarkList -> item.contestStartDate
                else -> return
            }

            val contestEndDate = when (item) {
                is ContestData -> item.contestEndDate
                is BookmarkList -> item.contestEndDate
                else -> return
            }

            binding.apply {
                tvTitle.text = title
                tvSubmitEndDate.text = "마감일: $submitEndDate"
                tvContestDate.text = "대회날짜: ${contestStartDate ?: "정보 없음"} ~ ${contestEndDate ?: "정보 없음"}"

                Glide.with(itemView.context)
                    .load(imgLink)
                    .into(ivContestImage)

                bookmarkHeart.isSelected = isBookmarked
                updateBookmarkIcon(isBookmarked)

                bookmarkHeart.setOnClickListener {
                    val newBookmarkState = !bookmarkHeart.isSelected
                    bookmarkHeart.isSelected = newBookmarkState
                    updateBookmarkIcon(newBookmarkState)
                    bookmarkListener?.invoke(id, newBookmarkState)
                }

                itemView.setOnClickListener {
                    itemListener?.invoke(id)
                }
            }
        }

        private fun updateBookmarkIcon(isBookmarked: Boolean) {
            binding.bookmarkHeart.setImageResource(
                if (isBookmarked) R.drawable.bookmarkheart_filled else R.drawable.bookmarkheart
            )
        }
    }

    fun updateData(newItems: List<Any>) {  // ContestData나 BookmarkList 리스트를 모두 받을 수 있음
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    fun addData(newItems: List<Any>) {
        val startPosition = items.size
        items.addAll(newItems)
        notifyItemRangeInserted(startPosition, newItems.size)
    }

    fun clearData() {
        items.clear()
        notifyDataSetChanged()
    }

    fun updateBookmarkStatus(contestId: Int, isBookmarked: Boolean) {
        if (isBookmarked) {
            bookmarkedItems.add(contestId)
        } else {
            bookmarkedItems.remove(contestId)
        }
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemContestBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        val contestId = when (item) {
            is ContestData -> item.id
            is BookmarkList -> item.contestId
            else -> return
        }
        val isBookmarked = bookmarkedItems.contains(contestId)

        holder.bind(
            item,
            isBookmarked,
            onItemClickListener,
            onBookmarkClickListener
        )
    }
}
