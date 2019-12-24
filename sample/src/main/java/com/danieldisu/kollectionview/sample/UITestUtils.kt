package com.danieldisu.kollectionview.sample

object UITestUtils {

    fun isEspresso(): Boolean = try {
        Class.forName("androidx.test.espresso.Espresso")
        true
    } catch (e: Throwable) {
        false
    }
}
