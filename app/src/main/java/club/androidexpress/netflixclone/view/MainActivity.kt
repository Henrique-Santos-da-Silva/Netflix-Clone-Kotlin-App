package club.androidexpress.netflixclone.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import club.androidexpress.netflixclone.databinding.ActivityMainBinding
import club.androidexpress.netflixclone.model.Category
import club.androidexpress.netflixclone.util.CategoryLoader
import club.androidexpress.netflixclone.util.CategoryTask

class MainActivity : AppCompatActivity(), CategoryLoader {
    private val binding: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private lateinit var mainAdapter: MainAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val categories: MutableList<Category> = mutableListOf()

        mainAdapter = MainAdapter(categories)

        binding.rvMain.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        binding.rvMain.adapter = mainAdapter

        CategoryTask(this).execute("https://tiagoaguiar.co/api/netflix/home")
    }

    override fun onResult(categories: List<Category>) {
        mainAdapter.setCategories(categories)
        mainAdapter.notifyDataSetChanged()
    }
}