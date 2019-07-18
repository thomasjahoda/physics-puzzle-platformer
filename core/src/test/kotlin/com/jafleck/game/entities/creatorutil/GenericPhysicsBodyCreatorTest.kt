package com.jafleck.game.entities.creatorutil

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Box2D
import com.badlogic.gdx.physics.box2d.PolygonShape
import com.badlogic.gdx.physics.box2d.World
import com.jafleck.extensions.libgdx.math.buildVertices
import ktx.box2d.body
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

internal class GenericPhysicsBodyCreatorTest {

    @Test
    @Disabled("crashes because of assertion error in box2d JNI")
    fun `box2d crashes with vertices in one line - 1`() {
        Box2D.init()
        val vertices = buildVertices {
            p(0.0, 2.5)
            p(0.75, 2.75)
            p(1.5, 3.0)
        }.asFloatArray()
        World(Vector2(), true).body {
            polygon(vertices) {

            }
        }
    }

    @Test
    @Disabled("crashes because of assertion error in box2d JNI")
    fun `box2d crashes with vertices in one line - 2`() {
        Box2D.init()
        val vertices = buildVertices {
            p(0, 0)
            p(1, 0)
            p(2, 0)
        }.asFloatArray()
        World(Vector2(), true).body {
            polygon(vertices) {

            }
        }
    }

    @Test
//    @Disabled
    fun `box2d filters duplicate vectors automatically`() {
        Box2D.init()
        val vertices = buildVertices {
            p(0, 0)
            p(1, 1)
            p(1, 1)
            p(1, 0)
            p(0, 0)
            p(1, 1)
            p(0, 0)
            p(1, 0)
        }.asFloatArray()
        val body = World(Vector2(), true).body {
            polygon(vertices) {

            }
        }
        val polygon = body.fixtureList[0].shape as PolygonShape
        Assertions.assertThat(polygon.vertexCount).isEqualTo(3)
    }
}
