package mapper

import gen.LtlBaseVisitor
import gen.LtlParser.*
import model.*

class LtlExpressionBuilder : LtlBaseVisitor<LtlExpression>() {
    override fun visitParentheses(ctx: ParenthesesContext?): LtlExpression =
        visit(ctx!!.getChild(1))

    override fun visitNegation(ctx: NegationContext?): LtlExpression =
            Not(visit(ctx!!.expression()))

    override fun visitConjunction(ctx: ConjunctionContext?): LtlExpression =
            And(visit(ctx!!.lhs), visit(ctx.rhs))

    override fun visitDisjunction(ctx: DisjunctionContext?): LtlExpression =
            Or(visit(ctx!!.lhs), visit(ctx.rhs))

    override fun visitImplication(ctx: ImplicationContext?): LtlExpression =
            Or(Not(visit(ctx!!.lhs)), visit(ctx.rhs))

    override fun visitNext(ctx: NextContext?): LtlExpression =
            Next(visit(ctx!!.expression()))

    override fun visitFuture(ctx: FutureContext?): LtlExpression =
            Future(visit(ctx!!.expression()))

    override fun visitGlobally(ctx: GloballyContext?): LtlExpression =
            Globally(visit(ctx!!.expression()))

    override fun visitUntil(ctx: UntilContext?): LtlExpression =
            Until(visit(ctx!!.lhs), visit(ctx.rhs))

    override fun visitRelease(ctx: ReleaseContext?): LtlExpression =
            Release(visit(ctx!!.lhs), visit(ctx.rhs))

    override fun visitVariable(ctx: VariableContext?): LtlExpression =
            Variable(ctx!!.ID().text)

    override fun visitBoolean(ctx: BooleanContext?): LtlExpression =
        when (ctx!!.text) {
            "false" -> False
            "true" -> True
            else -> throw AssertionError("unknown boolean value: ${ctx.text}")
        }
}