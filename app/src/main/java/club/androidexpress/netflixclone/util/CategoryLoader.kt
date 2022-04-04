package club.androidexpress.netflixclone.util

import club.androidexpress.netflixclone.model.Category

interface CategoryLoader {
    fun onResult(categories: List<Category>)
}