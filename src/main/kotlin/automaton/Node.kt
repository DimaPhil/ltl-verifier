package automaton

import model.LtlExpression

open class Node(
        val name: String = "",
        val incoming: MutableSet<Node> = mutableSetOf(),
        val now: MutableSet<LtlExpression> = mutableSetOf(),
        val next: MutableSet<LtlExpression> = mutableSetOf()
) {
    override fun toString() = "Node(name=$name)"
}
