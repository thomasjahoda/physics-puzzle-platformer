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
import com.jafleck.game.entities.creatorutil.GenericPhysicsBodyCreator
import com.jafleck.game.entities.maploading.GenericEntityConfig
import com.jafleck.game.entities.maploading.MapObjectFormExtractor
import com.jafleck.game.entities.maploading.loadFrom
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
        const val DENSITY = 2f
        const val FRICTION = 0.2f
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
    private val mapObjectFormExtractor: MapObjectFormExtractor,
    private val genericPhysicsBodyCreator: GenericPhysicsBodyCreator,
    private val initialGadget: Gadget
): MapEntityLoader {
    companion object {
        private val ENTITY_CONFIG = GenericEntityConfig(
            rotates = true,
            moves = true
        )
    }

    override val type: String
        get() = "Player"

    override fun loadEntity(mapObject: MapObject): Entity {
        return engine.createEntity().apply {
            loadFrom(mapObject, ENTITY_CONFIG, mapObjectFormExtractor)
            genericPhysicsBodyCreator.createDynamicBody(this) {
                density = PlayerEntity.DENSITY
                friction = PlayerEntity.FRICTION
            }
            add(VisualShapeComponent(
                borderColor = Color.BLACK, borderThickness = 0.04f,
                fillColor = Color.FIREBRICK.cpy().mul(0.9f)))
            add(PlayerComponent())
            add(SelectedGadgetComponent(initialGadget))
            engine.addEntity(this)
        }
    }

}

val playerModule = module {
    single { PlayerEntityCreator(get(), get(), get(), get(), get<BallThrowerGadget>()) }
}
