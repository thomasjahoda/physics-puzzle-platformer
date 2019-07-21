package com.jafleck.game.maploading

import com.jafleck.testutil.CustomClasspathAssetsFileHandleResolver
import com.jafleck.testutil.HeadlessLibgdxExtension
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(HeadlessLibgdxExtension::class)
internal class MapListTest {

    @Test
    fun getMaps() {
        val uut = MapList(CustomClasspathAssetsFileHandleResolver())
        Assertions.assertThat(uut.maps.size).isNotEqualTo(0)
        Assertions.assertThat(uut.maps[0].name).isNotNull()
        Assertions.assertThat(uut.maps[0].path).isNotNull()
    }
}
