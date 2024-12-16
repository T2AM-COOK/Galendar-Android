package com.example.galendar.feature.contests

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.galendar.R
import com.example.galendar.databinding.ItemBookmarkBinding
import com.example.galendar.remote.BookmarkList

class CalendarAdapter : ListAdapter<BookmarkList, CalendarAdapter.CalendarViewHolder>(CalendarDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarViewHolder {
        val binding = ItemBookmarkBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return CalendarViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CalendarViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class CalendarViewHolder(
        private val binding: ItemBookmarkBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(bookmark: BookmarkList) {
            binding.apply {
                // 제목 설정
                title.text = bookmark.title

                // 접수기간 설정
                val submitPeriod = "접수기간  |  ${bookmark.submitStartDate} ~ ${bookmark.submitEndDate}"
                submitEndStart.text = submitPeriod

            }
        }
    }
}

class CalendarDiffCallback : DiffUtil.ItemCallback<BookmarkList>() {
    override fun areItemsTheSame(oldItem: BookmarkList, newItem: BookmarkList): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: BookmarkList, newItem: BookmarkList): Boolean {
        return oldItem == newItem
    }
}