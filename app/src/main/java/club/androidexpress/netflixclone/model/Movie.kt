package club.androidexpress.netflixclone.model

class Movie(
    var id: Int,
    var coverUrl: String,
    val title: String,
    val desc: String,
    val cast: String,

) {
    constructor(): this(0, "", "", "", "")
}
