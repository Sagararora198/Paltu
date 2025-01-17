package com.example.paltu.ui.fragments

import android.graphics.Canvas
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.paltu.R
import com.example.paltu.ui.adaptor.CardAdapter
import com.example.paltu.ui.viewmodel.PawmbleViewModel
import kotlin.math.abs

class Pawmble : Fragment() {
    private lateinit var viewModel: PawmbleViewModel
    private lateinit var cardAdapter: CardAdapter
    private lateinit var recyclerView:RecyclerView
    private lateinit var emptyView:ViewGroup
    private lateinit var loader:ImageView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pawmble, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize ViewModel
        viewModel = ViewModelProvider(this)[PawmbleViewModel::class.java]

        // Setup RecyclerView
         recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        emptyView = view.findViewById(R.id.emptyStateView)
        loader = view.findViewById(R.id.loader)
        // Initialize adapter
        cardAdapter = CardAdapter(emptyList()) { isEmpty ->
            updateEmptyState(isEmpty)
        }
        recyclerView.adapter = cardAdapter

        // Observe ViewModel data
        viewModel.pawmbleCards.observe(viewLifecycleOwner) { cards ->
            cardAdapter.updateData(cards)
            updateEmptyState(cards.isEmpty())
        }

        // Observe errors
        viewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
        }
        context?.let {
            Glide.with(it)
                .load(R.drawable.paltu_design)
                .into(loader)
        }
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            loader.visibility = if (isLoading) View.VISIBLE else View.GONE
            recyclerView.visibility = if (isLoading) View.GONE else View.VISIBLE
        }
        // Fetch initial data
        viewModel.fetchPawmbleCards()

        // Setup item touch helper for swipe actions
        val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ) = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val card = cardAdapter.getItemAtPosition(position)
                Log.d("log"," $position")
                viewHolder.itemView.apply {
                    rotation = 0f
                    translationX = 0f
                    alpha = 1f
                }
                when (direction) {
                    ItemTouchHelper.LEFT -> {
                        // Remove item from adapter
                     cardAdapter.removeItem(position)
                    }
                    ItemTouchHelper.RIGHT -> {
                        // Fetch and navigate to animal details
                        Log.d("ID","$card")
                        Log.d("ID", card.tag_id.toString())

                        val bundle = Bundle().apply {
                            putInt("tagId", card.tag_id)
                        }
                        val animalDetailFragment = DetailAnimal().apply {
                            arguments = bundle
                        }

                        parentFragmentManager.beginTransaction()
                            .replace(R.id.fragment_container, animalDetailFragment)
                            .addToBackStack(null)
                            .commit()
                    }


                    }
                }



        })
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }
    private fun updateEmptyState(isEmpty: Boolean) {
        recyclerView.visibility = if (isEmpty) View.GONE else View.VISIBLE
        emptyView.visibility = if (isEmpty) View.VISIBLE else View.GONE
    }
}