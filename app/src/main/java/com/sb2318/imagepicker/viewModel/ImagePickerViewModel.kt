package com.sb2318.imagepicker.viewModel

import android.app.Application
import android.app.ProgressDialog
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.sb2318.imagepicker.MainActivity
import com.sb2318.imagepicker.model.Image
import java.util.UUID


class ImagePickerViewModel(private val application: Application):AndroidViewModel(application){

    private val _imageList= MutableLiveData<ArrayList<Image>>()
    val imageList:LiveData<ArrayList<Image>>
        get()= _imageList


    // get images from firebase

    fun getImages(){

        val fireStoreDb = FirebaseFirestore.getInstance()
        val collectionRef = fireStoreDb.collection("ImagePicker")

        collectionRef.get().addOnCompleteListener(OnCompleteListener { task->
            run {

                 if(task.isSuccessful) {
                     val tempList = arrayListOf<Image>()
                     for (snapshot in task.result) {

                         val imageUrl = snapshot.getString("imageUrl")
                         if (imageUrl != null) {
                             Log.d("ImageUrl",imageUrl)
                         }
                         val image = imageUrl?.let { Image(it) }

                         if (image != null) {
                             tempList.add(image)
                         }
                     }

                     Log.d("ImageUrl temp list ","${tempList.size}")
                     _imageList.value = tempList

                     Log.d("ImageUrl img list ","${_imageList?.value?.size}")
                 }

            }})
    }

    // upload images into firebase
     fun uploadImage(selectedImageUri: Uri, mainActivity: MainActivity) {

        if (selectedImageUri != null) {

            val progressDialog = ProgressDialog(mainActivity)
            progressDialog.setTitle("Uploading...")
            progressDialog.show()

            val storage = FirebaseStorage.getInstance();
            val storageReference = storage.reference;

            val uuid= UUID.randomUUID().toString()
            val childRef= "images/$uuid/image"

            val ref: StorageReference = storageReference
                .child(
                    childRef
                )

            ref.putFile(selectedImageUri)
                .addOnSuccessListener {


                    ref.downloadUrl.addOnSuccessListener(OnSuccessListener {

                        val db = FirebaseFirestore.getInstance()

                        val documentReference: CollectionReference =
                            db.collection("ImagePicker")

                        val image = Image(it.toString())

                        documentReference.add(image).addOnSuccessListener {
                            progressDialog.dismiss()

                            Toast
                                .makeText(
                                    application,
                                    "Image Uploaded!!",
                                    Toast.LENGTH_SHORT
                                )
                                .show()
                        }.addOnFailureListener {

                            progressDialog.dismiss()
                            Log.d("FirebaseException",it.localizedMessage)
                        }


                    })


                }
                .addOnFailureListener { e ->
                    progressDialog.dismiss()
                    Toast
                        .makeText(
                            application,
                            "Failed " + e.message,
                            Toast.LENGTH_SHORT
                        )
                        .show()
                }
        }
    }


}