package com.example.restaurantapp.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.DialogFragment
import com.example.restaurantapp.R

class ReviewOptionsFragment(
    private val restaurantId: String,
    private val review_id: String,
    private val onEditClicked: (String, String) -> Unit,
    private val onDeleteClicked: (String, String) -> Unit
) : DialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_review_options, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.btn_ubah).setOnClickListener {
            onEditClicked(restaurantId, review_id)
            dismiss()
        }

        view.findViewById<Button>(R.id.btn_hapus).setOnClickListener {
            onDeleteClicked(restaurantId, review_id)
            dismiss()
        }
    }
}
