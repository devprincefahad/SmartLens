package com.example.smartlens.textrecog

import androidx.camera.core.ImageAnalysis
import androidx.core.content.ContextCompat
import com.example.smartlens.BaseLensActivity

class TextRecognitionActivity : BaseLensActivity() {

    override val imageAnalyzer = TextAnalyzer(this,this)

    override fun startScanner() {
        startTextRecognition()
    }

    private fun startTextRecognition() {
        imageAnalysis.setAnalyzer(
            ContextCompat.getMainExecutor(this),
            imageAnalyzer
        )
    }
}