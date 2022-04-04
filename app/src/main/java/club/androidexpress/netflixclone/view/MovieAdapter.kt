package club.androidexpress.netflixclone.view

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import club.androidexpress.netflixclone.databinding.MovieItemBinding
import club.androidexpress.netflixclone.model.Movie
import club.androidexpress.netflixclone.util.ImageDownloaderTask
import club.androidexpress.netflixclone.util.OnItemClickListener

// TODO: 12/12/2021 As partes comentadas fazem parte de uma implementação de uma interface de um evento de click,
//  que foi subtituida pelas funçoes de tipos e lambdas do Kotlin, que podem ser usadas quando só se precisa de um evento

class MovieAdapter(private val movies: MutableList<Movie>):  RecyclerView.Adapter<MovieAdapter.MovieHolder>() /*, OnItemClickListener */{
    private lateinit var context: Context

    inner class MovieHolder(private val movieBinding: MovieItemBinding, private val onClick:((Int) -> Unit) /*private val listener: OnItemClickListener*/): RecyclerView.ViewHolder(movieBinding.root) {
        fun bind(movie: Movie) {
            ImageDownloaderTask(movieBinding.imgViewCover).execute(movie.coverUrl)

            movieBinding.root.setOnClickListener {
//                listener.onClick(adapterPosition)
//                onClick(adapterPosition)
                onClick.invoke(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieHolder {
        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
        val binding: MovieItemBinding = MovieItemBinding.inflate(inflater, parent, false)
        context = parent.context
        return MovieHolder(binding, onClick)
    }

    override fun onBindViewHolder(holder: MovieHolder, position: Int) = holder.bind(movies[position])


    override fun getItemCount(): Int = movies.size

//    override fun onClick(position: Int) {
//        if (movies.get(position).id <= 3) {
//            val intent = Intent(context, MovieActivity::class.java)
//            intent.putExtra("id", movies[position].id)
//            startActivity(context, intent, null)
//        }
//    }

    private val onClick: ((Int) -> Unit) = { position ->
        if (movies[position].id <= 3) {
            val intent = Intent(context, MovieActivity::class.java)
            intent.putExtra("id", movies[position].id)
            startActivity(context, intent, null)
        }
    }
}