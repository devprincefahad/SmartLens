package com.example.smartlens.barcode

import androidx.core.content.ContextCompat
import com.example.smartlens.BaseLensActivity

class BarcodeActivity : BaseLensActivity() {

    override val imageAnalyzer = BarcodeAnalyzer(this,this)

    override fun startScanner() {
        scanBarcode()
    }

    private fun scanBarcode() {
        imageAnalysis.setAnalyzer(
            ContextCompat.getMainExecutor(this),
            imageAnalyzer
        )

    }
}