package com.example.smartlens

import android.Manifest
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import kotlinx.android.synthetic.main.activity_camera.*
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

class CameraActivity : AppCompatActivity() {

    companion object {
        @JvmStatic
        val CAMERA_PERM_CODE = 422
    }

    private var imageCapture: ImageCapture? = null

    private fun createNextPhoto(): Pair<Uri, OutputStream> {
        val displayName = "IMG" + System.currentTimeMillis()
        val fileName = "MLKitSample" + File.separator +
                displayName + ".jpg"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, displayName)
                put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
                put(
                    MediaStore.MediaColumns.RELATIVE_PATH,
                    Environment.DIRECTORY_DCIM + File.separator + fileName
                )
            }
            val contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            val fileUri = contentResolver.insert(contentUri, contentValues)
            return Pair(fileUri!!, contentResolver.openOutputStream(fileUri)!!)
        } else {
            val dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
            val photoDir = File(dir, "MLKitSample")
            if (!photoDir.exists()) {
                photoDir.mkdirs()
            }
            val file = File(photoDir, "$displayName.jpg")
            return Pair(file.toUri(), FileOutputStream(file))
        }


    }

    private fun askCameraPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE),
            CAMERA_PERM_CODE
        )
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener(
            Runnable {
                val cameraProvider = cameraProviderFuture.get()

                val preview = Preview.Builder()
                    .build()
                    .also {
                        it.setSurfaceProvider(previewView.surfaceProvider)
                    }

                imageCapture = ImageCapture.Builder().build()

                val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

                try {
                    cameraProvider.unbindAll()
                    cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture)
                } catch (ex: Exception) {
                    Log.e("CAM", "Error bindind camera", ex)
                }

            },
            ContextCompat.getMainExecutor(this)

        )

    }

    private fun takePhoto() {
        if (imageCapture == null) {
            Toast.makeText(this, "Camera not available", Toast.LENGTH_SHORT).show()
            return
        }
        val outputDetails = createNextPhoto()
        val outputFileOptions =
            ImageCapture.OutputFileOptions.Builder(outputDetails.second).build()

        imageCapture!!.takePicture(
            outputFileOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    Toast.makeText(this@CameraActivity, "Image Saved", Toast.LENGTH_SHORT).show()
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                        with(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)) {
                            data = outputDetails.first
                            sendBroadcast(this)
                        }
                    }
                }

                override fun onError(exception: ImageCaptureException) {
                    Toast.makeText(this@CameraActivity, "Image Saving Failed", Toast.LENGTH_SHORT)
                        .show()
                    Log.e("CAM", "Image Saving Failed", exception)
                }

            }
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)

        askCameraPermission()

        btnTakePhoto.setOnClickListener { takePhoto() }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == CAMERA_PERM_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startCamera()
            } else {
                AlertDialog.Builder(this)
                    .setTitle("Permission Error")
                    .setMessage("Camera Permission not provided")
                    .setPositiveButton("OK") { _, _ -> finish() }
                    .setCancelable(false)
                    .show()
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

}

