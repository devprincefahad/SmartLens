package com.example.smartlens.facedetect

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.example.smartlens.barcode.BarcodeAnalyzer
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions

class FaceDetectAnalyzer(val context: Context, val si: BarcodeAnalyzer.SampleInterface) :
    ImageAnalysis.Analyzer {

    val detector = FaceDetection.getClient(
        FaceDetectorOptions.Builder()
            .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
            .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
            .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
            .build()
    )


    @SuppressLint("UnsafeOptInUsageError")
    override fun analyze(imageProxy: ImageProxy) {

        Log.d("FACEDETECT", "image analyzed")

        imageProxy.image?.let {
            val inputImage = InputImage.fromMediaImage(
                it,
                imageProxy.imageInfo.rotationDegrees
            )

            detector.process(inputImage)
                .addOnSuccessListener { faces ->
                    faces.forEach {

//                        si.getResult(it.smilingProbability!!.toString())
                        si.getResult(
                            " Faces = ${faces.size}\n Smiling Probability = ${it.smilingProbability}\n Left Eye Open Probability = ${it.leftEyeOpenProbability}\n" +
                                    " Right Eye Open Probability = ${it.rightEyeOpenProbability}"
                        )

                        Log.d("FACEDETECT", "Faces = ${faces.size}")
                        Log.d(
                            "FACEDETECT", """
                        ${it.smilingProbability}
                        ${it.leftEyeOpenProbability}
                        ${it.rightEyeOpenProbability}
                    """.trimIndent()
                        )
                    }
                }
                .addOnFailureListener { ex ->
                    Log.e("FACEDETECT", "Detection Failed", ex)
                }
                .addOnCompleteListener {
                    imageProxy.close()
                }

        }
            ?: imageProxy.close()
    }

    interface SampleInterface {
        fun getResult(value: String)
    }

}