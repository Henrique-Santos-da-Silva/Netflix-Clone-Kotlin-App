package club.androidexpress.netflixclone.model

data class Category(
    val name: String,
    val movies: MutableList<Movie>
)
