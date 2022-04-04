package club.androidexpress.netflixclone.util

import club.androidexpress.netflixclone.model.MovieDetail

interface MovieDetailLoader {
    fun movieDetailonResult(movieDetail: MovieDetail)
}