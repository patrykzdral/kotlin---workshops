package lodz.jug.kotlin.spring.functionalwebstart.exercises

import arrow.core.andThen
import arrow.core.identity
import org.amshove.kluent.`should contain all`
import org.amshove.kluent.shouldBe
import org.junit.Test

typealias Pipe = (String) -> String

class IntroSpring2Exercises{


    @Test
    fun buildIndexedNews(){

        val trim:(String)->String=String::trim
        val toLower : Pipe = String::toLowerCase
        val processor : Pipe = toLower andThen trim andThen String::capitalize

        val result=simpleNews{
            article("world".tag, "world news",String::capitalize)
            article("domestic".tag, "     Domestic news",String::trim)
            article("domestic".tag, "   domestic local news",trim andThen String::capitalize)
            article("fun".tag, "   funny news",{s:String -> s.trim()} andThen String::capitalize)
            article("fun".tag, "    more FUN",processor)
            article("fun".tag, "It's a prank bro!",::identity)
        }


        result.getValue(Tag("world")) `should contain all`   arrayListOf("World news")
        result.getValue(Tag("domestic")) `should contain all`   arrayListOf("Domestic news","Domestic local news")
        result.getValue(Tag("fun")) `should contain all`   arrayListOf("Funny news","More fun","It's a prank bro!")
    }


    @Test
    fun buildMultiNews(){
          val articles = multiNews {
              article(Tag2("world"),"world article 1")
              article("world".or("fun"),"funny things on the world")
              article("world" or "fun" or "sport","funny world cup")
              article(Tag2("fun"),"just fun")
          }


        articles.getFirstMatching("world") shouldBe "world article 1"
        articles.getFirstMatching("fun") shouldBe "funny things on the world"
        articles.getFirstMatching("sport") shouldBe "funny world cup"
    }
}


fun simpleNews(operation : SimpleNewsDsl.() -> Unit): Map<Tag, List<String>> = SimpleNewsDsl().apply(operation).display()
data class Tag(val v:String)

class SimpleNewsDsl{

    private val articles = mutableMapOf<Tag,List<String>>()

    val String.tag:Tag
        get() = Tag(this)

    fun article(tag:Tag,content : String,processor : (String) -> String){
        articles[tag] = articles[tag].orEmpty() + processor(content)
    }


    fun display(): Map<Tag, List<String>> = articles

}


fun multiNews(operation : MultiNewsDsl.() -> Unit):MultiNewsDsl.Articles = MultiNewsDsl().apply(operation).allArticles()

sealed class Tag2{
    abstract fun matches(other : String) :  Boolean
    companion object {
        operator fun invoke(tag:String) = SingleTag(tag)
    }
}

class SingleTag(private val tag:String) : Tag2() {
    override fun matches(other: String): Boolean  = when (other) {
        tag -> true
        else -> false
    }
}

class MultiTag(private val tags:Set<String>) : Tag2(){
    fun append(other:String) = MultiTag(tags+other)

    override fun matches(other: String): Boolean  = tags.stream().anyMatch { tag -> tag == other}
}

class MultiNewsDsl {
    private var articles =  listOf<Pair<Tag2,String>>()

    companion object {
        const val NOT_FOUND : String = "NOT_FOUND"
    }

    infix fun String.or(other:String): MultiTag = MultiTag(setOf(this, other))
    infix fun MultiTag.or(other: String):MultiTag = this.append(other)

    fun article(tag:Tag2,content : String){
        articles = articles + (tag to content)
    }

    fun allArticles() : Articles = Articles(articles)

    class Articles(private val articles: List<Pair<Tag2, String>>){
        fun getFirstMatching(tag:String): String = articles.find { (tag2,_)-> tag2.matches(tag)} ?.second ?: MultiNewsDsl.NOT_FOUND
    }

}

