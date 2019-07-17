package com.jafleck.testutil

import org.junit.jupiter.api.extension.AfterAllCallback
import org.junit.jupiter.api.extension.BeforeAllCallback
import org.junit.jupiter.api.extension.ExtensionContext


class HeadlessLibgdxAndDummyUiSkinExtension : BeforeAllCallback, AfterAllCallback {

    private val headlessLibgdxExtension = HeadlessLibgdxExtension()
    private val uiSkinExtension = DummyUiSkinExtension()

    override fun beforeAll(context: ExtensionContext?) {
        headlessLibgdxExtension.beforeAll(context)
        uiSkinExtension.beforeAll(context)
    }

    override fun afterAll(context: ExtensionContext?) {
        uiSkinExtension.afterAll(context)
        headlessLibgdxExtension.afterAll(context)
    }

}
