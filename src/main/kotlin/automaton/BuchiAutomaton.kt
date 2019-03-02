package automaton

import model.*

class BuchiAutomaton(
        val atomPropositions: Set<String>,
        val states: Set<Node>,
        val start: Set<Node>,
        val finish: Set<Node>,
        val delta: Map<Node, Map<Symbol, List<Node>>>
) {
    companion object {
        private fun addTransition(delta: MutableMap<Node, MutableMap<Symbol, MutableList<Node>>>,
                                  start: Node,
                                  symbol: Symbol,
                                  end: Node) {
            val transitionsFromStart = delta.computeIfAbsent(start) { mutableMapOf() }
            val endNodes = transitionsFromStart.computeIfAbsent(symbol) { mutableListOf() }
            endNodes.add(end)
        }

        fun create(automaton: Automaton): BuchiAutomaton {
            val atomPropositions: MutableSet<String> = mutableSetOf()
            with (atomPropositions) {
                automaton.states.map { it.name }.forEach { add(it) }
                automaton.events.map { it.name }.forEach { add(it) }
                automaton.transitions.map { it.event }.forEach { add(it) }
                automaton.transitions.flatMap { it.actions }.forEach { add(it) }
            }

            val states = mutableSetOf<Node>()
            val start: MutableSet<Node> = mutableSetOf()

            val nodesByStateId: MutableMap<Int, Pair<Node, Node>> = mutableMapOf()
            for (state in automaton.states) {
                val id = state.id
                val name = state.name
                val from = Node("${name}_enter")
                val to = Node(name)
                nodesByStateId[id] = Pair(from, to)
                states.add(from)
                states.add(to)
                if (state.type == 1) {
                    start.add(from)
                }
            }

            val delta = mutableMapOf<Node, MutableMap<Symbol, MutableList<Node>>>()
            for ((enterState, state) in nodesByStateId.values) {
                addTransition(delta, enterState, Symbol(Variable(state.name)), state)
            }
            for (transition in automaton.transitions) {
                val automatonTransitionStart = automaton.states
                        .find { transition.id in it.outgoing }
                        ?: throw AssertionError("no start for $transition")
                val automatonTransitionEnd = automaton.states
                        .find { transition.id in it.incoming }
                        ?: throw AssertionError("no end for $transition")
                val from = nodesByStateId[automatonTransitionStart.id]!!.second
                val to = nodesByStateId[automatonTransitionEnd.id]!!.first

                val eventVariable = Variable(transition.event)
                val stateVariable = Variable(from.name)
                val eventLabel = Symbol(setOf(stateVariable, eventVariable))
                val eventTransitionEnd: Node
                if (transition.actions.isEmpty()) {
                    eventTransitionEnd = to
                } else {
                    eventTransitionEnd = Node("temp_${transition.event}")
                    states.add(eventTransitionEnd)
                }
                addTransition(delta, from, eventLabel, eventTransitionEnd)

                var transitionStart = eventTransitionEnd
                for ((i, action) in transition.actions.withIndex()) {
                    val actionVariable = Variable(action)
                    val transitionEnd: Node
                    if (i == transition.actions.lastIndex) {
                        transitionEnd = to
                    } else {
                        transitionEnd = Node("temp_$action")
                        states.add(transitionEnd)
                    }
                    val label = Symbol(setOf(
                            stateVariable,
                            eventVariable,
                            actionVariable
                    ))
                    addTransition(delta, transitionStart, label, transitionEnd)
                    transitionStart = transitionEnd
                }
            }

            return BuchiAutomaton(atomPropositions, states, start, states, delta)
        }

        fun create(glba: GeneralizedLabeledBuchiAutomaton): BuchiAutomaton {
            data class CountingNode(val node: Node, val n: Int) : Node(node.name, node.incoming, node.now, node.next) {
                override fun toString(): String {
                    return "CountingNode(name=$name, n=$n)"
                }

                override fun equals(other: Any?): Boolean {
                    if (this === other) {
                        return true
                    }
                    if (other?.javaClass != javaClass) {
                        return false
                    }

                    other as CountingNode

                    return node == other.node && n == other.n
                }

                override fun hashCode(): Int = node.hashCode() * 31 + n
            }

            val nodes = mutableSetOf<Node>()
            val start = mutableSetOf<Node>()
            val finish = mutableSetOf<Node>()
            val delta = mutableMapOf<Node, MutableMap<Symbol, MutableList<Node>>>()

            for (node in glba.states) {
                for (i in 1 .. glba.finish.size) {
                    val countingNode = CountingNode(node, i)
                    nodes.add(countingNode)
                    if (i == 1 && node in glba.start) {
                        start.add(countingNode)
                    }
                    if (i == 1 && node in glba.finish[0]) {
                        finish.add(countingNode)
                    }
                }
            }

            for (state in glba.states) {
                val label = glba.labels[state] ?: throw AssertionError("No label for $state")
                val toList: List<Node> = glba.delta[state] ?: listOf(state)

                for ((i, f) in glba.finish.withIndex()) {
                    val fromCount = i + 1
                    val toCount = if (state !in f) fromCount else
                        (fromCount % glba.finish.size) + 1

                    for (toNode in toList) {
                        val from = CountingNode(state, fromCount)
                        val to = CountingNode(toNode, toCount)
                        addTransition(delta, from, label, to)
                    }
                }
            }

            return BuchiAutomaton(glba.atomPropositions, nodes, start, finish, delta)
        }
    }


    override fun toString(): String {
        val sb = StringBuilder("BuchiAutomaton:\n")
        with (sb) {
            append("AP:\n")
            atomPropositions.forEach { append(it); append(", ") }
            append("\nStates:\n")
            states.forEach { append(it); append(", ") }
            append("\nStart:\n")
            start.forEach { append(it); append(", ") }
            append("\nFinish:\n")
            finish.forEach { append(it); append(", ") }
            append("\nTransitions:\n")
            for ((from, transition) in delta) {
                for ((symbol, to) in transition) {
                    append("$from -> $symbol -> $to\n")
                }
            }
        }
        return sb.toString()
    }
}
