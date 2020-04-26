package id.dsc.realtour.ui.home

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.ScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.vr.sdk.widgets.pano.VrPanoramaEventListener
import com.google.vr.sdk.widgets.pano.VrPanoramaView
import id.dsc.realtour.data.model.ParentContent
import id.dsc.realtour.data.model.ParentContentJava

class VRImageLoader(private val context: Context) : VrListener {

    override fun onVrClick() {

    }

    override fun onSelected(position: Int) {
        loadImages(position)
    }

    private var panoOptions: VrPanoramaView.Options? = null
    lateinit var glide: RequestManager
    private var parentContents: MutableList<ParentContentJava>? = null
    private var listBottomAdapter: SpotVRAdapter? = null
    internal lateinit var layoutManager: LinearLayoutManager
    var isShown = true
    var pano_view: VrPanoramaView? = null

    fun loadVR(parentContentsItem: MutableList<ParentContentJava>, pano_view: VrPanoramaView,
                rvVR: RecyclerView? = null) {

        this.parentContents = parentContentsItem
        this.pano_view = pano_view

        listBottomAdapter = SpotVRAdapter(parentContents, context, this)
        layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        rvVR?.layoutManager = layoutManager
        rvVR?.adapter = listBottomAdapter
        pano_view.visibility = View.VISIBLE
        glide = Glide.with(context)
        pano_view.setInfoButtonEnabled(false)
        pano_view.isClickable = true
        pano_view.setTouchTrackingEnabled(true)
        pano_view.setStereoModeButtonEnabled(false)
        pano_view.setFullscreenButtonEnabled(false)
        panoOptions = VrPanoramaView.Options()
        panoOptions!!.inputType = VrPanoramaView.Options.TYPE_MONO
        loadImages(0)

        pano_view.setEventListener(object : VrPanoramaEventListener() {
            override fun onClick() {
                super.onClick()
//                if (!isShown) {
//                    val mAnimationSet = AnimatorSet()
//                    val fadeIn = ObjectAnimator.ofFloat(rvVR, View.ALPHA, 0f, 1f)
//                    fadeIn.addListener(object : Animator.AnimatorListener {
//                        override fun onAnimationStart(animation: Animator) {
//                            rvVR?.visibility = View.VISIBLE
//                        }
//
//                        override fun onAnimationEnd(animation: Animator) {}
//
//                        override fun onAnimationCancel(animation: Animator) {}
//
//                        override fun onAnimationRepeat(animation: Animator) {}
//                    })
//                    fadeIn.interpolator = LinearInterpolator()
//
//                    val fadeInToolbar = ObjectAnimator.ofFloat(ivGradient, View.ALPHA, 0f, 1f)
//                    fadeInToolbar.addListener(object : Animator.AnimatorListener {
//                        override fun onAnimationStart(animation: Animator) {
//                            ivGradient?.visibility = View.VISIBLE
//                        }
//
//                        override fun onAnimationEnd(animation: Animator) {}
//
//                        override fun onAnimationCancel(animation: Animator) {}
//
//                        override fun onAnimationRepeat(animation: Animator) {}
//                    })
//                    fadeInToolbar.interpolator = LinearInterpolator()
//                    mAnimationSet.duration = 350
//                    mAnimationSet.playTogether(fadeIn, fadeInToolbar)
//                    mAnimationSet.start()
//                } else {
//                    setFadeOutAnim(rvVR, ivGradient)
//                }
//                isShown = !isShown

            }
        })
    }

    private fun loadImages(position: Int) {
        glide.asBitmap()
                .load(parentContents?.get(position)?.url)
                .into(object : CustomTarget<Bitmap>() {
                    override fun onLoadFailed(errorDrawable: Drawable?) {
                    }

                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                        pano_view?.loadImageFromBitmap(resource, panoOptions)
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {

                    }

                })
    }

    fun setFadeOutAnim(rvVR: RecyclerView?, ivGradient: ImageView?) {
        val fadeOut = ObjectAnimator.ofFloat(rvVR, "alpha", 1f, .0f)
        fadeOut.duration = 350
        val fadeOutToolbar = ObjectAnimator.ofFloat(ivGradient, "alpha", 1f, .0f)
        fadeOutToolbar.duration = 350

        val mAnimationSet = AnimatorSet()
        mAnimationSet.play(fadeOut).with(fadeOutToolbar)
        mAnimationSet.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                rvVR?.visibility = View.GONE
            }
        })
        mAnimationSet.start()
    }
}
