package com.jafleck.game.gameplay.systems

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntitySystem
import com.badlogic.ashley.utils.ImmutableArray
import com.badlogic.gdx.math.Vector2
import com.jafleck.game.assets.ScreenToWorldScalingPropagator
import com.jafleck.game.families.PositionedPlayer
import com.jafleck.game.util.GameCamera


class TrackPlayerWithCameraSystem(
    priority: Int,
    private val camera: GameCamera
) : EntitySystem(priority) {

    private lateinit var entities: ImmutableArray<Entity>

    override fun addedToEngine(engine: Engine) {
        entities = engine.getEntitiesFor(PositionedPlayer.family)
        updateCamera()
    }

    override fun update(deltaSeconds: Float) {
        updateCamera()
    }

    private fun updateCamera() {
        require(entities.size() <= 1) { "Only one player may exist at one time but ${entities.size()} were found" }
        if (entities.size() == 1) {
            val entity = PositionedPlayer(entities.get(0))
            camera.position.set(entity.position.vector.cpy(), camera.position.z)
        }
    }

}
