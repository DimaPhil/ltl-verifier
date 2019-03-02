import automaton.Automaton
import automaton.BuchiAutomaton
import automaton.GeneralizedLabeledBuchiAutomaton
import gen.LtlLexer
import gen.LtlParser
import model.LtlExpression
import mapper.LtlExpressionBuilder
import model.Not
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import verification.verify
import java.io.File
import java.io.PrintStream

fun parseLtlExpression(expressionString: String): LtlExpression {
    val lexer = LtlLexer(CharStreams.fromString(expressionString))
    val parser = LtlParser(CommonTokenStream(lexer))
    val expression = parser.expression()
    return LtlExpressionBuilder().visit(expression)
}

fun main(args: Array<String>) {
    if (2 > args.size || args.size > 3) {
        println("Usage: Main <file with automaton> <file with ltl formula> [<output file>]")
        System.exit(0)
    }
    val automatonFile = args[0]
    val expressionFile = args[1]
    val outputFile = if (args.size < 3) System.out else PrintStream(File(args[2]).outputStream())

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
            outputFile.println("$item: always true")
        } else {
            outputFile.println("$item: not always true")
            outputFile.println("Counter-example:")
            for ((i, label) in answer.counterExamplePath!!.withIndex()) {
                assert(label.min == label.max)
                outputFile.println("$i: ${label.min}")
            }
            outputFile.println("Return back to ${answer.startIndex!!}, it's a cycle")
        }

        outputFile.println()
    }
}
