package com.example.doan_nhom9_sang6.TienAnh_Activity.AdapterTrangChu

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter

class BannerAdapter(
    private val context: Context,
    private val bannerImages: List<Int>,
    private val onClick: (Int) -> Unit
) : PagerAdapter() {
    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val imageView = ImageView(context)
        imageView.setImageResource(bannerImages[position])
        imageView.scaleType = ImageView.ScaleType.CENTER_CROP

        imageView.setOnClickListener {
            onClick(position)
        }

        container.addView(imageView)
        return imageView
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun getCount(): Int {
        return bannerImages.size
    }
}
