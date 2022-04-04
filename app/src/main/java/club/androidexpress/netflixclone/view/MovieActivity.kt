package club.androidexpress.netflixclone.view

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import club.androidexpress.netflixclone.R
import club.androidexpress.netflixclone.databinding.ActivityMovieBinding
import club.androidexpress.netflixclone.model.Movie
import club.androidexpress.netflixclone.model.MovieDetail
import club.androidexpress.netflixclone.util.ImageDownloaderTask
import club.androidexpress.netflixclone.util.MovieDetailLoader
import club.androidexpress.netflixclone.util.MovieDetailTask

class MovieActivity : AppCompatActivity(), MovieDetailLoader {
    private val binding: ActivityMovieBinding by lazy { ActivityMovieBinding.inflate(layoutInflater) }
    private lateinit var movieAdapter: MovieSimilarAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)


        supportActionBar?.let { toolbar ->
            toolbar.setDisplayHomeAsUpEnabled(true)
            toolbar.setHomeAsUpIndicator(R.drawable.ic_arrow_back)
            toolbar.title = null
        }

        val movies: MutableList<Movie> = mutableListOf()
        movieAdapter = MovieSimilarAdapter(movies)

        binding.rvSimilar.adapter = movieAdapter
        binding.rvSimilar.layoutManager = GridLayoutManager(this, 3)

        intent.extras?.let {
            val id: Int = it.getInt("id")
            MovieDetailTask(this).execute("https://tiagoaguiar.co/api/netflix/$id")
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) finish()
        return super.onOptionsItemSelected(item)
    }

    override fun movieDetailonResult(movieDetail: MovieDetail) {
        with(binding) {
            txtViewTitle.text = movieDetail.movie.title
            txtViewDescription.text = movieDetail.movie.desc
            txtViewCast.text = movieDetail.movie.cast
        }

        ImageDownloaderTask(binding.imgViewCover).apply {
            shadowEnabled = true
            execute(movieDetail.movie.coverUrl)
        }

        movieAdapter.setMovies(movieDetail.movieSimilar)
        movieAdapter.notifyDataSetChanged()
    }
}