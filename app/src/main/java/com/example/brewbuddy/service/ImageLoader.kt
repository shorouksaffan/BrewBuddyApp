package com.example.brewbuddy.service

import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ImageLoader @Inject constructor() {

    fun loadImage(
        imageView: ImageView,
        url: String,
        @DrawableRes placeholder: Int? = null,
        crossFade: Boolean = true
    ) {
        val request = Glide.with(imageView.context)
            .load(url)

        placeholder?.let { request.placeholder(it) }

        if (crossFade) {
            request.transition(DrawableTransitionOptions.withCrossFade())
        }

        request.into(imageView)
    }

    fun loadImage(
        imageView: ImageView,
        @DrawableRes resourceId: Int,
        crossFade: Boolean = true
    ) {
        val request = Glide.with(imageView.context)
            .load(resourceId)

        if (crossFade) {
            request.transition(DrawableTransitionOptions.withCrossFade())
        }

        request.into(imageView)
    }
}