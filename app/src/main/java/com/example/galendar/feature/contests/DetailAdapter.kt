//
//package com.example.galendar.feature.contests
//
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.recyclerview.widget.RecyclerView
//import com.bumptech.glide.Glide
//import com.example.galendar.databinding.FragmentDetailContestBinding
//import com.example.galendar.remote.DetailData
//
//// 디테일 페이지에서 컨테스트 세부 정보를 리스트로 표시하는 어댑터
//class DetailAdapter(
//    private var contests: MutableList<DetailData> = mutableListOf()
//) : RecyclerView.Adapter<DetailAdapter.ViewHolder>() {
//
//    interface DetailClickListener {
//        fun onDetailClick(contestId: Int)
//    }
//
//    private var detailClickListener: DetailClickListener? = null
//
//    class ViewHolder(private val binding: FragmentDetailContestBinding) : RecyclerView.ViewHolder(binding.root) {
//        fun bind(contest: DetailData, listener: DetailClickListener?) {
//            binding.title.text = contest.title
//            binding.content.text = contest.content
//            binding.submitDate.text = "${contest.submitStartDate} ~ ${contest.submitEndDate}"
//            binding.contestDate.text = "${contest.contestStartDate} ~ ${contest.contestEndDate}"
//            binding.target.text = contest.targets.joinToString(", ") { it.name }
//            binding.region.text = contest.regions.joinToString(", ") { it.name }
//
//            Glide.with(itemView.context)
//                .load(contest.imgLink)
//                .into(binding.img)
//
//            binding.img.setOnClickListener {
//                listener?.onDetailClick(contest.id)
//            }
//        }
//    }
//
//    fun setOnDetailClickListener(listener: DetailClickListener) {
//        detailClickListener = listener
//    }
//
//    fun updateData(newContests: List<DetailData>) {
//        contests.clear()
//        contests.addAll(newContests)
//        notifyDataSetChanged()
//    }
//
//    override fun getItemCount(): Int = contests.size
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        val binding = FragmentDetailContestBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//        return ViewHolder(binding)
//    }
//
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        holder.bind(contests[position], detailClickListener)
//    }
//}
