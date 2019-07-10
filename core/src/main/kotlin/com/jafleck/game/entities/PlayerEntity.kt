package com.jafleck.game.entities

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.maps.MapObject
import com.badlogic.gdx.maps.objects.RectangleMapObject
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.scenes.scene2d.utils.Drawable
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.jafleck.extensions.libgdxktx.ashley.get
import com.jafleck.game.assets.Assets
import com.jafleck.game.components.*
import com.jafleck.game.components.shape.RectangleShapeComponent
import com.jafleck.game.families.DrawableRectangle
import com.jafleck.game.families.MovingBody
import com.jafleck.game.gadgets.BallThrowerGadget
import com.jafleck.game.gadgets.Gadget
import com.jafleck.game.maploading.MapEntityLoader
import com.jafleck.game.maploading.getRectangleWorldCoordinates
import ktx.box2d.body
import org.koin.dsl.module

inline class PlayerEntity(val entity: Entity) {

    companion object {
        val SIZE = Vector2(1f, 1f)
        val HALF_SIZE: Vector2 = SIZE.cpy().scl(0.5f)
        const val DENSITY = 2f
        const val FRICTION = 0.2f
        val COLOR: Color = Color.RED

        fun isPlayer(entity: Entity): Boolean {
            return PlayerComponent.isIn(entity)
        }
    }

    fun asDrawableRectangle() = DrawableRectangle(entity)
    fun asMovingBody() = MovingBody(entity)

    val position
        get() = entity[OriginPositionComponent]
    val size
        get() = entity[RectangleShapeComponent]
    val drawableVisual
        get() = entity[DrawableVisualComponent]
    val player
        get() = entity[PlayerComponent]
    val body
        get() = entity[BodyComponent]
    val selectedGadget
        get() = entity[SelectedGadgetComponent]
}

class PlayerEntityCreator(
    private val engine: Engine,
    private val world: World,
    private val initialGadget: Gadget
) {
    fun createPlayerEntity(
        originPosition: Vector2
    ): PlayerEntity {
        val entity = engine.createEntity().apply {
            add(OriginPositionComponent(originPosition))
            add(RectangleShapeComponent(PlayerEntity.SIZE))
            add(RectangleBoundsComponent(PlayerEntity.SIZE))
            add(VelocityComponent(0f, 0f))
            add(RotationComponent(0f))
            add(VisualShapeComponent(
                borderColor = Color.BLACK, borderThickness = PlayerEntity.HALF_SIZE.x * 0.1f,
                fillColor = Color.FIREBRICK.cpy().mul(0.9f)))
            add(BodyComponent(world.body {
                type = BodyDef.BodyType.DynamicBody
                box(PlayerEntity.SIZE.x, PlayerEntity.SIZE.y) {
                    density = PlayerEntity.DENSITY
                    friction = PlayerEntity.FRICTION
                }
                this.position.set(originPosition)
            }))
            add(PlayerComponent())
            add(SelectedGadgetComponent(initialGadget))
        }
        engine.addEntity(entity)
        return PlayerEntity(entity)
    }
}

class PlayerEntityMapObjectLoader(
    private val playerEntityCreator: PlayerEntityCreator
) : MapEntityLoader {
    override val type: String
        get() = "PlayerSpawn"

    override fun loadEntity(mapObject: MapObject): Entity {
        require(mapObject is RectangleMapObject)
        return playerEntityCreator.createPlayerEntity(getRectangleWorldCoordinates(mapObject).getCenter(Vector2())).entity
    }
}

val playerModule = module {
    single { PlayerEntityCreator(get(), get(), get(BallThrowerGadget::class, null, null)) }
    single { PlayerEntityMapObjectLoader(get()) }
}
