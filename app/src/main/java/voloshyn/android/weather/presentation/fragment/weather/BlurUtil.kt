package voloshyn.android.weather.presentation.fragment.weather

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur
import android.widget.ImageView
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.graphics.drawable.toBitmap
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import voloshyn.android.weather.R
import kotlin.ClassCastException

object BlurUtil {

    private lateinit var imageDrawable: Drawable
    private lateinit var bitmap: Bitmap

    private fun blurBitmap(context: Context, bitmap: Bitmap, blurRadius: Float): Bitmap {
        val rs = RenderScript.create(context)
        var blurredBitmap = bitmap.copy(bitmap.config, false)

        val input = Allocation.createFromBitmap(
            rs,
            bitmap,
            Allocation.MipmapControl.MIPMAP_NONE,
            Allocation.USAGE_SCRIPT
        )
        val output = Allocation.createTyped(rs, input.type)
        val script = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs))
        if (blurRadius > 0)
            script.setRadius(blurRadius)
        script.setInput(input)
        script.forEach(output)
        output.copyTo(blurredBitmap)

        rs.destroy()
        return blurredBitmap

    }

    private fun convertBitmap(bitmap: Bitmap): Bitmap {
        return bitmap.copy(Bitmap.Config.ARGB_8888, true)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    suspend fun initialize(context: Context, imageUrl: String) {
        val imageLoader = ImageLoader.Builder(context)
            .crossfade(true)
            .build()

        val request = ImageRequest.Builder(context)
            .data(imageUrl)
            .build()
        try {
            imageDrawable = (imageLoader.execute(request) as SuccessResult).drawable
            bitmap = imageDrawable.toBitmap()
        }catch (e:Exception){
            imageDrawable= context.getDrawable(R.drawable.welcome_screen_background)!!
            bitmap = imageDrawable.toBitmap()
        }

    }

    fun setBlurredImageFromUrl(
        imageView: ImageView,
        blurRadius: Float
    ) {
        val convertedBitmap = convertBitmap(bitmap)
        if (blurRadius > 0f) {
            val blurredBitmap = blurBitmap(imageView.context, convertedBitmap, blurRadius)
            imageView.setImageBitmap(blurredBitmap)
            imageView.scaleType = ImageView.ScaleType.FIT_XY
        } else {
            imageView.setImageDrawable(imageDrawable)
            imageView.scaleType = ImageView.ScaleType.FIT_XY
        }
    }

}


