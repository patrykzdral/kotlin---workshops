@file:Suppress("UNREACHABLE_CODE")

package lodz.jug.kotlin.starter.fp.exercises

import arrow.core.andThen
import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec

class Part1DefineFunctionExercise : StringSpec() {
    init {
        //LEVEL1
        "complete function definition so that test passes".config(enabled = true) {
            val square: (Int) -> Int = { x -> x * x }// x*x
            val isEven: (Int) -> Boolean = { x -> x % 2 == 0 }// 3 % 2 -> 1 , 4% 2 -> 0

            val isEvenIt: (Int) -> Boolean = { it % 2 == 0 } //use form with it

            square(2) shouldBe 4
            square(3) shouldBe 9
            isEven(2) shouldBe true
            isEven(3) shouldBe false
            isEvenIt(3) shouldBe false
        }

        "operations on tuples".config(enabled = true) {
            val snd: (Pair<String, Int>) -> Int = { it.second }   //returns second element
            val swap: (Pair<String, Int>) -> Pair<Int, String> = { Pair(it.second, it.first) } //swap elements


            snd(Pair("a", 1)) shouldBe (1)
            snd(Pair("b", 2)) shouldBe (2)

            swap(Pair("a", 1)) shouldBe (Pair(1, "a"))
            swap(Pair("b", 2)) shouldBe (Pair(2, "b"))
        }

        "compose 3 functions in proper order".config(enabled = true) {
            val fst: (Pair<String, Int>) -> String = { it.first }
            val parse: (String) -> Int = { Integer.parseInt(it) }
            val square: (Int) -> Int = { Math.pow(it.toDouble(), 2.0).toInt() }

            val squareFst: (Pair<String, Int>) -> Int = fst andThen parse andThen square

            squareFst(Pair("2", 1)) shouldBe 4
            squareFst(Pair("3", 1)) shouldBe 9
        }

        //LEVEL2
        "custom 'andThen' method".config(enabled = true) {
            //customAndThen defined at the bottom of the file

            val squareStr = customAndThen({ it.toInt() }, { i: Int -> Math.sqrt(i.toDouble()) })

            squareStr("4") shouldBe 2.0
            squareStr("9") shouldBe 3.0

        }

        //LEVEL3
        // UNCOMMENT AND FIX andThenGeneric at the bottom of the file
        "andThen generic".config(enabled = false) {
            val isStringEven = andThenGeneric({ s: String -> s.toInt() }, { i: Int -> i % 2 == 0 })
            val squareAndToString: (Int) -> String = andThenGeneric({ it * it }, { i: Int -> "Result is : " + i })

            isStringEven("2") shouldBe true
            isStringEven("3") shouldBe false

            squareAndToString(3) shouldBe "Result is : 9"
            squareAndToString(4) shouldBe "Result is : 16"
        }


        "compose sequence of functions".config(enabled = false) {
            val intToInts: List<(Int) -> Int> = listOf({ i -> i + 1 }, { i -> i * i }, { i -> i - 1 }, { i -> i - 1 })
            val stringToStrings: List<(String) -> String> = listOf({ s -> s + "a" }, { s -> s + "b" }, { s -> s + "c" })

            andThenSeq(intToInts)(2) shouldBe 7
            andThenSeq(intToInts)(3) shouldBe 14
            andThenSeq(stringToStrings)("start_") shouldBe "start_abc"
        }


        //LEVEL4
        "map your own box".config(enabled = false) {
            val box1: MyBox<Int> = MyBox.of(1)
            val box2: MyBox<String> = MyBox.of("word")
            val box3: MyBox<User> = MyBox.of(User("userEmail@mail.com"))


            box1.map { it + 1 }.check { it == 2 } shouldBe true
            box2.map { "prefix_" + it }.check { it == "prefix_word" } shouldBe true
            box3.map { it.email }.check { it == "userEmail@mail.com" } shouldBe true
        }


    }

    //LEVEL2
    fun customAndThen(f: (String) -> Int, g: (Int) -> Double): (String) -> Double = { arg -> g(f(arg)) }

    //LEVEL3
    fun <A, B, C> andThenGeneric(f: (A) -> B, g: (B) -> C): (A) -> C = { arg -> g(f(arg)) }

    fun <A> andThenSeq(seq: List<(A) -> A>): (A) -> A = TODO()


    //LEVEL4
    class MyBox<A> private constructor(private val a: A) {
        companion object {
            fun <T> of(a: T) = MyBox(a)
        }

        fun <B> map(f: (A) -> B): MyBox<B> = TODO()
        fun check(p: (A) -> Boolean): Boolean = TODO()
    }


    class User(val email: String)


}
