package com.jafleck.game.entities.maploading

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.maps.objects.EllipseMapObject
import com.badlogic.gdx.maps.objects.PolygonMapObject
import com.badlogic.gdx.maps.objects.RectangleMapObject
import com.badlogic.gdx.math.Vector2
import com.jafleck.extensions.kotlin.round
import com.jafleck.extensions.libgdx.math.buildVertices
import com.jafleck.extensions.libgdx.math.round
import com.jafleck.extensions.libgdx.math.toListOfVertices
import com.jafleck.extensions.libgdxktx.ashley.get
import com.jafleck.extensions.libgdxktx.ashley.has
import com.jafleck.game.components.basic.MapObjectComponent
import com.jafleck.game.components.basic.OriginPositionComponent
import com.jafleck.game.components.basic.RotationComponent
import com.jafleck.game.components.basic.VelocityComponent
import com.jafleck.game.components.shape.CircleShapeComponent
import com.jafleck.game.components.shape.PolygonShapeComponent
import com.jafleck.game.components.shape.RectangleShapeComponent
import com.jafleck.game.entities.customizations.GenericEntityCustomization
import com.jafleck.game.maploading.scaleFromWorldToMap
import com.jafleck.testutil.HeadlessLibgdxExtension
import com.jafleck.testutil.maps.LibGdxTiledMapLoader
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.util.*
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

    @Test
    fun `polygon - actualMap - triangle as easy convex example`() {
        val map = LibGdxTiledMapLoader().loadMap("polygonTest1_triangle.tmx")
        val mapObject = map.layers[0].objects[0] as PolygonMapObject

        val entity = Entity()
        val uut = MapObjectFormExtractor()
        entity.loadGeneralComponentsFrom(mapObject, EXCLUDE_MAP_OBJECT_COMPONENT, GenericEntityCustomization(), uut)

        Assertions.assertThat(entity[RotationComponent].degrees).isEqualTo(0f)
        Assertions.assertThat(entity[OriginPositionComponent].vector).isEqualTo(Vector2(1.5f, 2.5f))
        Assertions.assertThat(entity[PolygonShapeComponent].vertices.toListOfVertices()).isEqualTo(buildVertices {
            p(-0.5, -0.5)
            p(0, 0.5)
            p(0.5, -0.5)
        }.asListOfVertices())
    }

    @Test
    fun `polygon - actualMap - whatever - concave but no intersection of lines`() {
        val map = LibGdxTiledMapLoader().loadMap("polygonTest2_whatever.tmx")
        val mapObject = map.layers[0].objects[0] as PolygonMapObject

        val entity = Entity()
        val uut = MapObjectFormExtractor()
        entity.loadGeneralComponentsFrom(mapObject, EXCLUDE_MAP_OBJECT_COMPONENT, GenericEntityCustomization(), uut)

        Assertions.assertThat(entity[RotationComponent].degrees).isEqualTo(0f)
        Assertions.assertThat(entity[OriginPositionComponent].vector).isEqualTo(Vector2(3f, 2f))
        Assertions.assertThat(entity[PolygonShapeComponent].vertices.toListOfVertices()).isEqualTo(buildVertices {
            p(0.5, 1)
            p(-1.5, 1)
            p(-0.75, -0.5)
            p(0.5, 0)
            p(1.5, -1)
            p(1.5, 0)
        }.asListOfVertices())
    }

    @Test
    fun `polygon - actualMap - 90° retard-rotation map`() {
        val map = LibGdxTiledMapLoader().loadMap("polygonTest3_rotation.tmx")
        val mapObject = map.layers[0].objects[0] as PolygonMapObject

        val entity = Entity()
        val uut = MapObjectFormExtractor()
        entity.loadGeneralComponentsFrom(mapObject, EXCLUDE_MAP_OBJECT_COMPONENT, GenericEntityCustomization(), uut)

        Assertions.assertThat(entity[RotationComponent].degrees).isEqualTo(270f)  // 360° - 90°
        Assertions.assertThat(entity[OriginPositionComponent].vector).isEqualTo(Vector2(1.25f, 1.25f))
        println(Arrays.toString(entity[PolygonShapeComponent].vertices))
        Assertions.assertThat(entity[PolygonShapeComponent].vertices.round(3).toListOfVertices()).isEqualTo(buildVertices {
            p(-0.5, 1)
            p(-0.5f, -0f) // 0f and -0f is not equal with the generic object .equals
            p(0.5, -1)
            p(0.5, 0)
        }.asListOfVertices())
    }

    @Test
    fun `polygon - actualMap - 90° retard-rotation map unrotated`() {
        val map = LibGdxTiledMapLoader().loadMap("polygonTest3_rotation.tmx")
        val mapObject = map.layers[0].objects[0] as PolygonMapObject
        // overwrite rotation to compare rotated and unrotated polygons
        mapObject.properties.put("rotation", 0f)

        val entity = Entity()
        val uut = MapObjectFormExtractor()
        entity.loadGeneralComponentsFrom(mapObject, EXCLUDE_MAP_OBJECT_COMPONENT, GenericEntityCustomization(), uut)

        Assertions.assertThat(entity[RotationComponent].degrees).isEqualTo(0f)
        Assertions.assertThat(entity[OriginPositionComponent].vector).isEqualTo(Vector2(4f, 2f))
        Assertions.assertThat(entity[PolygonShapeComponent].vertices.toListOfVertices()).isEqualTo(buildVertices {
            p(-0.5, 1)
            p(-0.5, 0)
            p(0.5, -1)
            p(0.5, 0)
        }.asListOfVertices())
    }

    @Test
    fun `ellipse - actualMap - 1 to 1 unrotated ellipse is circle`() {
        val map = LibGdxTiledMapLoader().loadMap("ellipseTest1_1to1ellipse_is_circle_rotated.tmx")
        val mapObject = map.layers[0].objects[0] as EllipseMapObject
        // overwrite rotation to compare rotated and unrotated circle
        mapObject.properties.put("rotation", 0f)

        val entity = Entity()
        val uut = MapObjectFormExtractor()
        entity.loadGeneralComponentsFrom(mapObject, EXCLUDE_MAP_OBJECT_COMPONENT, GenericEntityCustomization(), uut)

        Assertions.assertThat(entity[RotationComponent].degrees).isEqualTo(0f)
        Assertions.assertThat(entity[OriginPositionComponent].vector).isEqualTo(Vector2(2.5f, 2f))
        val circleShapeComponent = entity[CircleShapeComponent]
        Assertions.assertThat(circleShapeComponent.radius).isEqualTo(0.5f)
        Assertions.assertThat(entity.has(PolygonShapeComponent)).isFalse()
    }

    @Test
    fun `ellipse - actualMap - 2 to 1 unrotated ellipse`() {
        val map = LibGdxTiledMapLoader().loadMap("ellipseTest1_2to1ratio_rotated.tmx")
        val mapObject = map.layers[0].objects[0] as EllipseMapObject
        // overwrite rotation to compare rotated and unrotated ellipse
        mapObject.properties.put("rotation", 0f)

        val entity = Entity()
        val uut = MapObjectFormExtractor()
        entity.loadGeneralComponentsFrom(mapObject, EXCLUDE_MAP_OBJECT_COMPONENT, GenericEntityCustomization(), uut)

        Assertions.assertThat(entity[RotationComponent].degrees).isEqualTo(0f)
        Assertions.assertThat(entity[OriginPositionComponent].vector).isEqualTo(Vector2(3f, 2f))
        Assertions.assertThat(entity[PolygonShapeComponent].getRectangleAroundShape(Vector2()).round(2)).isEqualTo(Vector2(2f, 1f))
    }
}
