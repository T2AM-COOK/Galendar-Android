package com.example.galendar.feature.contests

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.galendar.remote.ContestData
import com.example.galendar.R
import com.example.galendar.databinding.ItemContestBinding


class ContestAdapter(
    private var contests: MutableList<ContestData> = mutableListOf()
) : RecyclerView.Adapter<ContestAdapter.ViewHolder>() {

    class ViewHolder(private val binding: ItemContestBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(contest: ContestData) {
            binding.tvTitle.text = contest.title
            binding.tvSubmitEndDate.text = "마감일: ${contest.submitEndDate }"
            binding.tvContestDate.text = "대회날짜: ${contest.contestStartDate ?: "정보 없음"} ~ ${contest.contestEndDate ?: "정보 없음"}"

            Glide.with(itemView.context)
                .load(contest.imgLink)
                .into(binding.ivContestImage)
        }
    }

    fun updateData(newContests: List<ContestData>) {
        contests.clear()
        contests.addAll(newContests)
        notifyDataSetChanged()
    }

    fun addData(newContests: List<ContestData>) {
        contests.addAll(newContests)
        notifyDataSetChanged()
    }

    fun clearData() {
        contests.clear()
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = contests.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding=ItemContestBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(contests[position])
    }
}

