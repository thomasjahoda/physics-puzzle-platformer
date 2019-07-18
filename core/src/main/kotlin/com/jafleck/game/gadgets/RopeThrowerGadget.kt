package com.jafleck.game.gadgets

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.Vector2
import com.jafleck.extensions.kotlin.withItIfNotNull
import com.jafleck.extensions.libgdxktx.ashley.get
import com.jafleck.extensions.libgdxktx.ashley.getOrNull
import com.jafleck.game.components.basic.OriginPositionComponent
import com.jafleck.game.components.basic.VelocityComponent
import com.jafleck.game.components.logic.ThrowerOfRopeComponent
import com.jafleck.game.entities.RopeEntityCreator
import com.jafleck.game.entities.RopePartEntity
import com.jafleck.game.entities.ThrownRopeEntity
import com.jafleck.game.families.PhysicalShapedEntity
import com.jafleck.game.families.ShapedEntity
import com.jafleck.game.util.logger
import ktx.ashley.remove
import ktx.math.div
import ktx.math.minus
import ktx.math.plus
import ktx.math.times

class RopeThrowerGadget(
    private val ropeEntityCreator: RopeEntityCreator,
    private val engine: Engine
) : MouseActivatedGadget {

    private val throwSpeed = 40f

    private val logger = logger(this::class)

    override fun activate(handler: Entity, targetPosition: Vector2) {
        if (withItIfNotNull(handler.getOrNull(ThrowerOfRopeComponent)) {
                delete(it.thrownRope)
            }) {
        } else {
            throwRope(handler, targetPosition)
        }
    }

    private fun delete(thrownRopeEntity: ThrownRopeEntity) {
        thrownRopeEntity.rope.parts.forEach {
            engine.removeEntity(it)
        }
        engine.removeEntity(thrownRopeEntity.entity)
        thrownRopeEntity.thrownRope.thrownBy.entity.remove<ThrowerOfRopeComponent>()
    }

    private fun throwRope(handler: Entity, targetPosition: Vector2) {
        val shapedEntity = ShapedEntity(handler)
        val entityRectangleSize = shapedEntity.shape.getRectangleAroundShape(Vector2())
        val entityPosition = handler[OriginPositionComponent].vector

        val throwDirection = (targetPosition - entityPosition).nor()
        val velocity = (throwDirection * throwSpeed) + (handler.getOrNull(VelocityComponent)?.vector ?: Vector2.Zero)
        val spawnPosition = entityPosition + throwDirection * (entityRectangleSize / 2 + RopePartEntity.ROPE_PART_SIZE / 2)

        logger.debug { "Spawning ball at $spawnPosition with velocity $velocity" }
        ropeEntityCreator.createRopeByBeingThrown(PhysicalShapedEntity(handler), Vector2(), spawnPosition, velocity).apply {
        }
    }

}
