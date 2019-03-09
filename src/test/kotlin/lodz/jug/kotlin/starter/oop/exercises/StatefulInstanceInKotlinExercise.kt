package lodz.jug.kotlin.starter.oop.exercises

import io.kotlintest.matchers.containsAll
import io.kotlintest.matchers.should
import io.kotlintest.matchers.shouldBe
import io.kotlintest.specs.StringSpec
import java.util.*

class StatefulInstanceInKotlinExercise : StringSpec() {
    //Uncomment tests and finish implementation
    init {
        "Stateful class should correctly implement 'HasState' interface"{
            val instance: HasState = KotlinStatefulClass("one")

            instance.set("two")

            instance.get() shouldBe "two"
        }

        "Stateful class should correctly implement Data Change journal"{
            val instance= KotlinStatefulClass("one")

            instance.set("two")
            instance.set("three")
            instance.set("four")

            val history: Collection<Any> = instance.showHistory()
            history should containsAll<Any>("one", "two", "three", "four") //notice that element from constructor is also here
        }
    }
}

interface HasState {
    fun set(state: Any)
    fun get(): Any
}

class KotlinStatefulClass(private var state: Any) : HasState {

    //private val stateList: MutableList<Any> = ArrayList()
    private val stateList: MutableList<Any> = mutableListOf()

    init {
        stateList.add(state)
    }

    override fun get(): Any {
        return state
    }

    override fun set(state: Any) {
        this.state = state
        stateList.add(state)
    }

    fun showHistory(): Collection<Any>{
        return stateList;
    }
}