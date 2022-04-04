package club.androidexpress.netflixclone.util

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.LayerDrawable
import android.os.AsyncTask
import android.widget.ImageView
import androidx.core.content.ContextCompat
import club.androidexpress.netflixclone.R
import java.io.InputStream
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class ImageDownloaderTask(private val imageViewParams: ImageView): AsyncTask<String, Unit, Bitmap>() {
    private lateinit var urlConnection: HttpsURLConnection

    var shadowEnabled: Boolean = false

    override fun doInBackground(vararg params: String): Bitmap? {
        val urlImg: String = params[0]

        try {
            val requestUrl = URL(urlImg)
            urlConnection = requestUrl.openConnection() as HttpsURLConnection

            val statusCode: Int = urlConnection.responseCode

            if (statusCode != 200) return null

            val inputStream: InputStream = urlConnection.inputStream

            inputStream.let {
                return BitmapFactory.decodeStream(it)
            }

        }catch (e: Exception) {
            e.printStackTrace()
        } finally {
            urlConnection.disconnect()

        }

        return null
    }

    override fun onPostExecute(_bitmap: Bitmap) {
        var bitmap: Bitmap = _bitmap

        if (shadowEnabled) {
            val drawable: LayerDrawable = ContextCompat.getDrawable(imageViewParams.context, R.drawable.shadows) as LayerDrawable

            val bitmapDrawable = BitmapDrawable(bitmap)
            drawable.setDrawableByLayerId(R.id.cover_drawable, bitmapDrawable)
            imageViewParams.setImageDrawable(drawable)

        } else {
            if (bitmap.width < imageViewParams.width || bitmap.height < imageViewParams.height)  {
                val matrix = Matrix()
                matrix.postScale((imageViewParams.width / bitmap.width).toFloat(), (imageViewParams.height / bitmap.height).toFloat())
                bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, false)
            }

            imageViewParams.setImageBitmap(bitmap)
        }

    }
}