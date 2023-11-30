package com.gitz.mejicomp

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Rect
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.gitz.mejicomp.databinding.ActivityAboutBinding

class AboutActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAboutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAboutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
        }

        val originalBitmap = BitmapFactory.decodeResource(resources, R.drawable.about_image)

        val circularBitmap = getCircleBitmap(originalBitmap)

        binding.aboutImage.setImageBitmap(circularBitmap)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun getCircleBitmap(bitmap: Bitmap): Bitmap {
        val outputSize = 720
        val scaledBitmap = Bitmap.createScaledBitmap(bitmap, outputSize, outputSize, true)

        val output = Bitmap.createBitmap(
            scaledBitmap.width,
            scaledBitmap.height,
            Bitmap.Config.ARGB_8888
        )

        val canvas = Canvas(output)

        val color = -0xbdbdbe
        val paint = Paint()
        val rect = Rect(0, 0, scaledBitmap.width, scaledBitmap.height)

        paint.isAntiAlias = true
        canvas.drawARGB(0, 0, 0, 0)
        paint.color = color

        val circleBitmap = Bitmap.createBitmap(
            scaledBitmap.width,
            scaledBitmap.height,
            Bitmap.Config.ARGB_8888
        )

        val circleCanvas = Canvas(circleBitmap)

        circleCanvas.drawCircle(
            scaledBitmap.width / 2f,
            scaledBitmap.height / 2f,
            scaledBitmap.width / 2f,
            paint
        )
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        circleCanvas.drawBitmap(scaledBitmap, rect, rect, paint)

        return circleBitmap
    }
}
