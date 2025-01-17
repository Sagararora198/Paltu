package com.example.paltu.ui.adaptor

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.paltu.R
import com.example.paltu.data.model.PawmbleCard

class CardAdapter(
    private var cardList: List<PawmbleCard>,
    private val onEmptyStateChanged: (Boolean) -> Unit
) : RecyclerView.Adapter<CardAdapter.CardViewHolder>() {

    // OnItemClickListener for handling swipe or click events
    private var onItemClickListener: ((PawmbleCard) -> Unit)? = null

    class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.cardImage)
        val title: TextView = itemView.findViewById(R.id.cardTitle)
        val description: TextView = itemView.findViewById(R.id.cardDescription)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.pawmble_card, parent, false)
        return CardViewHolder(view)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val card = cardList[position]

        // Load image using Glide for network images
        Glide.with(holder.image.context)
            .load(card.photos)
            .placeholder(R.drawable.ic_launcher_foreground) // Placeholder image
            .error(R.drawable.ic_launcher_foreground) // Error image
            .into(holder.image)

        // Set title as animal type
        holder.title.text = "Tag id:${card.tag_id}"

        // Set description as caretaker information
        holder.description.text = "animal type: ${card.animal_type}"

        // Optional: Set click listener
        holder.itemView.setOnClickListener {
            onItemClickListener?.invoke(card)
        }
    }

    override fun getItemCount() = cardList.size

    // Method to update the entire list
    fun updateData(newCardList: List<PawmbleCard>) {
        cardList = newCardList
        notifyDataSetChanged()
    }

    // Method to remove an item
    fun removeItem(position: Int) {
        val mutableList = cardList.toMutableList()
        mutableList.removeAt(position)
        cardList = mutableList
        onEmptyStateChanged(cardList.isEmpty())
        Log.d("remoded","$cardList")
        notifyItemRemoved(position)
    }

    // Method to get item at a specific position
    fun getItemAtPosition(position: Int): PawmbleCard {
        return cardList[position]
    }

    // Set click listener
    fun setOnItemClickListener(listener: (PawmbleCard) -> Unit) {
        onItemClickListener = listener
    }
}