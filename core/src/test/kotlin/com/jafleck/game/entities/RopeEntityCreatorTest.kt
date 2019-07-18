package com.jafleck.game.entities

import com.badlogic.gdx.math.Vector2
import com.jafleck.extensions.libgdx.math.round
import com.jafleck.testutil.HeadlessLibgdxExtension
import com.jafleck.testutil.integration.IntegrationTestApplicationExtension
import com.jafleck.testutil.junit.assertAll
import ktx.math.minus
import ktx.math.times
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.extension.RegisterExtension

@ExtendWith(HeadlessLibgdxExtension::class)
internal class RopeEntityCreatorTest {

    companion object {
        @JvmField
        @RegisterExtension
        val app = IntegrationTestApplicationExtension(
            loadedMap = "circle_player_and_walls.tmx"
        )
    }

    @Test
    fun `creating thrown rope - rotates it into the direction of the velocity`() {
        val player = app.player
        val thrownRopePosition = player.position.vector.cpy().add(1f, 1f)
        val velocity = Vector2(2f, 2f)
        val uut = RopeEntityCreator(app.engine, app.genericPhysicsBodyCreator)

        val thrownRope = uut.createRopeByBeingThrown(player.asPhysicalShapedEntity(), Vector2(), thrownRopePosition, velocity)

        Assertions.assertThat(thrownRope.rope.parts.size).isEqualTo(1)
        val firstRopePart = RopePartEntity(thrownRope.rope.parts[0])
        assertAll {
            assert { Assertions.assertThat(firstRopePart.stickyRopePart).isNotNull }
            assert { Assertions.assertThat(firstRopePart.stickyRopePart!!.anchored).isEqualTo(false) }
            assert { Assertions.assertThat(firstRopePart.velocity.vector).isEqualTo(velocity) }
            assert { Assertions.assertThat(firstRopePart.asPhysicalShapedEntity().position.vector).isEqualTo(thrownRopePosition) }
            assert { Assertions.assertThat(firstRopePart.asPhysicalShapedEntity().asShapedEntity().rectangleShape!!.vector).isEqualTo(RopePartEntity.ROPE_PART_SIZE) }
            assert { Assertions.assertThat(firstRopePart.asPhysicalShapedEntity().asShapedEntity().rotation!!.degrees).isEqualTo(45f) }
            assert { Assertions.assertThat(firstRopePart.ropePart.owningRopeEntity.entity).isEqualTo(thrownRope.entity) }
        }
    }

    @Test
    fun `attaching normal part to thrown rope - should attach between thrower and current first part, but attached to first part`() {
        val player = app.player
        val thrownRopePosition = player.position.vector.cpy().add(1f, 1f)
        val velocity = Vector2(1f, 1f)
        val uut = RopeEntityCreator(app.engine, app.genericPhysicsBodyCreator)

        // prepare thrown rope
        val thrownRope = uut.createRopeByBeingThrown(player.asPhysicalShapedEntity(), Vector2(), thrownRopePosition, velocity)
        val ropePartToAttachTo = RopePartEntity(thrownRope.rope.parts[0])

        // test attaching new normal part
        val newRopePart = uut.createNormalRopePartAttachedTo(player.position.vector, ropePartToAttachTo, velocity)

        val expectedNewRopePartOriginPosition = thrownRopePosition - ((thrownRopePosition - player.position.vector).nor() * RopePartEntity.ROPE_PART_SIZE.y)
        assertAll {
            assert { Assertions.assertThat(thrownRope.rope.parts.size).isEqualTo(2) }
            assert { Assertions.assertThat(newRopePart.entity).isSameAs(thrownRope.rope.parts[0]) }
            assert { Assertions.assertThat(newRopePart.stickyRopePart).isNull() }
            assert { Assertions.assertThat(newRopePart.velocity.vector).isEqualTo(velocity) }
            assert { Assertions.assertThat(newRopePart.asPhysicalShapedEntity().position.vector.cpy().round(1)).isEqualTo(expectedNewRopePartOriginPosition.cpy().round(1)) }
            assert { Assertions.assertThat(newRopePart.asPhysicalShapedEntity().asShapedEntity().rectangleShape!!.vector).isEqualTo(RopePartEntity.ROPE_PART_SIZE) }
            assert { Assertions.assertThat(newRopePart.asPhysicalShapedEntity().asShapedEntity().rotation!!.degrees).isEqualTo(45f) }
            assert { Assertions.assertThat(newRopePart.ropePart.owningRopeEntity.entity).isEqualTo(thrownRope.entity) }
        }
    }
}
