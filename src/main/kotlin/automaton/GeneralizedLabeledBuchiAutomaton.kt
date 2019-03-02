package automaton

import model.*

class GeneralizedLabeledBuchiAutomaton(
        val atomPropositions: Set<String>,
        val states: List<Node>,
        val labels: Map<Node, Symbol>,
        val delta: Map<Node, List<Node>>,
        val start: List<Node>,
        val finish: List<Set<Node>>
) {
    companion object {
        fun create(ltlExpression: LtlExpression, atomPropositions: Set<String>): GeneralizedLabeledBuchiAutomaton {
            val ltlFormula = LtlExpression.toNNF(ltlExpression)
            fun curr1(f: LtlExpression): Set<LtlExpression> = when (f) {
                is Until -> setOf(f.lhs)
                is Release -> setOf(f.rhs)
                is Or -> setOf(f.rhs)
                else -> throw AssertionError("curr1 of $f is undefined")
            }

            fun next1(f: LtlExpression): Set<LtlExpression> = when (f) {
                is Until -> setOf(f)
                is Release -> setOf(f)
                is Or -> emptySet()
                else -> throw AssertionError("next1 of $f is undefined")
            }

            fun curr2(f: LtlExpression): Set<LtlExpression> = when (f) {
                is Until -> setOf(f.rhs)
                is Release -> setOf(f.lhs, f.rhs)
                is Or -> setOf(f.lhs)
                else -> throw AssertionError("curr2 of $f is undefined")
            }

            val init = Node("init")
            val nodes: MutableList<Node> = mutableListOf()

            var nodeId = 0
            fun freshNodeName() = "node_${++nodeId}"

            fun expand(curr: Set<LtlExpression>, old: Set<LtlExpression>, next: Set<LtlExpression>, incoming: Set<Node>) {
                if (curr.isEmpty()) {
                    val r = nodes.find { it.next == next && it.now == old }
                    if (r != null) {
                        r.incoming.addAll(incoming)
                        return
                    } else {
                        val q = Node(freshNodeName())
                        nodes.add(q)
                        q.incoming.addAll(incoming)
                        q.now.addAll(old)
                        q.next.addAll(next)
                        expand(q.next, emptySet(), emptySet(), setOf(q))
                    }
                } else {
                    val f: LtlExpression = curr.first()
                    var nCurr: Set<LtlExpression> = curr - f
                    val nOld: Set<LtlExpression> = old + f

                    fun base(f: LtlExpression): Boolean {
                        if (f is False || f is True) {
                            return true
                        }
                        if (f is Variable) {
                            return true
                        }
                        if (f is Not && f.expression is Variable) {
                            return true
                        }
                        return false
                    }

                    if (base(f)) {
                        if (f is False || LtlExpression.neg(f) in nOld) {
                            return
                        }
                        expand(nCurr, nOld, next, incoming)
                    } else if (f is And) {
                        nCurr += (setOf(f.lhs, f.rhs) - nOld)
                        expand(nCurr, nOld, next, incoming)
                    } else if (f is Next) {
                        expand(nCurr, nOld, next + f.expression, incoming)
                    } else if (f is Or || f is Until || f is Release) {
                        val curr1 = nCurr + (curr1(f) - nOld)
                        expand(curr1, nOld, next + next1(f), incoming)
                        val curr2 = nCurr + (curr2(f) - nOld)
                        expand(curr2, nOld, next, incoming)
                    } else {
                        throw AssertionError("not in negative normal form")
                    }
                }
            }

            expand(mutableSetOf(ltlFormula), emptySet(), emptySet(), setOf(init))

            return createInner(ltlFormula, nodes, init, atomPropositions)
        }

        private fun createInner(ltlFormula: LtlExpression,
                                nodes: List<Node>,
                                init: Node,
                                atomicPropositions: Set<String>): GeneralizedLabeledBuchiAutomaton {

            val atomPropositions = atomicPropositions.map { Variable(it) }
            val labels = mutableMapOf<Node, Symbol>()
            for (node in nodes) {
                val min = node.now intersect atomPropositions
                val max = mutableSetOf<LtlExpression>()
                atomPropositions
                        .filter { Not(it) !in node.now }
                        .forEach { max.add(it) }
                labels[node] = Symbol(min, max)
            }

            val delta = mutableMapOf<Node, List<Node>>()
            for (node in nodes) {
                for (from in node.incoming) {
                    if (from == init)
                        continue
                    delta.merge(from, listOf(node)) { t, u -> t + u }
                }
            }

            val start = nodes.filter { init in it.incoming }

            val finish = mutableListOf<Set<Node>>()
            for (g in ltlFormula.closure()) {
                if (g !is Until)
                    continue
                val fg = nodes.filter { g.rhs in it.now || g !in it.now }.toSet()
                finish.add(fg)
            }

            return GeneralizedLabeledBuchiAutomaton(atomicPropositions, nodes, labels, delta, start, finish)
        }
    }
}
