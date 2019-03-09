package lodz.jug.kotlin.starter.oop

import lodz.jug.kotlin.Displayer

fun main(args: Array<String>) {
    Displayer.header("Interfaces and Singleton Objects")

    Displayer.title("using java interface")
    val kotlinImplementation:JavaInterface=KotlinImplementation("This is Kotlin")
    Displayer.section("kotlin class with java interface",kotlinImplementation.toJson())

    Displayer.title("calling java static methods from Kotlin")
    Displayer.section("STATIC FROM JAVA 1 : "+StaticMethods.prefix("scala","printing"))
    Displayer.section("STATIC FROM JAVA 2 (2*3): "+StaticMethods.multiply(2,3))


    Displayer.title("Objects")

    val factored: KotlinInterface = FactoryExample.factoryMethod("factoryExample")
    Displayer.section("factored",factored.toJson())

    val factoredInfix: KotlinInterface = FactoryExample infixFactory "infixExample"
    Displayer.section("infix",factoredInfix.toJson())

    val factoredInvoke=FactoryExample("invoke Example")
    Displayer.section("invoke",factoredInvoke.toJson())

    val fromPrivateConstructor = ThirdImplementation.instance("privateConstructor")
    Displayer.section("private",fromPrivateConstructor.toJson())



    Displayer.title("Properties in interfaces")
    val fourthImplementation=FourthImplementation("4th value")
    Displayer.section("4th toJson",fourthImplementation.toJson())
    Displayer.section("4th toJsonTrim",fourthImplementation.trimmedJson)

}

class KotlinImplementation(private val value:String) : JavaInterface{

    override fun toJson(): String =
        """{
       | "value" : '$value'
       |}"""


}

interface KotlinInterface {
    fun toJson():String
}


class SecondImplementation(val value:String) : KotlinInterface{
    override fun toJson(): String = """{"secondValue" : '$value'}"""
}




object FactoryExample{
    fun factoryMethod(v:String):KotlinInterface = SecondImplementation(v)
    infix fun infixFactory(v:String):KotlinInterface = SecondImplementation(v)
    operator fun invoke(v:String):KotlinInterface = SecondImplementation(v)
}


class ThirdImplementation private constructor(val v: String) : KotlinInterface{
    companion object {
        fun instance(v:String) = ThirdImplementation(v)
    }

    override fun toJson(): String = """{"thirdValue" : '$v'}"""
}

interface KotlinInterfaceWithPreoperty {
    fun toJson():String

    val trimmedJson:String
        get() = toJson().trim()
}

class FourthImplementation(val value:String) : KotlinInterface, KotlinInterfaceWithPreoperty{
    override fun toJson(): String = """              {"secondValue" : '$value'}       """
}
