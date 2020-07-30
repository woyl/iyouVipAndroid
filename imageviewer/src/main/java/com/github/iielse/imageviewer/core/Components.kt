package com.github.iielse.imageviewer.core

import android.view.View
import android.view.ViewGroup
import com.github.iielse.imageviewer.ImageViewerBuilder
import com.github.iielse.imageviewer.utils.log
import kotlin.IllegalStateException

object Components {
    private var initialize = false
    val working get() = initialize
    private var imageLoader: ImageLoader? = null
    private var dataProvider: DataProvider? = null
    private var transformer: Transformer? = null
    private var initKey: Long? = null
    private var vhCustomizer: VHCustomizer? = null
    private var overlayCustomizer: OverlayCustomizer? = null
    private var viewerCallback: ViewerCallback? = null
    private var num :Int?= null

    fun initialize(imageLoader: ImageLoader, dataProvider: DataProvider, transformer: Transformer, initKey: Long,all:Int) {
        log { "Components initialize" }
        if (initialize) throw IllegalStateException()
        Components.imageLoader = imageLoader
        Components.dataProvider = dataProvider
        Components.transformer = transformer
        Components.initKey = initKey
        Components.num = all
        initialize = true

    }

    fun setVHCustomizer(vhCustomizer: VHCustomizer?) {
        Components.vhCustomizer = vhCustomizer
    }

    fun setViewerCallback(viewerCallback: ViewerCallback?) {
        Components.viewerCallback = viewerCallback
    }

    fun setOverlayCustomizer(overlayCustomizer: OverlayCustomizer?) {
        Components.overlayCustomizer = overlayCustomizer
    }


    fun requireImageLoader() = imageLoader ?: object : ImageLoader {}
    fun requireDataProvider() = dataProvider ?: object : DataProvider {}
    fun requireTransformer() = transformer ?: object : Transformer {}
    fun requireInitKey() = initKey ?: 0
    fun requireVHCustomizer() = vhCustomizer ?: object : VHCustomizer {}
    fun requireViewerCallback() = viewerCallback ?: object : ViewerCallback {}
    fun requireOverlayCustomizer() = overlayCustomizer ?: object : OverlayCustomizer {}

    fun reAll() = num

    fun release() {
        log { "Components release" }
        initialize = false
        imageLoader = null
        dataProvider = null
        transformer = null
        initKey = null
        vhCustomizer = null
        viewerCallback = null
        overlayCustomizer = null
    }
}