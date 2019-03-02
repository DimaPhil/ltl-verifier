package model

class Symbol(val min: Set<LtlExpression>, val max: Set<LtlExpression>) {
    constructor(value: Set<LtlExpression>) : this(value, value)
    constructor(expression: LtlExpression) : this(setOf(expression))

    override fun toString() = "Symbol: <$min, $max>"
}

infix fun Symbol.subset(l: Symbol) = this.min.containsAll(l.min) && l.max.containsAll(this.max)
