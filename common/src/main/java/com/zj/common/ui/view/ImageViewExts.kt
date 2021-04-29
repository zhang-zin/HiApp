package com.zj.common.ui.view

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.widget.ImageView
import android.widget.SimpleCursorTreeAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.zj.hi_library.util.HiViewUtil

fun ImageView.loadUrl(url: String?) {
    if (HiViewUtil.isActivityDestroyed(context)) return
    if (!url.isNullOrEmpty())
        Glide.with(context).load(url).into(this)
}

fun ImageView.loadUrl(url: String?, callback: (Drawable) -> Unit) {
    if (HiViewUtil.isActivityDestroyed(context)) return
    if (!url.isNullOrEmpty())
        Glide.with(context).load(url).into(object : SimpleTarget<Drawable>() {
            override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                callback.invoke(resource)
            }
        })
}

fun ImageView.loadCircle(url: String) {
    if (HiViewUtil.isActivityDestroyed(context)) return
    Glide.with(this).load(url)
        .transform(CircleCrop()).into(this)
}

//巨坑，glide 的 图片裁剪 和 imageview scaleType 有冲突。 centerCrop .
fun ImageView.loadCorner(url: String, corner: Int) {
    if (HiViewUtil.isActivityDestroyed(context)) return
    Glide.with(this).load(url).transform(CenterCrop(), RoundedCorners(corner)).into(this)
}

fun ImageView.loadCircleBorder(
    url: String,
    borderWidth: Float = 0f,
    borderColor: Int = Color.WHITE
) {
    if (HiViewUtil.isActivityDestroyed(context)) return
    Glide.with(this).load(url).transform(CircleBorderTransform(borderWidth, borderColor)).into(this)

}

class CircleBorderTransform(private val borderWidth: Float, borderColor: Int) : CircleCrop() {
    private var borderPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)

    init {
        borderPaint.color = borderColor
        borderPaint.style = Paint.Style.STROKE
        borderPaint.strokeWidth = borderWidth
    }

    override fun transform(
        pool: BitmapPool,
        toTransform: Bitmap,
        outWidth: Int,
        outHeight: Int
    ): Bitmap {
        val transform = super.transform(pool, toTransform, outWidth, outHeight)
        val canvas = Canvas(transform)

        val halfWidth = outWidth / 2.toFloat()
        val halfHeight = outHeight / 2.toFloat()


        canvas.drawCircle(
            halfWidth,
            halfHeight,
            halfWidth.coerceAtMost(halfHeight) - borderWidth / 2,
            borderPaint
        )

        canvas.setBitmap(null)

        return transform
    }
}