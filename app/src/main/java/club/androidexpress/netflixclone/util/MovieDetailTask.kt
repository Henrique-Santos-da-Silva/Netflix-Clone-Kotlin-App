package club.androidexpress.netflixclone.util

import android.os.AsyncTask
import club.androidexpress.netflixclone.model.Movie
import club.androidexpress.netflixclone.model.MovieDetail
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

class MovieDetailTask(private val movieDetailLoader: MovieDetailLoader): AsyncTask<String, Unit, MovieDetail>(), MovieDetailLoader by movieDetailLoader {

    override fun doInBackground(vararg params: String): MovieDetail? {
        val url: String = params[0]

        try {
            val requestUrl = URL(url)
            val urlConnection: HttpsURLConnection = requestUrl.openConnection() as HttpsURLConnection
            urlConnection.readTimeout = 2000
            urlConnection.connectTimeout = 2000

            val responseCode: Int = urlConnection.responseCode
            if (responseCode > 400) {
                throw IOException("Error na comunicação do servidor")
            }

            val inputStream = BufferedInputStream(urlConnection.inputStream)
            val jsonAsString: String = toString(inputStream)

            val movieDetail: MovieDetail = getMovieDetail(JSONObject(jsonAsString))

            inputStream.close()

            return movieDetail

        } catch (e: MalformedURLException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        return null
    }

    override fun onPostExecute(movieDetail: MovieDetail) {
        super.onPostExecute(movieDetail)

        movieDetailLoader.movieDetailonResult(movieDetail)
    }

    @Throws(JSONException::class)
    private fun getMovieDetail(json: JSONObject): MovieDetail {
        val id: Int = json.getInt("id")
        val title: String = json.getString("title")
        val desc: String = json.getString("desc")
        val cast: String= json.getString("cast")
        val coverUrl: String = json.getString("cover_url")

        val movies: MutableList<Movie> = mutableListOf()
        val movieArray: JSONArray = json.getJSONArray("movie")

        for (i: Int in 0 until movieArray.length()) {
            val movie: JSONObject = movieArray.getJSONObject(i)
            val c: String = movie.getString("cover_url")
            val idSimilar: Int = movie.getInt("id")

            val similar = Movie()

            similar.id = idSimilar
            similar.coverUrl = c

            movies.add(similar)
        }

        val movie = Movie(id, coverUrl, title, desc, cast)


        return MovieDetail(movie, movies)

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