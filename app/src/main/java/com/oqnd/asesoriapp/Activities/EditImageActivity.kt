package com.oqnd.asesoriapp.Activities

import android.app.Dialog
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.oqnd.asesoriapp.R

class EditImageActivity : AppCompatActivity() {

    private lateinit var upProfileImage : ImageView
    private lateinit var btnSelectImage : Button
    private lateinit var btnUpdateImage : Button
    private var imageUri : Uri ?= null

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_image)

        btnSelectImage = findViewById(R.id.btnLoad)
        btnUpdateImage = findViewById(R.id.btnUpdate)
        upProfileImage = findViewById(R.id.upProfileImage)
        progressBar = findViewById(R.id.pProgressBar)

        firebaseAuth = FirebaseAuth.getInstance()

        btnSelectImage.setOnClickListener {
            showDialog()
        }

        btnUpdateImage.setOnClickListener {
            validateImage()
        }
    }

    private fun validateImage(){
        if(imageUri == null){
            Toast.makeText(applicationContext, "¡Elige una imagen primero!", Toast.LENGTH_SHORT).show()
        }else{
            uploadImage()
        }
    }

    private fun uploadImage(){
        btnSelectImage.isVisible = false
        btnUpdateImage. isVisible = false
        progressBar.isVisible = true

        val imageRoute = "User_Profile/" + firebaseAuth.uid
        val referenceStorage = FirebaseStorage.getInstance().getReference(imageRoute)
        referenceStorage.putFile(imageUri!!).addOnSuccessListener { task ->
            val uriTask : Task<Uri> = task.storage.downloadUrl
            while (!uriTask.isSuccessful);
            val urlImage = "${uriTask.result}"

            updateImage(urlImage)

        }.addOnFailureListener{ e->
            Toast.makeText(applicationContext, e.message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateImage(urlImage: String) {
        val hashmap : HashMap<String, Any> = HashMap()
        if(imageUri != null){
            hashmap["image"] = urlImage
        }

        val reference = FirebaseDatabase.getInstance().getReference("Users")
        reference.child(firebaseAuth.uid!!).updateChildren(hashmap).addOnSuccessListener {
            btnSelectImage.isVisible = true
            btnUpdateImage. isVisible = true
            progressBar.isVisible = false

            Toast.makeText(applicationContext, "La foto de perfil ha sido actualizada.", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener { e->
            Toast.makeText(applicationContext, e.message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun openCamera(){
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "Title")
        values.put(MediaStore.Images.Media.DESCRIPTION, "Description")
        imageUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)

        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
        cameraActivityResultLauncher.launch(intent)
    }

    private val cameraActivityResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()){result ->
            if(result.resultCode == RESULT_OK){
                upProfileImage.setImageURI(imageUri)
            }else{
                Toast.makeText(applicationContext, "Proceso cancelado", Toast.LENGTH_SHORT).show()
            }
        }

    private fun openGallery(){
        val intent = Intent(Intent.ACTION_PICK)
        intent.type="image/*"
        galleryActivityResultLauncher.launch(intent)
    }

    private val galleryActivityResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
        ActivityResultCallback<ActivityResult> { result ->
            if (result.resultCode == RESULT_OK){
                val data = result.data
                imageUri = data!!.data
                upProfileImage.setImageURI(imageUri)
            }else{
                Toast.makeText(applicationContext, "Proceso cancelado", Toast.LENGTH_SHORT).show()
            }
        }
    )

    private fun showDialog(){
        val btnOpenGallery : Button
        val btnOpenCamera : Button

        val dialog = Dialog(this@EditImageActivity)

        dialog.setContentView(R.layout.dialog_select)

        btnOpenGallery = dialog.findViewById(R.id.btnOpenGallery)
        btnOpenCamera = dialog.findViewById(R.id.btnOpenCamera)

        btnOpenGallery.setOnClickListener {
            openGallery()
            dialog.dismiss()
        }

        btnOpenCamera.setOnClickListener {
            openCamera()
            dialog.dismiss()
        }

        dialog.show()
    }
}