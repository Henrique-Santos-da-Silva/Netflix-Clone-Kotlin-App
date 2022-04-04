package club.androidexpress.netflixclone.view

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import club.androidexpress.netflixclone.databinding.CategoryItemBinding
import club.androidexpress.netflixclone.model.Category

class MainAdapter(private val categories: MutableList<Category>): RecyclerView.Adapter<MainAdapter.CategoryHolder>() {

    private lateinit var context: Context

    inner class CategoryHolder(private val categoryBinding: CategoryItemBinding): RecyclerView.ViewHolder(categoryBinding.root) {
        fun bind(category: Category) {
            with(categoryBinding) {
                txtViewTitle.text = category.name
                rvMovie.adapter = MovieAdapter(category.movies)
                rvMovie.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryHolder {
        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
        val binding: CategoryItemBinding = CategoryItemBinding.inflate(inflater, parent, false)
        context = parent.context
        return CategoryHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryHolder, position: Int) = holder.bind(categories[position])

    override fun getItemCount(): Int = categories.size

    fun setCategories(categories: List<Category>) {
        this.categories.clear()
        this.categories.addAll(categories)
    }
}