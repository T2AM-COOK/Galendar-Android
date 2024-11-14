package com.example.galendar

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.view.View
import android.widget.Button

class PickerFragment : Fragment(R.layout.fragment_picker) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // view.findViewById를 통해 버튼 참조
        val submitStartData: Button = view.findViewById(R.id.submit_start_data)
        val submitEndData: Button = view.findViewById(R.id.submit_end_data)
        val contestStartData: Button = view.findViewById(R.id.contest_start_data)
        val contestEndData: Button = view.findViewById(R.id.contest_end_data)

        // WindowInsets 설정
        ViewCompat.setOnApplyWindowInsetsListener(view.findViewById(R.id.picker_root)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}
