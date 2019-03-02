package model

sealed class LtlExpression {
    fun inNNF(): Boolean = when (this) {
        is False, True -> true
        is Variable -> true

        is Not -> expression is Variable

        is And -> lhs.inNNF() && rhs.inNNF()
        is Or -> lhs.inNNF() && rhs.inNNF()

        is Next -> expression.inNNF()

        is Future -> false
        is Globally -> false

        is Until -> lhs.inNNF() && rhs.inNNF()
        is Release -> lhs.inNNF() && rhs.inNNF()
    }

    fun closure(): Set<LtlExpression> {
        val res = mutableSetOf(False, True)

        fun closureHelper(f: LtlExpression) {
            res.add(f)
            res.add(toNNF(neg(f)))
            when (f) {
                is Not -> closureHelper(f.expression)
                is And -> {
                    closureHelper(f.lhs)
                    closureHelper(f.rhs)
                }
                is Or -> {
                    closureHelper(f.lhs)
                    closureHelper(f.rhs)
                }
                is Next -> closureHelper(f.expression)
                is Future -> closureHelper(f.expression)
                is Globally -> closureHelper(f.expression)
                is Until -> {
                    closureHelper(f.lhs)
                    closureHelper(f.rhs)
                }
                is Release -> {
                    closureHelper(f.lhs)
                    closureHelper(f.rhs)
                }
                else -> {}
            }
        }

        closureHelper(this)
        return res
    }

    override fun toString() = when (this) {
        is False -> "false"
        is True -> "true"
        is Variable -> name
        is Not -> "!($expression)"
        is And -> "($lhs && $rhs)"
        is Or -> "($lhs || $rhs)"
        is Next -> "N ($expression)"
        is Future -> "F ($expression)"
        is Globally -> "G ($expression)"
        is Until -> "($lhs U $rhs)"
        is Release -> "($lhs R $rhs)"
    }

    companion object {
        fun neg(f: LtlExpression): LtlExpression = when (f) {
            is False -> True
            is True -> False
            is Not -> f.expression
            else -> Not(f)
        }

        fun toNNF(formula: LtlExpression): LtlExpression {
            fun substituteFuture(f: Future) = Until(True, f.expression)
            fun substituteGlobally(f: Globally) = Release(False, f.expression)

            val result = when (formula) {
                is False, True -> formula
                is Variable -> formula

                is Not -> when (formula.expression) {
                    is False -> True
                    is True -> False
                    is Variable -> formula

                    is Not -> toNNF(formula.expression.expression)
                    is And -> Or(toNNF(Not(formula.expression.lhs)), toNNF(Not(formula.expression.rhs)))
                    is Or -> And(toNNF(Not(formula.expression.lhs)), toNNF(Not(formula.expression.rhs)))

                    is Next -> toNNF(Next(Not(formula.expression.expression)))
                    is Future -> toNNF(Not(substituteFuture(formula.expression)))
                    is Globally -> toNNF(Not(substituteGlobally(formula.expression)))

                    is Until -> Release(toNNF(Not(formula.expression.lhs)), toNNF(Not(formula.expression.rhs)))
                    is Release -> Until(toNNF(Not(formula.expression.lhs)), toNNF(Not(formula.expression.rhs)))
                }

                is And -> And(toNNF(formula.lhs), toNNF(formula.rhs))
                is Or -> Or(toNNF(formula.lhs), toNNF(formula.rhs))

                is Next -> Next(toNNF(formula.expression))
                is Future -> toNNF(substituteFuture(formula))
                is Globally -> toNNF(substituteGlobally(formula))

                is Until -> Until(toNNF(formula.lhs), toNNF(formula.rhs))
                is Release -> Release(toNNF(formula.lhs), toNNF(formula.rhs))
            }

            assert(result.inNNF())

            return result
        }
    }
}

object False : LtlExpression()
object True : LtlExpression()

data class Variable(val name: String) : LtlExpression() {
    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        if (other?.javaClass != javaClass) {
            return false
        }

        other as Variable
        return name == other.name
    }

    override fun hashCode(): Int = name.hashCode()
}

data class Not(val expression: LtlExpression) : LtlExpression() {
    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        if (other?.javaClass != javaClass) {
            return false
        }

        other as Not

        return expression == other.expression
    }

    override fun hashCode(): Int = expression.hashCode()
}

data class And(val lhs: LtlExpression, val rhs: LtlExpression) : LtlExpression() {
    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        if (other?.javaClass != javaClass) {
            return false
        }

        other as And
        return lhs == other.lhs && rhs == other.rhs
    }

    override fun hashCode(): Int = 31 * lhs.hashCode() + rhs.hashCode()
}

data class Or(val lhs: LtlExpression, val rhs: LtlExpression) : LtlExpression() {
    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        if (other?.javaClass != javaClass) {
            return false
        }

        other as Or
        return lhs == other.lhs && rhs == other.rhs
    }

    override fun hashCode(): Int = 31 * lhs.hashCode() + rhs.hashCode()
}

data class Next(val expression: LtlExpression) : LtlExpression() {
    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        if (other?.javaClass != javaClass) {
            return false
        }

        other as Next
        return expression == other.expression
    }

    override fun hashCode(): Int = expression.hashCode()
}

data class Future(val expression: LtlExpression) : LtlExpression() {
    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        if (other?.javaClass != javaClass) {
            return false
        }

        other as Future
        return expression == other.expression
    }

    override fun hashCode(): Int = expression.hashCode()
}

data class Globally(val expression: LtlExpression) : LtlExpression() {
    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        if (other?.javaClass != javaClass) {
            return false
        }

        other as Globally
        return expression == other.expression
    }

    override fun hashCode(): Int = expression.hashCode()
}

data class Until(val lhs: LtlExpression, val rhs: LtlExpression) : LtlExpression() {
    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        if (other?.javaClass != javaClass) {
            return false
        }

        other as Until
        return lhs == other.lhs && rhs == other.rhs
    }

    override fun hashCode(): Int = lhs.hashCode() * 31 + rhs.hashCode()
}

data class Release(val lhs: LtlExpression, val rhs: LtlExpression) : LtlExpression() {
    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        if (other?.javaClass != javaClass) {
            return false
        }

        other as Release
        return lhs == other.lhs && rhs == other.rhs
    }

    override fun hashCode(): Int = lhs.hashCode() * 31 + rhs.hashCode()
}
