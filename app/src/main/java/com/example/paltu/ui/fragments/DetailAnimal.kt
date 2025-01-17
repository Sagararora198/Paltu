package com.example.paltu.ui.fragments

import Registration
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.paltu.R
import com.example.paltu.data.model.PawmbleCardDetail
import com.example.paltu.ui.viewmodel.PawmbleViewModel

class DetailAnimal : Fragment() {

    private lateinit var viewModel: PawmbleViewModel

    // View references
    private lateinit var animalImage: ImageView
    private lateinit var animalName: TextView
    private lateinit var contactText: TextView
    private lateinit var genderText: TextView
    private lateinit var fitnessText: TextView
    private lateinit var sterilisationIcon: ImageView
    private lateinit var vaccinatedIcon: ImageView
    private lateinit var buttonAdopt:Button
    private  var tag_id:Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_detail_animal, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize ViewModel
        viewModel = ViewModelProvider(requireActivity())[PawmbleViewModel::class.java]

        // Initialize views
        initializeViews(view)

        // Get tagId from arguments and fetch data
        arguments?.getInt("tagId")?.let { tagId ->
            tag_id = tagId
            viewModel.fetchAnimalDetail(tagId)
        }

        // Observe animal details
        viewModel.selectedAnimalDetail.observe(viewLifecycleOwner) { detail ->
            detail?.let { animal ->
                Log.d("animal","asdsa")
                updateUI(animal)
            }
        }
        buttonAdopt.setOnClickListener {
            val bundle = Bundle().apply {
                putInt("tagId", tag_id)
            }
            val registration = Registration().apply {
                arguments = bundle
            }
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, registration)
                .addToBackStack(null)
                .commit()
        }
    }

    private fun initializeViews(view: View) {
        animalImage = view.findViewById(R.id.animalDetailImage)
        animalName = view.findViewById(R.id.animaTagId)
        contactText = view.findViewById(R.id.animalDetailContact)
        genderText = view.findViewById(R.id.animalGender)
        fitnessText = view.findViewById(R.id.fitness)
        sterilisationIcon = view.findViewById(R.id.sterilisation)
        vaccinatedIcon = view.findViewById(R.id.vaccinated)
        buttonAdopt = view.findViewById(R.id.buttonAdopt)
    }

    private fun updateUI(animal: PawmbleCardDetail) {
        // Load image using Glide
        Glide.with(requireContext())
            .load(animal.photos)
            .into(animalImage)

        // Update text fields
        animalName.text = animal.tag_id.toString()
        contactText.text = animal.contact
        genderText.text = animal.gender
        fitnessText.text = animal.fitness

        // Update status icons
        updateStatusIcon(sterilisationIcon, animal.sterilisation)
        updateStatusIcon(vaccinatedIcon, animal.vaccination)
    }

    private fun updateStatusIcon(imageView: ImageView, isPositive: Boolean) {
        val iconResource = if (isPositive) {
            R.drawable.ic_tick
        } else {
            R.drawable.ic_cross
        }
        imageView.setImageResource(iconResource)
    }
}