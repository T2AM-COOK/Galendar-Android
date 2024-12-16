package com.example.galendar.feature.contests

import android.os.Bundle
import androidx.navigation.NavDirections
import com.example.galendar.R

class ContestListFragmentDirections private constructor() {
    companion object {
        fun actionSearchViewFragmentToDetailFragment(contestId: Int): NavDirections {
            return ActionContestListFragmentToContestDetailFragment(contestId)
        }
    }

    private data class ActionContestListFragmentToContestDetailFragment(
        val contestId: Int // String으로 변경
    ) : NavDirections {
        override val actionId: Int
            get() = R.id.action_searchViewFragment_to_detailFragment

        override val arguments: Bundle
            get() {
                val args = Bundle()
                args.putInt("contestId", contestId) // String으로 값 전달
                return args
            }
    }
}
