import java.io.File

fun parseInputFile(file: File): Pair<ArrayList<Process>, Int> {
    var resultNumberOfTicks = 0
    val list = arrayListOf<Process>().apply {
        file.forEachLine {
            it.split(" ").let {
                add(
                    Process(
                        name = it[0],
                        numberOfTicks = it[1].toInt(),
                        appearanceTime = if (it.size > 2) it[2].toInt() else 0
                    ).let {
                        resultNumberOfTicks += it.numberOfTicks
                        it
                    }
                )
            }
        }
    }
    return Pair(list, resultNumberOfTicks)
}

fun main() {
    val numberOfActualTicks: Int
    var numberOfWaitTicks = 0
    var numberOfAbstractTicks = 0
    var currentTick = 0
    val processList = parseInputFile(
//        File("test1")
        File("test2")
    ).let {
        numberOfActualTicks = it.second
        it.first
    }

    val ticks = arrayListOf<String>()
    println(numberOfActualTicks)
    repeat(numberOfActualTicks) {
        processList.filter { it.appearanceTime <= currentTick && it.ticksLeft > 0 }.let { sublist ->
            if (sublist.isNotEmpty())
                sublist.minBy { it.ticksLeft }.let {
                    ticks.add(it.name)
                    it.ticksLeft--
                }
            else {
                ticks.add("_")
            }
        }
        currentTick++
    }

    processList.forEach { process ->
        print(
            "${process.name} ${process.numberOfTicks} ${process.appearanceTime} "
        )
        currentTick = 0
        ticks.subList(0, ticks.indexOfLast { it.contains(process.name) } + 1).forEach { processName ->
            print(processName.contains(process.name).let {
                if (currentTick < process.appearanceTime) " "
                else {
                    if (!it) numberOfWaitTicks++
                    numberOfAbstractTicks++
                    if (it) "И" else "Г"
                }
            })
            currentTick++
        }
        println()
    }

//    println(ticks)
    println("Efficiency: ${"%.${2}f".format((numberOfWaitTicks.toFloat() / numberOfAbstractTicks * 100))}%")
}

data class Process(
    val name: String,
    val numberOfTicks: Int,
    val appearanceTime: Int,
    var ticksLeft: Int = numberOfTicks
)