package com.gitz.mejicomp

import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.gitz.mejicomp.model.Parts
import com.gitz.mejicomp.databinding.ActivityDetailBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext


class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding

    companion object {
        const val EXTRA_PARTS = "extra_parts"
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val parts = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra<Parts>(EXTRA_PARTS, Parts::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra<Parts>(EXTRA_PARTS)
        }
        if (parts != null) {
            populateDetail(parts)
        }
    }

    private fun populateDetail(parts: Parts) {
        supportActionBar?.title = parts.title

        val priceArray = resources.getStringArray(R.array.data_price)
        val formattedPrice = priceArray[parts.price]

        val priceString = getString(R.string.detail_price, formattedPrice)

        Glide.with(this)
            .load(parts.image)
            .into(binding.imgDetailParts)

        binding.tvDetailTitle.text = parts.title
        binding.tvDetailDescription.text = parts.description
        binding.tvDetailPrice.text = priceString
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_detail, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_share -> {
                sharePart()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun sharePart() {
        val parts = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra<Parts>(EXTRA_PARTS, Parts::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra<Parts>(EXTRA_PARTS)
        }

        if (parts != null) {
            val priceArray = resources.getStringArray(R.array.data_price)
            val formattedPrice = priceArray[parts.price]
            val priceString = getString(R.string.detail_price, formattedPrice)

            val shareText = "Check out this part:\n\n" + "${parts.title}\n" + "${parts.description}\n" + priceString

            GlobalScope.launch(Dispatchers.IO) {
                val imageUri = insertImageIntoMediaStore(parts.image)
                withContext(Dispatchers.Main) {
                    if (imageUri != null) {
                        shareTextWithImage(shareText, imageUri)
                    }
                }
            }
        }
    }

    private suspend fun insertImageIntoMediaStore(imageResourceId: Int): Uri {
        val bitmap = Glide.with(this)
            .asBitmap()
            .load(imageResourceId)
            .submit()
            .get()


        val resultBitmap = Bitmap.createBitmap(
            bitmap.width,
            bitmap.height,
            Bitmap.Config.ARGB_8888
        )

        resultBitmap.eraseColor(Color.WHITE)

        val canvas = Canvas(resultBitmap)
        canvas.drawBitmap(bitmap, 0f, 0f, null)

        return withContext(Dispatchers.IO) {
            val imageUrl = MediaStore.Images.Media.insertImage(
                applicationContext.contentResolver,
                resultBitmap,
                "image_with_background",
                null
            )
            Uri.parse(imageUrl)
        }
    }


    private fun shareTextWithImage(shareText: String, imageUri: Uri) {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, shareText)
            putExtra(Intent.EXTRA_STREAM, imageUri)
            type = "image/jpeg"
        }

        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivity(shareIntent)
    }

}