package com.jafleck.game.gameplay.systems.visual

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntitySystem
import com.badlogic.ashley.utils.ImmutableArray
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Align
import com.jafleck.game.families.VisualTextEntity
import com.jafleck.game.gameplay.ui.GameViewport
import com.jafleck.game.gameplay.ui.UiCamera
import com.jafleck.game.util.logger


class TextRenderSystem(
    private val gameViewport: GameViewport,
    private val uiCamera: UiCamera
) : EntitySystem() {

    private lateinit var entities: ImmutableArray<Entity>

    private val logger = logger(this::class)

    private val spriteBatch = SpriteBatch()
    private val tmpScreenPosition = Vector2()
    private val tmpRectangleSize = Vector2()

    override fun addedToEngine(engine: Engine) {
        entities = engine.getEntitiesFor(VisualTextEntity.family)
    }

    override fun removedFromEngine(engine: Engine) {
    }

    override fun update(deltaSeconds: Float) {
        Gdx.gl20.glEnable(GL20.GL_BLEND) // enable alpha in color having an effect (default blending equation: https://stackoverflow.com/a/38555868/1218254)
        spriteBatch.projectionMatrix = uiCamera.combined

        spriteBatch.begin()
        for (untypedEntity in entities) {
            val entity = VisualTextEntity(untypedEntity)
            val position = entity.position
            val visualText = entity.visualText
            // TODO support rotation in fonts
//            val rotationDegrees = entity.rotation?.degrees ?: 0f
            val rectangleSize = entity.asShapedEntity().shape.getRectangleAroundShape(Vector2())

            val gameFont = visualText.gameFont
            val text = visualText.text

            gameFont.bitmapFont.color = visualText.color
            val entityScreenWidth = gameViewport.project(tmpRectangleSize.set(rectangleSize)).x
            val screenPosition = gameViewport.project(tmpScreenPosition.set(position.vector))
            screenPosition.x -= entityScreenWidth / 2
            screenPosition.y += gameFont.currentPixelFontSize / 2 // '+=' instead of '-=' because of screen coordinates!
            gameFont.bitmapFont.draw(spriteBatch, text, screenPosition.x, screenPosition.y, entityScreenWidth, Align.center, false)
        }
        spriteBatch.end()
    }


}
