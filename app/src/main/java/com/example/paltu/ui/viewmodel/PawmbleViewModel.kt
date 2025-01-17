package com.example.paltu.ui.viewmodel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.paltu.data.model.ApplicationRequest
import com.example.paltu.data.model.ApplicationResponse
import com.example.paltu.data.model.PawmbleCard
import com.example.paltu.data.model.PawmbleCardDetail
import com.example.paltu.data.repository.PawmbleRepository
import kotlinx.coroutines.launch
import java.io.File

class PawmbleViewModel : ViewModel() {
    private val repository = PawmbleRepository()

    private val _pawmbleCards = MutableLiveData<List<PawmbleCard>>()
    val pawmbleCards: LiveData<List<PawmbleCard>> = _pawmbleCards

    private val _selectedAnimalDetail = MutableLiveData<PawmbleCardDetail?>()
    val selectedAnimalDetail: LiveData<PawmbleCardDetail?> = _selectedAnimalDetail

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: MutableLiveData<String?> = _error

    private val _applicationResponse = MutableLiveData<ApplicationResponse?>()
    val applicationResponse: LiveData<ApplicationResponse?> = _applicationResponse

    fun fetchPawmbleCards() {
        _isLoading.value = true
        viewModelScope.launch {
            val result = repository.getPawmbleCards()
            result.onSuccess { cards ->
                _pawmbleCards.value = cards
                _isLoading.value = false
            }.onFailure { exception ->
                _error.value = exception.message
                _isLoading.value = false
            }
        }
    }

    fun fetchAnimalDetail(tagId: Int) {
        Log.d("here","here")
        _isLoading.value = true
        viewModelScope.launch {
            val result = repository.getAnimalDetail(tagId)
            result.onSuccess { detail ->
                _selectedAnimalDetail.value = detail
                _isLoading.value = false
            }.onFailure { exception ->
                _error.value = exception.message
                _isLoading.value = false
                _selectedAnimalDetail.value = null
            }
        }
    }
    fun submitApplication(
        tagId: Int,
        name: String,
        contact: String,
        whatsapp: String,
        address: String,
        occupation: String,
        email: String,
        adopterImage: File,
        adopterDoc: File,
        incamp: String
    ) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val applicationRequest = ApplicationRequest(
                    tag_id = tagId,
                    name = name,
                    contact = contact,
                    whatsapp = whatsapp,
                    address = address,
                    occupation = occupation,
                    email = email,
                    adopterImage = adopterImage,
                    adopterDoc = adopterDoc,
                    incamp = incamp
                )

                Log.d("post","$applicationRequest")
                val result = repository.submitApplication(applicationRequest)
                result.onSuccess { response ->
                    _applicationResponse.value = response
                    _error.value = null
                }.onFailure { exception ->
                    _error.value = exception.message
                    _applicationResponse.value = null
                }
            } catch (e: Exception) {
                _error.value = e.message
                _applicationResponse.value = null
            } finally {
                _isLoading.value = false
            }
        }
    }
}