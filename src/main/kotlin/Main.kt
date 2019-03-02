import automaton.Automaton
import automaton.BuchiAutomaton
import automaton.GeneralizedLabeledBuchiAutomaton
import console.ConsoleColorer
import gen.LtlLexer
import gen.LtlParser
import model.LtlExpression
import mapper.LtlExpressionBuilder
import model.Not
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import verification.verify
import java.io.File

fun parseLtlExpression(expressionString: String): LtlExpression {
    val lexer = LtlLexer(CharStreams.fromString(expressionString))
    val parser = LtlParser(CommonTokenStream(lexer))
    val expression = parser.expression()
    return LtlExpressionBuilder().visit(expression)
}

fun main(args: Array<String>) {
    if (args.size != 2) {
        println("Usage: Main <file with automaton> <file with ltl formula>")
        System.exit(0)
    }
    val automatonFile = args[0]
    val expressionFile = args[1]

    val automaton = Automaton.readFromFile(automatonFile)
    val buchiAutomaton = BuchiAutomaton.create(automaton)

    val lineList = mutableListOf<String>()
    File(expressionFile).inputStream().bufferedReader().useLines { lines ->
        lines.forEach { lineList.add(it) }
    }

    for (item in lineList) {
        val ltlExpression = Not(parseLtlExpression(item))

        val generalizedBuchiAutomatonByLtl = GeneralizedLabeledBuchiAutomaton.create(ltlExpression, buchiAutomaton.atomPropositions)
        val buchiAutomatonByLtl = BuchiAutomaton.create(generalizedBuchiAutomatonByLtl)

        val answer = verify(buchiAutomaton, buchiAutomatonByLtl)
        if (answer.holds) {
            ConsoleColorer.print("$item: always true", "green")
        } else {
            ConsoleColorer.print("$item: not always true", "red")
            println("Counter-example:")
            for ((i, label) in answer.counterExamplePath!!.withIndex()) {
                assert(label.min == label.max)
                println("$i: ${label.min}")
            }
            println("Return back to ${answer.startIndex!!}, it's a cycle")
        }

        println()
    }
}
