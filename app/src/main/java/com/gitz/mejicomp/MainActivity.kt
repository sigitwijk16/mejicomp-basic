package com.gitz.mejicomp

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gitz.mejicomp.adapter.ListPartsAdapter
import com.gitz.mejicomp.databinding.ActivityMainBinding
import com.gitz.mejicomp.model.Parts

class MainActivity : AppCompatActivity() {
    private lateinit var rvParts: RecyclerView
    private lateinit var binding: ActivityMainBinding
    private val list = ArrayList<Parts>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        rvParts = binding.rvParts
        rvParts.setHasFixedSize(true)

        list.addAll(getListParts())
        showRecyclerList()
    }

    private fun getListParts(): ArrayList<Parts> {
        val titles = resources.getStringArray(R.array.data_name)
        val descriptions = resources.getStringArray(R.array.data_description)
        val images = resources.obtainTypedArray(R.array.data_image)
        val prices = resources.getStringArray(R.array.data_price)

        val listParts = ArrayList<Parts>()
        for (i in titles.indices) {
            val parts = Parts(titles[i], descriptions[i], images.getResourceId(i, -1), i)
            listParts.add(parts)
        }
        images.recycle()
        return listParts
    }

    private fun showRecyclerList() {
        rvParts.layoutManager = LinearLayoutManager(this)
        val listPartsAdapter = ListPartsAdapter(list)
        rvParts.adapter = listPartsAdapter

        listPartsAdapter.setOnItemClickCallback(object : ListPartsAdapter.OnItemClickCallback {
            override fun onItemClicked(data: Parts) {
                val intent = Intent(this@MainActivity, DetailActivity::class.java)
                intent.putExtra(DetailActivity.EXTRA_PARTS, data)
                startActivity(intent)
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.about_page -> {
                val intent = Intent(this, AboutActivity::class.java)
                startActivity(intent)
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}
