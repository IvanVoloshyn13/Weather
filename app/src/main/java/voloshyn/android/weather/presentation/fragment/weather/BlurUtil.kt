package voloshyn.android.weather.presentation.fragment.weather

import android.content.Context
import android.graphics.Bitmap
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur
import android.widget.ImageView
import androidx.core.graphics.drawable.toBitmap
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult

object BlurUtil {

    fun blurBitmap(context: Context, bitmap: Bitmap, blurRadius: Float): Bitmap {
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

        script.setRadius(blurRadius)
        script.setInput(input)
        script.forEach(output)
        output.copyTo(blurredBitmap)

        rs.destroy()
        return blurredBitmap
    }

    fun convertBitmap(bitmap: Bitmap): Bitmap {
        return bitmap.copy(Bitmap.Config.ARGB_8888, true)
    }

    suspend fun setBlurredImageFromUrl(
        context: Context,
        imageView: ImageView,
        imageUrl: String,
        blurRadius: Float
    ) {
        val imageLoader = ImageLoader.Builder(context)
            .crossfade(true)
            .build()

        val request = ImageRequest.Builder(context)
            .data(imageUrl)
            .build()

        val bitmap = (imageLoader.execute(request) as SuccessResult).drawable.toBitmap()

        val convertedBitmap = convertBitmap(bitmap)
        val blurredBitmap = blurBitmap(context, convertedBitmap, blurRadius)
        imageView.setImageBitmap(blurredBitmap)
        imageView.scaleType = ImageView.ScaleType.FIT_XY
    }
}