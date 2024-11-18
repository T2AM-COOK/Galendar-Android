package com.example.galendar

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import android.util.Log
import android.widget.Button

class ContestAdapter(private var contestList: List<ContestData>) : RecyclerView.Adapter<ContestAdapter.ContestViewHolder>() {

    class ContestViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivContestImage: ImageView = itemView.findViewById(R.id.ivContestImage)
        val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        val tvSubmitEndDate: TextView = itemView.findViewById(R.id.tvSubmitEndDate)
        val tvContestDates: TextView = itemView.findViewById(R.id.tvContestDate)
    }

    // 데이터 갱신 메서드
    fun updateData(newContestList: List<ContestData>) {
        if (contestList.size != newContestList.size || !contestList.containsAll(newContestList)) {
            contestList = newContestList
            notifyDataSetChanged()  // RecyclerView에 데이터가 변경되었음을 알림
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContestViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_contest, parent, false)
        return ContestViewHolder(view)
    }

    override fun onBindViewHolder(holder: ContestViewHolder, position: Int) {
        val contest = contestList[position]

        // 디버깅을 위한 로그 추가
        Log.d("ContestAdapter", "Binding contest: ${contest.title}, position: $position")

        holder.tvTitle.text = contest.title
        holder.tvSubmitEndDate.text = "제출 마감일: ${contest.submitEndDate}"
        holder.tvContestDates.text = "대회 기간: ${contest.contestStartDate} ~ ${contest.contestEndDate}"

        // 이미지 로드 (imgLink URL을 사용하여 Glide로 이미지 로딩)
        Glide.with(holder.itemView.context)
            .load(contest.imgLink) // imgLink에 있는 URL 사용
            .error(R.drawable.error) // 이미지 로딩 실패 시 기본 이미지
            .into(holder.ivContestImage)
    }

    override fun getItemCount(): Int = contestList.size
}
