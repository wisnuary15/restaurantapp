package com.example.restaurantapp.ui.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.restaurantapp.R
import com.example.restaurantapp.data.model.Restaurant

class RestaurantAdapter(
    private var restaurantList: List<Restaurant>,
    private val onItemClicked: (Restaurant) -> Unit
) : RecyclerView.Adapter<RestaurantAdapter.RestaurantViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_restaurant, parent, false)
        return RestaurantViewHolder(view)
    }

    override fun onBindViewHolder(holder: RestaurantViewHolder, position: Int) {
        val restaurant = restaurantList[position]
        holder.tvRestaurantName.text = restaurant.name
        holder.tvRestaurantDescription.text = restaurant.description
        holder.ratingBar.rating = restaurant.rating.toFloat()
        holder.tvCity.text = restaurant.city

        Glide.with(holder.itemView.context)
            .load(restaurant.pictureId)
            .into(holder.ivRestaurantImage)

        holder.itemView.setOnClickListener {
            onItemClicked(restaurant)
        }
    }

    override fun getItemCount(): Int = restaurantList.size

    fun updateData(newRestaurantList: List<Restaurant>) {
        restaurantList = newRestaurantList
        notifyDataSetChanged()
    }

    class RestaurantViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvRestaurantName: TextView = itemView.findViewById(R.id.tvRestaurantName)
        val tvRestaurantDescription: TextView = itemView.findViewById(R.id.tvRestaurantDescription)
        val ratingBar: RatingBar = itemView.findViewById(R.id.ratingBar)
        val tvCity: TextView = itemView.findViewById(R.id.tvCity)
        val ivRestaurantImage: ImageView = itemView.findViewById(R.id.ivRestaurantImage)
    }
}
