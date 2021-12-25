package com.example.smartlens.imagelabeler

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.example.smartlens.barcode.BarcodeAnalyzer
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabel
import com.google.mlkit.vision.label.ImageLabelerOptionsBase
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions

class ImageLabelAnalyzer(val context: Context, val si: BarcodeAnalyzer.SampleInterface) :
    ImageAnalysis.Analyzer {

    private val labeler = ImageLabeling.getClient(
        ImageLabelerOptions.Builder()
            .setConfidenceThreshold(0.5F)
            .build()
    )

    @SuppressLint("UnsafeOptInUsageError")
    override fun analyze(imageProxy: ImageProxy) {

        Log.d("LABEL", "image analyzed")

        imageProxy.image?.let {
            val inputImage = InputImage.fromMediaImage(
                it,
                imageProxy.imageInfo.rotationDegrees
            )
            labeler.process(inputImage)
                .addOnSuccessListener { labels ->
                    labels.forEach { label ->
                        si.getResult(" Object = ${label.text}\n Confidence = ${label.confidence}")
                        Log.d(
                            "LABEL", """
                            Format = ${label.text}
                            Confidence = ${label.confidence}
                         """.trimIndent()
                        )
                    }
                }
                .addOnFailureListener { ex ->
                    Log.e("LABEL", "Detection Failed", ex)
                }
                .addOnCompleteListener {
                    imageProxy.close()
                }

        } ?: imageProxy.close()
    }

    interface SampleInterface {
        fun getResult(value: String)
    }

}