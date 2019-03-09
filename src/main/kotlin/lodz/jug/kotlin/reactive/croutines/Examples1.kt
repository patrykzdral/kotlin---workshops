package lodz.jug.kotlin.reactive.croutines

import kotlinx.coroutines.*
import lodz.jug.kotlin.Displayer

fun main(args: Array<String>) {
    example1GlobalScope()
//    example2MixingBlocking()
//    example3FullBlocking()
//    runExample4()
//    example5WaitForJob()
//    example6LocalScopeWithDslReceiver()
//    example7CustomScope()
//    example8ManyDots()
}

//one scope - main thread and scope thread
//in side launch new thread is started
fun example1GlobalScope() {
    GlobalScope.launch {
        delay(1000L)
        Displayer.displayThread()
        println("World!")
    }
    println("Hello! : in ${Displayer.threadName()}")
    Thread.sleep(2000)
}


//scope and blocking
//runBlocking in main thread
fun example2MixingBlocking() {
    GlobalScope.launch {
        delay(1000L)
        displayThread("in launch")
        println("World!")
    }
    println("Hello!")
    runBlocking {
        displayThread("in runBlocking")
        delay(2000L)
    }
}

//runBlocking still in main thread
fun example3FullBlocking() = runBlocking {
    GlobalScope.launch {
        displayThread("in runBlocking inside launch")
        delay(1000L)
        println("World!")
    }
    println("Hello!")
    displayThread("runBlocking first level")
    delay(2000)
}

fun runExample4() {
    displayThread("example4Outer")
    example4ThreadsInfo()
}

fun example4ThreadsInfo() = runBlocking {
    GlobalScope.launch {
        delay(1000L)
        displayThread("example4LocalScope")
        println("World!")
    }
    println("Hello!")
    displayThread("example4OuterScope")
    delay(2000)
}


fun example5WaitForJob() = runBlocking {
    val job = GlobalScope.launch {
        delay(1000L)
        displayThread("example5 in global scope")
        println("World!")
    }

    println("Hello5 and waiting")
    job.join()
}

//take a look at runBlocking signature - it expects code block with coroutin scope as a receiver
fun example6LocalScopeWithDslReceiver() = runBlocking {
    launch {
        delay(1000L)
        displayThread("example 6 in local scope")
        println("World!")
    }
    displayThread("example 6 outside scope")
    println("Hello6")
}


//Creates local custom scope
// check order of execution to understand how exactly code is executed
fun example7CustomScope() = runBlocking {
    launch {
        delay(1000L)
        displayThread("example 7 in local scope")
    }

    coroutineScope {
        launch {
            delay(500)
            displayThread("example 7 in custom scope")
        }

        delay(500)
        displayThread("example 7 in custom scope after launch")

    }


    displayThread("example 7 outside scopes")

}


fun example8ManyDots()   {
    var threads = emptySet<String>()
    runBlocking {
        repeat(10_000){
            launch {
                delay(100)
                threads += Thread.currentThread().name
                print(".")
            }
        }
    }

    println("\nrun in following threads")
    threads.forEach(::println)
}