package com.example.galendar.feature.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.view.View
import com.example.galendar.R

class BookmarkFragment : Fragment(R.layout.fragment_bookmark) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // WindowInsets 설정
        ViewCompat.setOnApplyWindowInsetsListener(view.findViewById(R.id.bookmark_root)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}
