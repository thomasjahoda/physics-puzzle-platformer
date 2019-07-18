package com.jafleck.testutil.junit

import org.junit.platform.commons.util.BlacklistedExceptions
import org.opentest4j.MultipleFailuresError

fun assertAll(block: AssertAllBuilder.() -> Unit) {
    val assertAllBuilder = AssertAllBuilder()
    assertAllBuilder.block()
    assertAllBuilder.executeAssertions()
}

class AssertAllBuilder {
    private val assertions = mutableListOf<() -> Unit>()
    var printExceptionsOnFailure = true

    fun assert(block: () -> Unit) {
        assertions.add(block)
    }

    internal fun executeAssertions() {
        var hasFailure = false
        val results = assertions.map {
            try {
                it()
                null
            } catch (t: Throwable) {
                BlacklistedExceptions.rethrowIfBlacklisted(t)
                hasFailure = true
                t
            }
        }
        if (hasFailure) {
            val failures = results.filterNotNull()
            if (printExceptionsOnFailure) {
                failures.forEach {
                    it.printStackTrace()
                }
            }
            val multipleFailuresError = MultipleFailuresError(null, failures)
            failures.forEach { multipleFailuresError.addSuppressed(it) }
            throw multipleFailuresError
        }
    }
}
