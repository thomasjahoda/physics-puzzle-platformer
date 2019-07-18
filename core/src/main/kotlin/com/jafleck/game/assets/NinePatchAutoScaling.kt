package com.jafleck.game.assets

import com.badlogic.gdx.graphics.g2d.NinePatch
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable
import com.jafleck.game.util.logger
import ktx.math.div
import kotlin.math.max

private val moduleLogger = logger("NinePatchAutoScaling")


fun ScreenToWorldScalingPropagator.autoScaleNinePatch(ninePatch: NinePatch,
                                                      initialScaling: Vector2) {
    moduleLogger.debug { "Auto-scaling nine-patch" }
    moduleLogger.debug { "- Initial middle width: ${ninePatch.middleWidth}" }

    ninePatch.scale(initialScaling.x, initialScaling.y)
    moduleLogger.debug { "- Middle width after scaling with initialScaling ($initialScaling): ${ninePatch.middleWidth}" }

    ninePatch.scale(scaling.x, scaling.y)
    moduleLogger.debug { "- Middle width after applying current scale: ${ninePatch.middleWidth}" }

    registerObserver { oldScaling, newScaling ->
        val oldScalar = keepAspectWhenScaling(oldScaling)
        val newScalar = keepAspectWhenScaling(newScaling)
        moduleLogger.debug { "New Scaling for nine-patch: $newScaling, converted to scalar $newScalar" }
        moduleLogger.debug { "- middle width before change: ${ninePatch.middleWidth}" }

        // revert old scaling
        ninePatch.scale(1f / oldScalar, 1f / oldScalar)
        moduleLogger.debug { "- middle with after reverting old scaling: ${ninePatch.middleWidth}" }

        // apply new scaling
        ninePatch.scale(newScalar, newScalar)
        moduleLogger.debug { "- middle with after applying old scaling: ${ninePatch.middleWidth}" }
    }
}

private fun keepAspectWhenScaling(oldScaling: Vector2) = max(oldScaling.x, oldScaling.y)

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
