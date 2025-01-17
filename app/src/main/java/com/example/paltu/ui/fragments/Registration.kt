import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.paltu.R
import com.example.paltu.ui.Home.RegistrationSuccessActivity
import com.example.paltu.ui.viewmodel.PawmbleViewModel
import java.io.File

class Registration : Fragment() {

    private val PICK_PROFILE_IMAGE = 1
    private val PICK_PET_IMAGE = 2
    private lateinit var profilePicturePreview: ImageView
    private lateinit var petPicturePreview: ImageView
    private var profileImageUri: Uri? = null
    private var petImageUri: Uri? = null
    private var tagId = -1
    private var isInCamp = false
    private lateinit var viewModel: PawmbleViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            tagId = it.getInt("tagId", -1)
        }
        Log.d("registration",tagId.toString())
        viewModel = ViewModelProvider(this)[PawmbleViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_registration, container, false)

        val nameInput = view.findViewById<EditText>(R.id.nameInput)
        val contactInput = view.findViewById<EditText>(R.id.contactInput)
        val whatsappInput = view.findViewById<EditText>(R.id.whatsappInput)
        val addressInput = view.findViewById<EditText>(R.id.addressInput)
        val occupationInput = view.findViewById<EditText>(R.id.occupationInput)
        val email = view.findViewById<EditText>(R.id.useremail)
        val incamp = view.findViewById<RadioGroup>(R.id.incamp)
        val submitButton = view.findViewById<Button>(R.id.submitButton)
        val uploadProfilePictureButton = view.findViewById<Button>(R.id.uploadProfilePictureButton)
        val uploadPetPictureButton = view.findViewById<Button>(R.id.uploadPetPictureButton)

        profilePicturePreview = view.findViewById(R.id.profilePicturePreview)
        petPicturePreview = view.findViewById(R.id.petPicturePreview)

        // RadioGroup Listener
        incamp.setOnCheckedChangeListener { _, checkedId ->
            isInCamp = when (checkedId) {
                R.id.trueOption -> true
                R.id.falseOption -> false
                else -> false
            }
        }

        uploadProfilePictureButton.setOnClickListener {
            pickImage(PICK_PROFILE_IMAGE)
        }

        uploadPetPictureButton.setOnClickListener {
            pickImage(PICK_PET_IMAGE)
        }

        submitButton.setOnClickListener {
            if (validateInputs(
                    nameInput.text.toString(),
                    contactInput.text.toString(),
                    whatsappInput.text.toString(),
                    addressInput.text.toString(),
                    occupationInput.text.toString(),
                    email.text.toString()
                )
            ) {
                submitForm(
                    nameInput.text.toString(),
                    contactInput.text.toString(),
                    whatsappInput.text.toString(),
                    addressInput.text.toString(),
                    occupationInput.text.toString(),
                    email.text.toString(),
                    isInCamp
                )
            }
        }

        return view
    }

    private fun pickImage(requestCode: Int) {
        val intent = Intent(Intent.ACTION_PICK).apply {
            type = "image/*"
        }
        startActivityForResult(intent, requestCode)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && data != null) {
            val selectedImageUri: Uri? = data.data
            when (requestCode) {
                PICK_PROFILE_IMAGE -> {
                    profileImageUri = selectedImageUri
                    profilePicturePreview.setImageURI(selectedImageUri)
                }
                PICK_PET_IMAGE -> {
                    petImageUri = selectedImageUri
                    petPicturePreview.setImageURI(selectedImageUri)
                }
            }
        }
    }

    private fun validateInputs(
        name: String,
        contact: String,
        whatsapp: String,
        address: String,
        occupation: String,
        petHistory: String,
    ): Boolean {
        if (name.isEmpty() || contact.isEmpty() || whatsapp.isEmpty() ||
            address.isEmpty() || occupation.isEmpty() || petHistory.isEmpty()
        ) {
            Toast.makeText(requireContext(), "All fields are required", Toast.LENGTH_SHORT).show()
            return false
        }
        if (!contact.matches("\\d{10}".toRegex())) {
            Toast.makeText(requireContext(), "Invalid contact number", Toast.LENGTH_SHORT).show()
            return false
        }
        if (!whatsapp.matches("\\d{10}".toRegex())) {
            Toast.makeText(requireContext(), "Invalid WhatsApp number", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun submitForm(
        name: String,
        contact: String,
        whatsapp: String,
        address: String,
        occupation: String,
        email: String,
        isInCamp: Boolean
    ) {
        viewModel.submitApplication(tagId,name,contact,whatsapp,address,occupation,email,getFileFromUri(profileImageUri),getFileFromUri(petImageUri),isInCamp.toString() )

        viewModel.applicationResponse.observe(viewLifecycleOwner) { response ->
            response?.let {
                // Launch success activity with the registration ID
                RegistrationSuccessActivity.start(
                    requireContext(),
                    it.application_id.toString(), // Assuming your response contains registrationId
                    "Registration Successful!" // You can customize this message
                )
                // Optional: Close the current fragment/activity
                activity?.finish()
            }
        }
    }
    private fun getFileFromUri( uri: Uri?): File {
            val inputStream = uri?.let { context?.contentResolver?.openInputStream(it) }
            val file = File(context?.cacheDir, "temp_file_${System.currentTimeMillis()}")
            inputStream?.use { input ->
                file.outputStream().use { output ->
                    input.copyTo(output)
                }
            }
            return file
        }

}
