package com.jafleck.game.assets

import com.badlogic.gdx.graphics.g2d.NinePatch
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable
import com.jafleck.game.util.logger
import ktx.math.div
import kotlin.math.max

private val moduleLogger = logger("NinePatchAutoScaling")

// TODO refactor to be like GameFontAutoScaling.kt

fun ScreenToWorldScalingPropagator.autoScaleNinePatch(ninePatch: NinePatch,
                                                      initialScaling: Vector2) {
    moduleLogger.debug { "Auto-scaling nine-patch" }
    moduleLogger.debug { "- Initial middle width: ${ninePatch.middleWidth}" }

    ninePatch.scale(initialScaling.x, initialScaling.y)
    moduleLogger.debug { "- Middle width after scaling with initialScaling ($initialScaling): ${ninePatch.middleWidth}" }

    ninePatch.scale(worldToScreenScalingFactor.x, worldToScreenScalingFactor.y)
    moduleLogger.debug { "- Middle width after applying current scale: ${ninePatch.middleWidth}" }

    worldToScreenScalingFactorListeners.addListener { newWorldToScreenScaling, oldWorldToScreenScaling, _ ->
        require(newWorldToScreenScaling.x == newWorldToScreenScaling.y)
        val oldScreenToWorldScaling = 1f / oldWorldToScreenScaling.x
        val newScreenToWorldScaling = 1f / newWorldToScreenScaling.x
        moduleLogger.debug { "New Scaling for nine-patch: $newScreenToWorldScaling" }
        moduleLogger.debug { "- middle width before change: ${ninePatch.middleWidth}" }

        // revert old scaling
        ninePatch.scale(1f / oldScreenToWorldScaling, 1f / oldScreenToWorldScaling)
        moduleLogger.debug { "- middle with after reverting old scaling: ${ninePatch.middleWidth}" }

        // apply new scaling
        ninePatch.scale(newScreenToWorldScaling, newScreenToWorldScaling)
        moduleLogger.debug { "- middle with after applying old scaling: ${ninePatch.middleWidth}" }
    }
}

fun NinePatchDrawable.autoScale(screenToWorldScalingPropagator: ScreenToWorldScalingPropagator,
                                initialScaling: Vector2) {
    screenToWorldScalingPropagator.autoScaleNinePatch(this.patch, initialScaling)
}

fun NinePatchDrawable.autoScaleByExpectedWorldSize(screenToWorldScalingPropagator: ScreenToWorldScalingPropagator,
                                                   expectedWorldSize: Vector2) {
    val minScreenSize = Vector2(max(this.patch.leftWidth + this.patch.rightWidth, 1f), max(this.patch.topHeight + this.patch.bottomHeight, 1f))
    val initialScaling = expectedWorldSize.cpy().div(max(minScreenSize.x, minScreenSize.y))
    screenToWorldScalingPropagator.autoScaleNinePatch(this.patch, initialScaling)
}
