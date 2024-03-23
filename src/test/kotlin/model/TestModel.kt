package model

import kotlin.test.BeforeTest
import prepareServer

abstract class TestModel {
    @BeforeTest
    fun setup() {
        prepareServer()
    }
}
