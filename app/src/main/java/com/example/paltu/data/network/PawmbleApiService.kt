package com.example.paltu.data.network


import com.example.paltu.data.model.ApplicationRequest
import com.example.paltu.data.model.ApplicationResponse
import com.example.paltu.data.model.PawmbleCard
import com.example.paltu.data.model.PawmbleCardDetail
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface PawmbleApiService {
    @GET("animals/animals")
    suspend fun getPawmbleCards(): Response<List<PawmbleCard>>

    @GET("animals/animals/{tagId}")
    suspend fun getAnimalDetail(@Path("tagId") tagId: Int): Response<PawmbleCardDetail>

    @Multipart
    @POST("applications/appli")
    suspend fun submitApplication(
        @Part("tag_id") tagId: RequestBody,
        @Part("name") name: RequestBody,
        @Part("contact") contact: RequestBody,
        @Part("whatsapp") whatsapp: RequestBody,
        @Part("address") address: RequestBody,
        @Part("occupation") occupation: RequestBody,
        @Part("email") email: RequestBody,
        @Part adopter_image: MultipartBody.Part,  // Remove quotes as it's not a form field
        @Part adopter_doc: MultipartBody.Part,    // Remove quotes as it's not a form field
        @Part("incamp") incamp: RequestBody
    ): Response<ApplicationResponse>

}