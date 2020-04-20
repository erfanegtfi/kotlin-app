package com.e.kotlinapp

import android.graphics.PorterDuff
import android.text.TextUtils
import android.util.Log
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.e.kotlinapp.network.api.ApiClient
import com.squareup.picasso.Picasso


//https://softwarebrothers.co/blog/bindingadapter/
//https://android.jlelse.eu/android-data-binding-binding-adapters-part-5-2bc91e43caa0

//https://softwarebrothers.co/blog/bindingadapter/
//https://android.jlelse.eu/android-data-binding-binding-adapters-part-5-2bc91e43caa0

@BindingAdapter("favorite_color")
fun ImageView.favoriteColor(favorite: Boolean) {
    setColorFilter(
        ContextCompat.getColor(
            context,
            if (favorite) R.color.colorAccent else R.color.colorAccent
        ), PorterDuff.Mode.SRC_IN
    )
}

@BindingAdapter("image_url")
fun ImageView.loadImage(imageUrl: String) {
    Log.v("loadImage", imageUrl);
    Picasso.get()
        .load(
            if (TextUtils.isEmpty(imageUrl))
                ApiClient.BASE_IMAGE_URL else imageUrl
        )
        .placeholder(R.drawable.img_placeholder)
        .error(R.drawable.img_placeholder)
        .into(this);
}

@BindingAdapter("circular_image_url")
fun ImageView.circularImageUrl(imageUrl: String) {
    Log.v("circularImageUrl", imageUrl);
    Picasso.get()
        .load(
            if (TextUtils.isEmpty(imageUrl))
                ApiClient.BASE_IMAGE_URL else imageUrl
        )
        .placeholder(R.drawable.img_placeholder_user)
        .error(R.drawable.img_placeholder_user)
        .transform(CircleTransform()).fit()
        .into(this);
}


//    @BindingAdapter("dis_like_color")
//    fun ImageView.disLikeColor(like: LikeType) {
//        setColorFilter(
//            ContextCompat.getColor(
//                context,
//                if (like === LikeType.DOWN_VOTE) R.color.md_red_400 else R.color.black
//            ), PorterDuff.Mode.SRC_IN
//        )
//    }
//
//    @BindingAdapter("like_color")
//    fun ImageView.likeColor(like: LikeType) {
//        setColorFilter(
//            ContextCompat.getColor(
//                context,
//                if (like === LikeType.UP_VOTE) R.color.md_green_400 else R.color.black
//            ), PorterDuff.Mode.SRC_IN
//        )
//    }

//    @BindingAdapter(
//        value = ["span_string_from", "span_string_to", "span_string_color"],
//        requireAll = true
//    )
//    fun TextView.spanString(view: TextView, from: Int, to: Int, @ColorRes color: Int) {
//        if (!TextUtils.isEmpty(view.text)) view.setText(
//            UtilsSpannable.spannableTextViewPart(
//                view,
//                from,
//                to,
//                color
//            )
//        ) else view.text = view.text
//    }
