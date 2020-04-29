package id.dsc.realtour.ui.ar

import android.graphics.Point
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.ar.core.*
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.FrameTime
import com.google.ar.sceneform.assets.RenderableSource
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.rendering.Renderable
import com.google.ar.sceneform.ux.ArFragment
import com.google.ar.sceneform.ux.TransformableNode
import id.dsc.realtour.R
import id.dsc.realtour.data.model.Feed
import id.dsc.realtour.data.model.Feeds
import kotlinx.android.synthetic.main.activity_a_r.*


class ARActivity : AppCompatActivity() {

    lateinit var arFragment: ArFragment
    private val pointer = PointerDrawable()
    private var isTracking: Boolean = false
    private var isHitting: Boolean = false
    val feed  by lazy { intent.getParcelableExtra<Feed>("feed") }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_a_r)
        arFragment = supportFragmentManager.findFragmentById(R.id.sceneform_fragment) as ArFragment
        arFragment.arSceneView.scene
            .addOnUpdateListener {
                arFragment.onUpdate(it)
                onUpdate()
            }
        initializeGallery()
        Log.e("urlll", feed.parentContent[0].url)
    }

    private fun onUpdate() {
        val trackingChanged: Boolean = updateTracking()
        val contentView: View = findViewById(android.R.id.content)
        if (trackingChanged) {
            if (isTracking) {
                contentView.getOverlay().add(pointer)
            } else {
                contentView.getOverlay().remove(pointer)
            }
            contentView.invalidate()
        }
        if (isTracking) {
            val hitTestChanged: Boolean = updateHitTest()
            if (hitTestChanged) {
                pointer.isEnabled = isHitting
                contentView.invalidate()
            }
        }
    }

    private fun updateTracking(): Boolean {
        val frame: Frame? = arFragment.arSceneView.arFrame
        val wasTracking = isTracking
        isTracking = frame != null &&
                frame.getCamera().getTrackingState() === TrackingState.TRACKING
        return isTracking != wasTracking
    }

    private fun updateHitTest(): Boolean {
        val frame = arFragment.arSceneView.arFrame
        val pt = getScreenCenter()
        val hits: List<HitResult>
        val wasHitting = isHitting
        isHitting = false
        if (frame != null) {
            hits = frame.hitTest(pt.x.toFloat(), pt.y.toFloat())
            for (hit in hits) {
                val trackable = hit.trackable
                if (trackable is Plane &&
                    (trackable as Plane).isPoseInPolygon(hit.hitPose)
                ) {
                    tv_tap_to_place.isEnabled = true
                    tv_tap_to_place.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_border_button_primary))
                    isHitting = true
                    break
                }
            }
        }
        return wasHitting != isHitting
    }

    private fun getScreenCenter(): Point {
        val vw = findViewById<View>(android.R.id.content)
        return Point(vw.width / 2, vw.height / 2)
    }

    fun initializeGallery(): Unit {

        tv_tap_to_place.setOnClickListener{
            addObject(Uri.parse(feed.parentContent[0].url))
        }


    }

    private fun addObject(parse: Uri?) {
        val frame = arFragment.arSceneView.arFrame
        val point = getScreenCenter()

        if(frame != null) {
            val hits = frame.hitTest(point.x.toFloat(), point.y.toFloat())

            for(hit in hits) {
                val trackable = hit.trackable

                if(trackable is Plane &&
                    (trackable as Plane).isPoseInPolygon(hit.hitPose)) {
                    placeObject(arFragment, hit.createAnchor(), parse)
                }
            }
        }
    }

    private fun placeObject(arFragment: ArFragment, createAnchor: Anchor?, parse: Uri?) {
        ModelRenderable.builder()
            .setSource(arFragment.context, RenderableSource.builder().setSource(
                arFragment.context,
                parse,
                RenderableSource.SourceType.GLTF2).build())
            .setRegistryId(parse)
            .build()
            .thenAccept {
                addNodeToScene(arFragment, createAnchor, it)
            }
//        ModelRenderable.builder()
//                .setSource(arFragment.context, parse)
//                .build()
//                .thenAccept{ renderable: Renderable ->
//                    addNodeToScene(arFragment, createAnchor, renderable)
//                }
            .exceptionally { throwable  ->
                val builder: AlertDialog.Builder = AlertDialog.Builder(this)
                builder.setTitle("Error")
                    .setMessage(throwable.message)
                val dialog = builder.create()
                dialog.show()
                return@exceptionally null
            }
    }

    private fun addNodeToScene(arFragment: ArFragment, createAnchor: Anchor?, renderable: Renderable?) {
        val anchorNode = AnchorNode(createAnchor)
        val transformableNode = TransformableNode(arFragment.transformationSystem)
        transformableNode.renderable  = renderable
        transformableNode.setParent(anchorNode)
        arFragment.arSceneView.scene.addChild(anchorNode)
        transformableNode.select()
    }


}
