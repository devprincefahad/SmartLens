package com.example.smartlens.barcode

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.example.smartlens.BaseLensActivity
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import kotlinx.android.synthetic.main.activity_base_lens.*

class BarcodeAnalyzer(val context: Context,val si: SampleInterface) : ImageAnalysis.Analyzer {

    val scanner = BarcodeScanning.getClient()

    @SuppressLint("UnsafeOptInUsageError")
    override fun analyze(imageProxy: ImageProxy) {

        Log.d("BARCODE", "image analyzed")

        imageProxy.image?.let {
            val inputImage = InputImage.fromMediaImage(
                it,
                imageProxy.imageInfo.rotationDegrees
            )
            scanner.process(inputImage)
                .addOnSuccessListener { codes ->
                    codes.forEach { barcode ->

                        si.getResult("Scanned Value = ${barcode.rawValue}\n")

                        Log.d(
                            "BARCODE", """
                            Format = ${barcode.format}
                            Value = ${barcode.rawValue}
                         """.trimIndent()
                        )
                    }
                }
                .addOnFailureListener { ex ->
                    Log.e("BARCODE", "Detection Failed", ex)
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