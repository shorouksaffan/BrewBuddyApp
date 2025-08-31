package com.example.brewbuddy.service

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.example.brewbuddy.R
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ImageLoader @Inject constructor() {

    fun loadImage(
        imageView: ImageView,
        url: String?,
        @DrawableRes placeholder: Int? = null,
        @DrawableRes errorPlaceholder: Int? = null,
        crossFade: Boolean = true,
        roundedCorners: Int = 0,
        centerCrop: Boolean = true,
        skipMemoryCache: Boolean = false,
        listener: ImageLoadListener? = null
    ) {
        if (url.isNullOrBlank()) {
            errorPlaceholder?.let { imageView.setImageResource(it) }
            listener?.onImageLoadFailed(Exception("Empty or null URL"))
            return
        }

        val requestBuilder = Glide.with(imageView.context)
            .load(url)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .skipMemoryCache(skipMemoryCache)

        // Apply request options
        val requestOptions = RequestOptions()
        placeholder?.let { requestOptions.placeholder(it) }
        errorPlaceholder?.let { requestOptions.error(it) }

        if (roundedCorners > 0) {
            requestOptions.transform(RoundedCorners(roundedCorners))
        }

        if (centerCrop) {
            requestOptions.centerCrop()
        }

        requestBuilder.apply(requestOptions)

        // Add transition
        if (crossFade) {
            requestBuilder.transition(DrawableTransitionOptions.withCrossFade())
        }

        // Add listener if provided
        listener?.let {
            requestBuilder.addListener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>,
                    isFirstResource: Boolean
                ): Boolean {
                    listener.onImageLoadFailed(e ?: Exception("Image loading failed"))
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable,
                    model: Any,
                    target: Target<Drawable>,
                    dataSource: DataSource,
                    isFirstResource: Boolean
                ): Boolean {
                    listener.onImageLoadSuccess()
                    return false
                }
            })
        }

        requestBuilder.into(imageView)
    }

    fun loadImage(
        imageView: ImageView,
        @DrawableRes resourceId: Int,
        crossFade: Boolean = true,
        roundedCorners: Int = 0,
        centerCrop: Boolean = true
    ) {
        val requestBuilder = Glide.with(imageView.context)
            .load(resourceId)
        val requestOptions = RequestOptions()
        if (roundedCorners > 0) {
            requestOptions.transform(RoundedCorners(roundedCorners))
        }
        if (centerCrop) {
            requestOptions.centerCrop()
        }

        requestBuilder.apply(requestOptions)

        if (crossFade) {
            requestBuilder.transition(DrawableTransitionOptions.withCrossFade())
        }

        requestBuilder.into(imageView)
    }

    fun loadCircularImage(
        imageView: ImageView,
        url: String?,
        @DrawableRes placeholder: Int? = null,
        @DrawableRes errorPlaceholder: Int? = null
    ) {
        if (url.isNullOrBlank()) {
            errorPlaceholder?.let { imageView.setImageResource(it) }
            return
        }

        val requestBuilder = Glide.with(imageView.context)
            .load(url)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .apply(RequestOptions.circleCropTransform())

        placeholder?.let { requestBuilder.placeholder(it) }
        errorPlaceholder?.let { requestBuilder.error(it) }

        requestBuilder.into(imageView)
    }

    fun clear(imageView: ImageView) {
        Glide.with(imageView.context).clear(imageView)
    }

    interface ImageLoadListener {
        fun onImageLoadSuccess()
        fun onImageLoadFailed(exception: Exception)
    }

    companion object {
        fun loadDrinkThumbnail(
            imageView: ImageView,
            url: String?,
            listener: ImageLoadListener? = null
        ) {
            val loader = ImageLoader()
            loader.loadImage(
                imageView = imageView,
                url = url,
                placeholder = R.drawable.placeholder_drink,
                errorPlaceholder = R.drawable.error_drink,
                roundedCorners = 12,
                centerCrop = true,
                listener = listener
            )
        }

        fun loadDrinkDetail(
            imageView: ImageView,
            url: String?,
            listener: ImageLoadListener? = null
        ) {
            val loader = ImageLoader()
            loader.loadImage(
                imageView = imageView,
                url = url,
                placeholder = R.drawable.placeholder_drink_large,
                errorPlaceholder = R.drawable.error_drink_large,
                roundedCorners = 16,
                centerCrop = true,
                listener = listener
            )
        }

        fun loadUserAvatar(
            imageView: ImageView,
            url: String?,
            listener: ImageLoadListener? = null
        ) {
            val loader = ImageLoader()
            loader.loadCircularImage(
                imageView = imageView,
                url = url,
                placeholder = R.drawable.placeholder_avatar,
                errorPlaceholder = R.drawable.error_avatar
            )
        }
    }
}