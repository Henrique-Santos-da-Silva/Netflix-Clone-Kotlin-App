package club.androidexpress.netflixclone.model

data class MovieDetail(
    val movie: Movie,
    val movieSimilar: List<Movie>
)
