package lodz.jug.kotlin.starter.oop.exercises

import io.kotlintest.matchers.shouldBe
import io.kotlintest.specs.StringSpec
import lodz.jug.kotlin.Education

class FactoryMethodExercise : StringSpec(){
    init{
        "Factory Method should return 'Upper' processor"  {
            val upper: Processor = ProcessorFactory.processorFor("UPPER")

            val result=upper.process("someInput")

            result shouldBe "SOMEINPUT"
        }

        "Factory Method should  trim processor" {
            val trim= ProcessorFactory.processorFor("TRIM")

            val result=trim.process("      someInput    ")

            result shouldBe "someInput"
        }
    }
}

interface Processor{
    fun process(input:String):String
}

object ProcessorFactory{
    fun processorFor(id:String): Processor {
        return when (id) {
            "UPPER" -> Upper
            "TRIM" -> Trim
            else -> throw RuntimeException("unknown processor $id")
        }

    }

    private object Upper : Processor {
        override fun process(input: String): String = input.toUpperCase()
    }

    private object Trim : Processor {
        override fun process(input: String): String = input.trim()
    }

}