package com.creativegames.andenginedemo

import android.graphics.Color
import android.util.DisplayMetrics
import android.util.Log
import org.andengine.engine.camera.ZoomCamera
import org.andengine.engine.options.EngineOptions
import org.andengine.engine.options.ScreenOrientation
import org.andengine.engine.options.WakeLockOptions
import org.andengine.engine.options.resolutionpolicy.FillResolutionPolicy
import org.andengine.entity.scene.Scene
import org.andengine.entity.scene.background.Background
import org.andengine.entity.text.Text
import org.andengine.opengl.font.Font
import org.andengine.opengl.font.FontFactory
import org.andengine.opengl.texture.TextureOptions
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas
import org.andengine.ui.activity.SimpleBaseGameActivity
import kotlin.math.max
import kotlin.math.min

class MainActivity : SimpleBaseGameActivity() {

    private var screenWidth = 360f
    private var screenHeight = 600f
    private var screenOrientation = ScreenOrientation.PORTRAIT_FIXED

    private var titleText: Text? = null
    private var font: Font? = null

    override fun onCreateResources() {
        val fontSize = 16
        val fontColor = Color.WHITE
        val fontFile = "font/Merriweather-Regular.otf"
        val borderSize = 0
        val borderColor = Color.TRANSPARENT
        val atlas =
            BitmapTextureAtlas(textureManager, 256, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA)
        font = FontFactory.createStrokeFromAsset(
            fontManager,
            atlas,
            assets,
            fontFile,
            fontSize.toFloat(),
            true,
            fontColor,
            borderSize.toFloat(),
            borderColor
        )
        font?.load()
    }

    override fun onCreateScene(): Scene {
        val scene = Scene()
        scene.background = Background(0f, 0f, 0f)

        val text = getText(R.string.app_name)
        val px = screenWidth / 2
        val py = screenHeight / 2
        titleText = Text(px, py, font, text, vertexBufferObjectManager)
        scene.attachChild(titleText)

        return scene
    }

    override fun onCreateEngineOptions(): EngineOptions {
        val metrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(metrics)
        val deviceWidth = min(metrics.heightPixels, metrics.widthPixels)
        val deviceHeight = max(metrics.heightPixels, metrics.widthPixels)

        screenHeight = deviceHeight * screenWidth / deviceWidth
        val scale = deviceWidth.toFloat() / screenWidth
        Log.i(
            "",
            "screen: " + screenWidth + "x" + screenHeight
                    + " device: " + deviceWidth + "x" + deviceHeight
                    + " scale: " + scale
        )

        val camera = ZoomCamera(0f, 0f, screenWidth, screenHeight)
        val engineOptions =
            EngineOptions(true, screenOrientation, FillResolutionPolicy(), camera)
        engineOptions.audioOptions.setNeedsMusic(true).setNeedsSound(true)
        engineOptions.renderOptions.configChooserOptions.isRequestedMultiSampling = true
        engineOptions.wakeLockOptions = WakeLockOptions.SCREEN_ON

        return engineOptions
    }
}