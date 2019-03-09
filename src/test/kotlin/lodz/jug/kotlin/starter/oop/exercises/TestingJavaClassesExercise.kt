package lodz.jug.kotlin.starter.oop.exercises

import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec
import lodz.jug.kotlin.starter.oop.JavaStatefulObject

class TestingJavaClassesExercise : StringSpec(){
    init {
        "state in java class should be set correctly".config(enabled = true) {
            val sut= JavaStatefulObject()

            sut.state = 101

            sut.state shouldBe 101
        }

        //UNCOMMENT AND FIX
        "convert to string correctly" {
            //???
            val sut= JavaStatefulObject()

            sut.state = "STATE"

            sut.toString() shouldBe "JavaStatefulObject{state=STATE}"
        }
    }
}