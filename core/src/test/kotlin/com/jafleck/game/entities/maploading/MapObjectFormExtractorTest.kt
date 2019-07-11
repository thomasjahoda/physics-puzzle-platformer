package com.jafleck.game.entities.maploading

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.maps.objects.EllipseMapObject
import com.badlogic.gdx.maps.objects.RectangleMapObject
import com.badlogic.gdx.math.Vector2
import com.jafleck.extensions.libgdxktx.ashley.get
import com.jafleck.game.components.MapObjectComponent
import com.jafleck.game.components.OriginPositionComponent
import com.jafleck.game.components.RotationComponent
import com.jafleck.game.components.VelocityComponent
import com.jafleck.game.components.shape.CircleShapeComponent
import com.jafleck.game.components.shape.RectangleShapeComponent
import com.jafleck.game.entities.customizations.GenericEntityCustomization
import com.jafleck.game.maploading.scaleFromWorldToMap
import com.jafleck.testutil.HeadlessLibgdxExtension
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.math.hypot

@ExtendWith(HeadlessLibgdxExtension::class)
internal class MapObjectFormExtractorTest {

    companion object {
        private val ALL_OPTIONS = GenericEntityConfig(
            rotates = true,
            moves = true,
            trackMapObject = true
        )
        private val EXCLUDE_MAP_OBJECT_COMPONENT = GenericEntityConfig(
            rotates = true,
            moves = true,
            trackMapObject = false
        )
    }

    @Test
    fun `simple rectangle - should initialize components with default values`() {
        val mapObject = RectangleMapObject(10f.scaleFromWorldToMap(), 20f.scaleFromWorldToMap(), 4f.scaleFromWorldToMap(), 2f.scaleFromWorldToMap())
        mapObject.properties.put("id", 1)

        val entity = Entity()
        val uut = MapObjectFormExtractor()
        entity.loadGeneralComponentsFrom(mapObject, ALL_OPTIONS, GenericEntityCustomization(), uut)

        Assertions.assertThat(entity[OriginPositionComponent].vector).isEqualTo(Vector2(10f + (4f / 2), 20f + (2f / 2)))
        Assertions.assertThat(entity[RectangleShapeComponent].vector).isEqualTo(Vector2(4f, 2f))
        Assertions.assertThat(entity[VelocityComponent].vector).isEqualTo(Vector2(0f, 0f))
        Assertions.assertThat(entity[RotationComponent].degrees).isEqualTo(0f)
        Assertions.assertThat(entity[MapObjectComponent].value).isSameAs(mapObject)
        Assertions.assertThat(entity.components.size()).isEqualTo(5)
    }

    @Test
    fun `rotated rectangle - -45° retard-rotation should be converted to 45° standard-rotation`() {
        val mapObject = RectangleMapObject(10f.scaleFromWorldToMap(), 20f.scaleFromWorldToMap(), 4f.scaleFromWorldToMap(), 4f.scaleFromWorldToMap())
        mapObject.properties.put("id", 1)
        mapObject.properties.put("rotation", -45f)

        val entity = Entity()
        val uut = MapObjectFormExtractor()
        entity.loadGeneralComponentsFrom(mapObject, EXCLUDE_MAP_OBJECT_COMPONENT, GenericEntityCustomization(), uut)

        // related map is saved in rotatedPlatformVisualHelp.tmx with a lot of documentation if you need help to cross-check values.
        // Important: Tiled saves rectangles position from the top-left position but the LibGDX loader already converts this to bottom-left
        Assertions.assertThat(entity[RotationComponent].degrees).isEqualTo(45f)
        Assertions.assertThat(entity[OriginPositionComponent].vector).isEqualTo(Vector2(10f + (hypot(4f, 4f) / 2), 20f + 4f))
        Assertions.assertThat(entity[RectangleShapeComponent].vector).isEqualTo(Vector2(4f, 4f))
        Assertions.assertThat(entity[VelocityComponent].vector).isEqualTo(Vector2(0f, 0f))
        Assertions.assertThat(entity.components.size()).isEqualTo(4)
    }

    @Test
    fun `rotated rectangle - 315° retard-rotation should be converted to 45° standard-rotation`() {
        val mapObject = RectangleMapObject(10f.scaleFromWorldToMap(), 20f.scaleFromWorldToMap(), 4f.scaleFromWorldToMap(), 4f.scaleFromWorldToMap())
        mapObject.properties.put("id", 1)
        mapObject.properties.put("rotation", 315f)

        val entity = Entity()
        val uut = MapObjectFormExtractor()
        entity.loadGeneralComponentsFrom(mapObject, EXCLUDE_MAP_OBJECT_COMPONENT, GenericEntityCustomization(), uut)

        // related map is saved in rotatedPlatformVisualHelp.tmx with a lot of documentation if you need help to cross-check values.
        // Important: Tiled saves rectangles position from the top-left position but the LibGDX loader already converts this to bottom-left
        Assertions.assertThat(entity[RotationComponent].degrees).isEqualTo(45f)
        Assertions.assertThat(entity[OriginPositionComponent].vector).isEqualTo(Vector2(10f + (hypot(4f, 4f) / 2), 20f + 4f))
        Assertions.assertThat(entity[RectangleShapeComponent].vector).isEqualTo(Vector2(4f, 4f))
        Assertions.assertThat(entity[VelocityComponent].vector).isEqualTo(Vector2(0f, 0f))
        Assertions.assertThat(entity.components.size()).isEqualTo(4)
    }

    @Test
    fun `circle as ellipse - convert`() {
        val mapObject = EllipseMapObject(10f.scaleFromWorldToMap(), 20f.scaleFromWorldToMap(), 4f.scaleFromWorldToMap(), 4f.scaleFromWorldToMap())
        mapObject.properties.put("id", 1)

        val entity = Entity()
        val uut = MapObjectFormExtractor()
        entity.loadGeneralComponentsFrom(mapObject, EXCLUDE_MAP_OBJECT_COMPONENT, GenericEntityCustomization(), uut)

        Assertions.assertThat(entity[RotationComponent].degrees).isEqualTo(0f)
        // libgdx has in fact NOT already converted to an origin position
        Assertions.assertThat(entity[OriginPositionComponent].vector).isEqualTo(Vector2(10f + 4f / 2, 20f + 4f / 2))
        Assertions.assertThat(entity[CircleShapeComponent].radius).isEqualTo(4f / 2)
        Assertions.assertThat(entity[VelocityComponent].vector).isEqualTo(Vector2(0f, 0f))
        Assertions.assertThat(entity.components.size()).isEqualTo(4)
    }
}
