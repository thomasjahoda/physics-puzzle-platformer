package com.jafleck.extensions.kotlin

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

internal class RoundKtTest {

    @Test
    fun `float - rounding up 8`() {
        Assertions.assertThat(10.328f.round(2)).isEqualTo(10.33f)
    }

    @Test
    fun `float - rounding 5 towards nearest even integer - up`() {
        Assertions.assertThat(10.335f.round(2)).isEqualTo(10.34f)
    }

    @Test
    fun `float - rounding 5 towards nearest even integer - down`() {
        Assertions.assertThat(10.325f.round(2)).isEqualTo(10.32f)
    }

    @Test
    fun `float - rounding down 4`() {
        Assertions.assertThat(10.324f.round(2)).isEqualTo(10.32f)
    }

    @Test
    fun `float - no rounding`() {
        Assertions.assertThat(10f.round(0)).isEqualTo(10f)
    }

    @Test
    fun `double - rounding up 8`() {
        Assertions.assertThat(10.328.round(2)).isEqualTo(10.33)
    }

    @Test
    fun `double - rounding 5 towards nearest even integer - up`() {
        Assertions.assertThat(10.335.round(2)).isEqualTo(10.34)
    }

    @Test
    fun `double - rounding 5 towards nearest even integer - down`() {
        Assertions.assertThat(10.325.round(2)).isEqualTo(10.32)
    }

    @Test
    fun `double - rounding down 4`() {
        Assertions.assertThat(10.324.round(2)).isEqualTo(10.32)
    }

    @Test
    fun `double - no rounding`() {
        Assertions.assertThat(10.0.round(0)).isEqualTo(10.0)
    }
}
