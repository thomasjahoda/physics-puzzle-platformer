package com.jafleck.assumptions

import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.jafleck.extensions.libgdx.math.round
import com.jafleck.testutil.HeadlessLibgdxExtension
import com.jafleck.testutil.integration.IntegrationTestApplicationExtension
import com.jafleck.testutil.junit.assertAll
import ktx.box2d.body
import ktx.math.plus
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.extension.RegisterExtension

@ExtendWith(HeadlessLibgdxExtension::class)
internal class Box2DWorldAssumptionsTest {

    companion object {
        @JvmField
        @RegisterExtension
        val app = IntegrationTestApplicationExtension(
            loadedMap = "circle_player_and_walls.tmx"
        )
    }

    @Test
    fun `getWorldPoint - on unrotated body - should just be relative to the bodies origin position`() {
        val world = app.world
        val body = world.body {
            box(1f, 1f)
            position.set(3f, 3f)
        }
        assertAll {
            assert { Assertions.assertThat(body.getWorldPoint(Vector2())).isEqualTo(Vector2(3f, 3f)) }
            assert { Assertions.assertThat(body.getWorldPoint(Vector2(1f, 2f))).isEqualTo(Vector2(4f, 5f)) }
            assert { Assertions.assertThat(body.getWorldPoint(Vector2(-1f, -2f))).isEqualTo(Vector2(2f, 1f)) }
        }
    }

    @Test
    fun `getWorldPoint - on rotated body - should get world points based on rotation`() {
        val world = app.world
        val body = world.body {
            box(1f, 1f)
            position.set(3f, 3f)
            angle = 45f * MathUtils.degreesToRadians
        }
        assertAll {
            assert { Assertions.assertThat(body.getWorldPoint(Vector2())).isEqualTo(Vector2(3f, 3f)) }
            assert {
                val localPoint = Vector2(1f, 2f)
                Assertions.assertThat(body.getWorldPoint(localPoint))
                    .isEqualTo(body.position + localPoint.cpy().rotate(45f))
            }
            assert {
                val localPoint = Vector2(-1f, -2f)
                Assertions.assertThat(body.getWorldPoint(localPoint))
                    .isEqualTo(body.position + localPoint.cpy().rotate(45f))
            }
            assert {
                val localPoint = Vector2(0f, -2f)
                Assertions.assertThat(body.getWorldPoint(localPoint))
                    .isEqualTo(body.position + localPoint.cpy().rotate(45f))
            }
            assert { Assertions.assertThat(Vector2(1f, 0f).rotate(90f).round(3)).isEqualTo(Vector2(-0f, 1f)) }
        }
    }
}
