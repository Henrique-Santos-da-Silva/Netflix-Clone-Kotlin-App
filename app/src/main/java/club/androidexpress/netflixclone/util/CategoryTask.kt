package club.androidexpress.netflixclone.util

import android.os.AsyncTask
import club.androidexpress.netflixclone.model.Category
import club.androidexpress.netflixclone.model.Movie
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedInputStream
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.net.MalformedURLException
import java.net.URL
import java.nio.charset.Charset
import javax.net.ssl.HttpsURLConnection

class CategoryTask(private val categoryLoader: CategoryLoader) : AsyncTask<String, Unit, List<Category>>(), CategoryLoader by categoryLoader {

    override fun doInBackground(vararg params: String): List<Category> {
        val url: String = params[0]

        try {
            val requestUrl = URL(url)
            val urlConnection = requestUrl.openConnection() as HttpsURLConnection
            urlConnection.readTimeout = 2000
            urlConnection.connectTimeout = 2000

            val responseCode: Int = urlConnection.responseCode
            if (responseCode > 400) {
                throw IOException("Error na comunicação do servidor")
            }

            val inputStream = BufferedInputStream(urlConnection.inputStream)
            val jsonAsString = toString(inputStream)

            val categories: List<Category> = getCategories(JSONObject(jsonAsString))

            inputStream.close()

            return categories

        } catch (e: MalformedURLException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        return emptyList()
    }

    override fun onPostExecute(result: List<Category>) {
        super.onPostExecute(result)

        categoryLoader.onResult(result)
    }



    private fun getCategories(json: JSONObject): List<Category> {
        val categories = mutableListOf<Category>()
        val categoryArray: JSONArray = json.getJSONArray("category")

        for (i in 0 until categoryArray.length()) {
            val category: JSONObject = categoryArray.getJSONObject(i)
            val title: String = category.getString("title")

            val movies = mutableListOf<Movie>()
            val movieArray: JSONArray = category.getJSONArray("movie")

            for (j in 0 until movieArray.length()) {
                val movie: JSONObject = movieArray.getJSONObject(j)

                val coverUrl = movie.getString("cover_url")

                val movieObj = Movie()

                movieObj.coverUrl = coverUrl
                movieObj.id = movie.getInt("id")

                movies.add(movieObj)
            }

            val categoryObj = Category(title, movies)

            categories.add(categoryObj)
        }

        return categories
    }



    @Throws(IOException::class)
    private fun toString(inputStream: InputStream): String {
        val byte = ByteArray(1024)
        val baos = ByteArrayOutputStream()
        var bytesRead: Int

        while (true) {
            bytesRead = inputStream.read(byte)
            if (bytesRead == -1) break
            baos.write(byte, 0, bytesRead)
        }

        return String(baos.toByteArray(), Charset.forName("UTF-8"))
    }

}