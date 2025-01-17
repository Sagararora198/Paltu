package com.example.paltu.data.repository

import android.util.Log
import com.example.paltu.data.model.ApplicationRequest
import com.example.paltu.data.model.ApplicationResponse
import com.example.paltu.data.model.PawmbleCard
import com.example.paltu.data.model.PawmbleCardDetail
import com.example.paltu.data.network.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class PawmbleRepository {
    private val apiService = RetrofitClient.instance

    suspend fun getPawmbleCards(): Result<List<PawmbleCard>> {
        return try {
            withContext(Dispatchers.IO) {
                val response = apiService.getPawmbleCards()
                if (response.isSuccessful) {
                    val availableAnimal = response.body()?.filter { it.avaliable }
                    Log.d("response","${response.body()}")
                    Log.d("available", availableAnimal.toString())
                    Result.success(availableAnimal ?: emptyList())
                } else {
                    Result.failure(Exception("Failed to fetch Pawmble cards: ${response.code()}"))
                }
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getAnimalDetail(tagId: Int): Result<PawmbleCardDetail> {
        Log.d("here","here too")
        return try {
            withContext(Dispatchers.IO) {
                val response = apiService.getAnimalDetail(tagId)
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        Log.d("Detail", body.toString())
                        Result.success(body)
                    } else {
                        Result.failure(Exception("No animal detail found"))
                    }
                } else {
                    Result.failure(Exception("Failed to fetch animal detail: ${response.code()}"))
                }
            }
        } catch (e: Exception) {
            Log.d("here","here exc $e")
            Result.failure(e)
        }
    }
    suspend fun submitApplication(applicationRequest: ApplicationRequest): Result<ApplicationResponse> {
        return try {
            withContext(Dispatchers.IO) {
                // Prepare parts
                val tagIdPart = createPartFromString(applicationRequest.tag_id.toString())
                val namePart = createPartFromString(applicationRequest.name)
                val contactPart = createPartFromString(applicationRequest.contact)
                val whatsappPart = createPartFromString(applicationRequest.whatsapp)
                val addressPart = createPartFromString(applicationRequest.address)
                val occupationPart = createPartFromString(applicationRequest.occupation)
                val emailPart = createPartFromString(applicationRequest.email)
                val incampPart = createPartFromString(applicationRequest.incamp)
                val adopterImagePart = createFilePart("adopter_image", applicationRequest.adopterImage)
                val adopterDocPart = createFilePart("adopter_doc", applicationRequest.adopterDoc)

                // API call
                Log.d("in here","$tagIdPart $namePart $contactPart $whatsappPart $addressPart $occupationPart $emailPart $adopterImagePart $adopterDocPart $incampPart")
                val response = apiService.submitApplication(
                    tagIdPart,
                    namePart,
                    contactPart,
                    whatsappPart,
                    addressPart,
                    occupationPart,
                    emailPart,
                    adopterImagePart,
                    adopterDocPart,
                    incampPart
                )

                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        Log.d("Application", "Application submitted successfully: $body")
                        Result.success(body)
                    } else {
                        Log.e("Application", "Empty response body")
                        Result.failure(Exception("Empty response body"))
                    }
                } else {
                    Log.e("Application", "Failed: ${response.code()} - ${response.message()}")
                    Result.failure(Exception("Failed with code: ${response.code()}"))
                }
            }
        } catch (e: Exception) {
            Log.e("ApplicationError", "Error submitting application: $e")
            Result.failure(e)
        }
    }
    private fun createPartFromString(value: String): RequestBody =
        RequestBody.create("text/plain".toMediaTypeOrNull(), value)

    private fun createFilePart(key: String, file: File): MultipartBody.Part {
        val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData(key, file.name, requestFile)
    }

}