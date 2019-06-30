package com.jafleck.extensions.libgdxktx.ashley

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

internal class DummyComponentA : Component {
    companion object : ComponentMapperAccessor<DummyComponentA>(DummyComponentA::class)

    var value: String = ""
}

internal class DummyComponentB : Component {
    companion object : ComponentMapperAccessor<DummyComponentB>(DummyComponentB::class)

    var value: String = ""
}

internal class ComponentMapperAccessorTest {

    @Test
    fun `get - success on single component`() {
        val entity = Entity().apply {
            add(DummyComponentA().apply {
                value = "some value"
            })
        }
        val dummyComponentA = entity[DummyComponentA]
        Assertions.assertThat(dummyComponentA).isNotNull
        Assertions.assertThat(dummyComponentA.value).isEqualTo("some value")
    }

    @Test
    fun `get - success with multiple components`() {
        val entity = Entity().apply {
            add(DummyComponentA().apply {
                value = "some value"
            })
            add(DummyComponentB().apply {
                value = "some other value"
            })
        }
        val dummyComponentA = entity[DummyComponentA]
        Assertions.assertThat(dummyComponentA.value).isEqualTo("some value")
        val dummyComponentB = entity[DummyComponentB]
        Assertions.assertThat(dummyComponentB.value).isEqualTo("some other value")
    }

    @Test
    fun `get - fail because component does not exist`() {
        val entity = Entity().apply {
            add(DummyComponentB())
        }
        Assertions.assertThatThrownBy { entity[DummyComponentA] }
            .isExactlyInstanceOf(IllegalStateException::class.java)
            .hasMessage("componentMapper[entity] must not be null")
    }

    @Test
    fun `contains - true (use overloaded 'in' operator)`() {
        val entity = Entity().apply {
            add(DummyComponentA().apply {
                value = "some value"
            })
        }
        val containsComponentA = DummyComponentA in entity
        Assertions.assertThat(containsComponentA).isTrue()
    }

    @Test
    fun `contains - true (use isIn function directly)`() {
        val entity = Entity().apply {
            add(DummyComponentA().apply {
                value = "some value"
            })
        }
        val containsComponentA = DummyComponentA.isIn(entity)
        Assertions.assertThat(containsComponentA).isTrue()
    }

    @Test
    fun `contains - false`() {
        val entity = Entity().apply {
            add(DummyComponentA().apply {
                value = "some value"
            })
        }
        val containsComponentB = DummyComponentB in entity
        Assertions.assertThat(containsComponentB).isFalse()
    }
}
