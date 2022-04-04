package club.androidexpress.netflixclone.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import club.androidexpress.netflixclone.databinding.MovieSimilarItemBinding
import club.androidexpress.netflixclone.model.Movie
import club.androidexpress.netflixclone.util.ImageDownloaderTask

class MovieSimilarAdapter (private val movies: MutableList<Movie>):  RecyclerView.Adapter<MovieSimilarAdapter.MovieSimilarHolder>() {
    inner class MovieSimilarHolder(private val movieBinding: MovieSimilarItemBinding) : RecyclerView.ViewHolder(movieBinding.root) {
        fun bind(movie: Movie) {
            ImageDownloaderTask(movieBinding.imgViewCover).execute(movie.coverUrl)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieSimilarHolder {
        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
        val binding: MovieSimilarItemBinding = MovieSimilarItemBinding.inflate(inflater, parent, false)
        return MovieSimilarHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieSimilarHolder, position: Int)= holder.bind(movies[position])

    override fun getItemCount(): Int = movies.size

    fun setMovies(movies: List<Movie>) {
        this.movies.clear()
        this.movies.addAll(movies)
    }

}