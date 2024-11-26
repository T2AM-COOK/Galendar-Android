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


class ContestAdapter(
    private var contests: MutableList<ContestData> = mutableListOf()
) : RecyclerView.Adapter<ContestAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val contestImage: ImageView = view.findViewById(R.id.ivContestImage)
        val contestTitle: TextView = view.findViewById(R.id.tvTitle)
        val contestEndDate: TextView = view.findViewById(R.id.tvSubmitEndDate)
        val contestDate: TextView = view.findViewById(R.id.tvContestDate)
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


    override fun getItemCount(): Int = contests.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_contest, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val contest = contests[position]

        holder.contestTitle.text = contest.title
        holder.contestEndDate.text = "마감일: ${contest.submitEndDate}"
        holder.contestDate.text = "대회날짜: ${contest.contestStartDate} ~ ${contest.contestEndDate}"

        Glide.with(holder.itemView.context)
            .load(contest. imgLink)
            .into(holder.contestImage)
    }
}
