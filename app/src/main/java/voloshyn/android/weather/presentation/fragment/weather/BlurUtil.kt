package voloshyn.android.weather.presentation.fragment.weather

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur
import android.util.Log
import android.widget.ImageView
import androidx.core.graphics.drawable.toBitmap
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import voloshyn.android.data.di.DefaultDispatcher
import voloshyn.android.weather.R
import javax.inject.Inject

class BlurUtil @Inject constructor(
    @DefaultDispatcher private val dispatcher: CoroutineDispatcher
){

    private lateinit var imageDrawable: Drawable
    private lateinit var bitmap: Bitmap

    //Maybe using flow here
    private suspend fun blurBitmap(context: Context, bitmap: Bitmap, blurRadius: Float): Bitmap = withContext(dispatcher){
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
        return@withContext blurredBitmap

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
        } catch (e: Exception) {
            imageDrawable = context.getDrawable(R.drawable.welcome_screen_background)!!
            bitmap = imageDrawable.toBitmap()
        }

    }

   suspend fun setBlurredImageFromUrl(
        imageView: ImageView,
        blurRadius: Float
    ) = withContext(Dispatchers.Main){
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


