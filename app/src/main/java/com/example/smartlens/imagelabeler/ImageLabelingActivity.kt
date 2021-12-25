package com.example.smartlens.imagelabeler

import androidx.core.content.ContextCompat
import com.example.smartlens.BaseLensActivity

class ImageLabelingActivity : BaseLensActivity() {
    override val imageAnalyzer = ImageLabelAnalyzer(this,this)

    override fun startScanner() {
        startImageLabeling()
    }

    private fun startImageLabeling() {
        imageAnalysis.setAnalyzer(
            ContextCompat.getMainExecutor(this),
            imageAnalyzer
        )
    }

}