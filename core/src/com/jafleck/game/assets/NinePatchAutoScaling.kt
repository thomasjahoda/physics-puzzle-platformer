package com.jafleck.game.assets

import com.badlogic.gdx.graphics.g2d.NinePatch
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable
import com.badlogic.gdx.utils.Logger
import com.jafleck.game.config.LoggingConfig
import com.jafleck.game.util.asGdxLoggingLevel
import kotlin.math.max

private val logger = Logger("NinePatchAutoScaling", LoggingConfig.gameLoggingLevel.asGdxLoggingLevel())

// some default scaling is necessary as even one pixel is already too big for most applications
val defaultNinePatchScaling = Vector2(0.3f, 0.3f)

fun ScreenToWorldScalingPropagator.autoScaleNinePatch(ninePatch: NinePatch, initialScaling: Vector2 = defaultNinePatchScaling) {
    // TODO #10: this is a work-in-progress, I don't know what a good solution would be and some trial-and-error lead me to this.
    //  The camera scaleX and scaleY itself might already be wrong for this use-case. Some other value should maybe be used instead.
    logger.debug("Auto-scaling nine-patch")
    logger.debug("- Initial middle width: ${ninePatch.middleWidth}")

    ninePatch.scale(initialScaling.x, initialScaling.y)
    logger.debug("- Middle width after scaling with initialScaling ($initialScaling): ${ninePatch.middleWidth}")

    // maybe try something in relation to the nine-patch size instead? (but then remove initialScaling param)
    //    ninePatch.scale(1 / ninePatch.totalWidth, 1 / ninePatch.totalHeight)

    ninePatch.scale(scaling.x, scaling.y)
    logger.debug("- Middle width after applying current scale: ${ninePatch.middleWidth}")

    registerObserver { oldScaling, newScaling ->
        val oldScalar = keepAspectWhenScaling(oldScaling)
        val newScalar = keepAspectWhenScaling(newScaling)
        logger.debug("New Scaling for nine-patch: $newScaling, converted to scalar $newScalar")
        logger.debug("- middle width before change: ${ninePatch.middleWidth}")

        // revert old scaling
        ninePatch.scale(1f / oldScalar, 1f / oldScalar)
        logger.debug("- middle with after reverting old scaling: ${ninePatch.middleWidth}")

        // apply new scaling
        ninePatch.scale(newScalar, newScalar)
        logger.debug("- middle with after applying old scaling: ${ninePatch.middleWidth}")
    }
}

private fun keepAspectWhenScaling(oldScaling: Vector2) = max(oldScaling.x, oldScaling.y)

fun NinePatchDrawable.autoScale(screenToWorldScalingPropagator: ScreenToWorldScalingPropagator) {
    screenToWorldScalingPropagator.autoScaleNinePatch(this.patch)
}
