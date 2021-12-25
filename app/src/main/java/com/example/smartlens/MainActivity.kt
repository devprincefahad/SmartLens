package com.example.smartlens

import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.smartlens.barcode.BarcodeActivity
import com.example.smartlens.facedetect.FaceDetectActivity
import com.example.smartlens.imagelabeler.ImageLabelingActivity
import com.example.smartlens.textrecog.TextRecognitionActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    companion object {
        @JvmStatic
        val PHOTO_REQ_CODE = 234

        @JvmStatic
        val EXTRA_DATA = "data"
    }

    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<RecyclerAdapter.ViewHolder>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        val rv = findViewById<RecyclerView>(R.id.recyclerView)

        layoutManager = LinearLayoutManager(this)
        rv.layoutManager = layoutManager
        adapter = RecyclerAdapter()
        rv.adapter = adapter

        /*btnTakeExtPhoto.setOnClickListener {
            val takePhotoIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(takePhotoIntent, PHOTO_REQ_CODE)
        }

        btnCameraActivity.setOnClickListener {
            startActivity(Intent(this, CameraActivity::class.java))
        }

        btnBarcodeActivity.setOnClickListener {
            startActivity(Intent(this, BarcodeActivity::class.java))
        }

        btnFaceDetectActivity.setOnClickListener {
            startActivity(Intent(this, FaceDetectActivity::class.java))
        }
        btnLabelerActivity.setOnClickListener {
            startActivity(Intent(this, ImageLabelingActivity::class.java))
        }

        btnTextRecogActivity.setOnClickListener {
            startActivity(Intent(this, TextRecognitionActivity::class.java))
        }*/

    }

   /* override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == PHOTO_REQ_CODE) {
            val bitmap = data?.extras?.get(EXTRA_DATA) as Bitmap
            imgThumb.setImageBitmap(bitmap)
            return
        }

        super.onActivityResult(requestCode, resultCode, data)
    }*/
}