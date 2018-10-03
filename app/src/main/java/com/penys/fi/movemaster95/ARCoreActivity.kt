package com.penys.fi.movemaster95

import android.annotation.SuppressLint
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import com.google.ar.core.HitResult
import com.google.ar.core.Plane
import com.google.ar.core.TrackingState
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.ux.ArFragment
import com.google.ar.sceneform.ux.TransformableNode
import kotlinx.android.synthetic.main.activity_arcore.*
import java.util.concurrent.CompletableFuture

class ARCoreActivity : AppCompatActivity() {

    lateinit var testRenderable: ModelRenderable
    lateinit var fragment: ArFragment
    lateinit var renderableFuture: CompletableFuture<ModelRenderable>
    lateinit var modelUri: Uri
    lateinit var duck_sound: MediaPlayer


    var blast_count = 0
    var nro: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_arcore)




        fragment = supportFragmentManager.findFragmentById(R.id.sceneform_fragment) as ArFragment

        blast_counter.text = "Ducks blasted: " + blast_count.toString()
        modelUri = Uri.parse("RubberDuck.sfb")
        duck_sound = MediaPlayer.create(this, R.raw.duck)
        renderableFuture = ModelRenderable.builder()
                .setSource(this, modelUri).build()
        renderableFuture.thenAccept { it -> testRenderable = it }

        fragment.arSceneView.scene.addOnUpdateListener {
            if (fragment.arSceneView.arFrame.camera.trackingState == TrackingState.TRACKING) {
                addObject()
            }
        }
    }

    private fun rndm(): Float {
        var rndm = Math.random() * 0.5
        return rndm.toFloat()

    }

    @SuppressLint("SetTextI18n")
    private fun addObject() {
        val frame = fragment.arSceneView.arFrame
        val hits: List<HitResult>
        if (frame != null) {
            hits = frame.hitTest(rndm(), rndm())
            for (hit in hits) {
                val trackable = hit.trackable
                if (trackable is Plane) {
                    val anchor = hit!!.createAnchor()
                    val anchorNode = AnchorNode(anchor)
                    anchorNode.setParent(fragment.arSceneView.scene)
                    val mNode = TransformableNode(fragment.transformationSystem)
                    if (nro <= 2) {
                        mNode.setParent(anchorNode)
                        mNode.renderable = testRenderable
                        mNode.select()
                        duck_sound.start()
                        nro++

                        Log.d("NUMERO", "$nro")
                    }
                    mNode.setOnTapListener { _, _ ->
                        duck_sound.start()
                        blast_count++
                        blast_counter.text = "Ducks blasted: $blast_count"
                        anchorNode.removeChild(mNode)
                        nro--
                        Log.d("NUMERO", "$nro")
                    }
                } else {

                    return
                }
            }
        }
    }

    private fun getScreenCenter(): android.graphics.Point {
        val vw = findViewById<View>(android.R.id.content)
        return android.graphics.Point(vw.width / 2, vw.height / 2)
    }
}
