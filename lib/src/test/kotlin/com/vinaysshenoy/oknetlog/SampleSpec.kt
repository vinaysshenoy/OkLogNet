package com.vinaysshenoy.oknetlog

import org.amshove.kluent.shouldEqualTo
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on

/**
 * Created by vinaysshenoy on 16/01/18.
 */
object SampleSpec : Spek({

    describe("A sample test") {
        on("running it") {
            it("should succeed") {
                5 shouldEqualTo 5
            }
        }
    }
})