package verification

import automaton.BuchiAutomaton
import automaton.Node
import model.Symbol
import model.subset

data class VerificationResult(
        val holds: Boolean,
        val counterExamplePath: List<Symbol>? = null,
        val startIndex: Int? = null
)

fun verify(automaton: BuchiAutomaton, ltlAutomaton: BuchiAutomaton): VerificationResult {
    val verticesPath = mutableListOf<Pair<Node, Node>>()
    val pathSet = mutableSetOf<Pair<Node, Node>>()
    val transitions = mutableListOf<Symbol>()

    val used = mutableSetOf<Pair<Node, Node>>()
    val transitionsRepeated = mutableListOf<Symbol>()

    var foundPath = false
    var path: List<Symbol>? = null
    var cycleStartIndex: Int? = null

    fun constructLoop(state: Node): Map<Symbol, List<Node>> =
        mapOf(Symbol(emptySet(), emptySet()) to listOf(state))

    fun dfsRepeated(vu: Pair<Node, Node>) {
        if (foundPath) {
            return
        }
        used.add(vu)
        val automatonTransitions = automaton.delta.getOrDefault(vu.first, constructLoop(vu.first))
        val ltlTransitions = ltlAutomaton.delta.getOrDefault(vu.second, constructLoop(vu.second))
        for (automatonTransitionLabel in automatonTransitions.keys) {
            for (ltlTransitionLabel in ltlTransitions.keys) {
                if (automatonTransitionLabel subset ltlTransitionLabel) {
                    val automatonNodes = automatonTransitions.getOrDefault(automatonTransitionLabel, emptyList())
                    val ltlNodes = ltlTransitions.getOrDefault(ltlTransitionLabel, emptyList())
                    for (automatonNode in automatonNodes) {
                        for (ltlNode in ltlNodes) {
                            val to = Pair(automatonNode, ltlNode)
                            if (to in pathSet) {
                                foundPath = true
                                path = transitions + transitionsRepeated + automatonTransitionLabel
                                cycleStartIndex = verticesPath.indexOf(to)
                                return
                            }
                            if (to !in used) {
                                transitionsRepeated.add(automatonTransitionLabel)
                                dfsRepeated(to)
                                transitionsRepeated.removeAt(transitionsRepeated.lastIndex)
                            }
                        }
                    }
                }
            }
        }
    }

    fun dfs(vu: Pair<Node, Node>) {
        if (foundPath) {
            return
        }
        pathSet.add(vu)
        verticesPath.add(vu)
        val automatonTransitions = automaton.delta.getOrDefault(vu.first, constructLoop(vu.first))
        val ltlTransitions = ltlAutomaton.delta.getOrDefault(vu.second, constructLoop(vu.second))
        for (autoTransitionLabel in automatonTransitions.keys) {
            for (ltlTransitionLabel in ltlTransitions.keys) {
                if (autoTransitionLabel subset ltlTransitionLabel) {
                    val automatonNodes = automatonTransitions.getOrDefault(autoTransitionLabel, emptyList())
                    val ltlNodes = ltlTransitions.getOrDefault(ltlTransitionLabel, emptyList())
                    for (automatonNode in automatonNodes) {
                        for (ltlNode in ltlNodes) {
                            val to = Pair(automatonNode, ltlNode)
                            if (to !in pathSet) {
                                transitions.add(autoTransitionLabel)
                                dfs(to)
                                transitions.removeAt(transitions.lastIndex)
                            }
                        }
                    }
                }
            }
        }
        if (vu.first in automaton.finish && vu.second in ltlAutomaton.finish) {
            used.clear()
            dfsRepeated(vu)
        }
        verticesPath.removeAt(verticesPath.lastIndex)
        pathSet.remove(vu)
    }

    for (v in automaton.start) {
        for (u in ltlAutomaton.start) {
            dfs(Pair(v, u))
            if (foundPath) {
                return VerificationResult(false, path, cycleStartIndex)
            }
        }
    }

    return VerificationResult(true)
}