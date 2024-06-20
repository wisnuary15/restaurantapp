package com.example.restaurantapp.ui.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.restaurantapp.R
import com.example.restaurantapp.data.model.CustomerReview

class ReviewAdapter(
    private var reviewList: List<CustomerReview>,
    private val activity: FragmentActivity,
    private val restaurantId: String,
    private val onEditClicked: (String, String, String) -> Unit,
    private val onDeleteClicked: (String, String) -> Unit
) : RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_review, parent, false)
        return ReviewViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        val review = reviewList[position]
        holder.tvReviewerName.text = review.name
        holder.tvReviewText.text = review.review
        holder.tvReviewDate.text = review.date

        holder.ivOptions.setOnClickListener {
            val reviewId = review.review_id // Ambil reviewId dari objek review
            if (reviewId.isNotEmpty()) { // Pastikan reviewId tidak null atau kosong
                val fragment = ReviewOptionsFragment(
                    restaurantId = restaurantId,
                    review_id = reviewId,
                    onEditClicked = { restId, revId -> onEditClicked(restId, revId, review.review) },
                    onDeleteClicked = { restId, revId -> onDeleteClicked(restId, revId) }
                )
                fragment.show(activity.supportFragmentManager, "ReviewOptionsFragment")
            }
        }
    }

    override fun getItemCount(): Int = reviewList.size

    fun updateData(newReviewList: List<CustomerReview>) {
        reviewList = newReviewList
        notifyDataSetChanged()
    }

    fun updateReviewText(review_id: String, updatedText: String) {
        val index = reviewList.indexOfFirst { it.review_id == review_id }
        if (index != -1) {
            val updatedReview = reviewList[index].copy(review = updatedText)
            reviewList = reviewList.toMutableList().apply {
                set(index, updatedReview)
            }
            notifyItemChanged(index)
        }
    }

    class ReviewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvReviewerName: TextView = itemView.findViewById(R.id.tvReviewerName)
        val tvReviewText: TextView = itemView.findViewById(R.id.tvReviewText)
        val tvReviewDate: TextView = itemView.findViewById(R.id.tvReviewDate)
        val ivOptions: ImageView = itemView.findViewById(R.id.ivOptions)
    }
}
