package mapper

import gen.LtlBaseVisitor
import gen.LtlParser.*
import model.*

class LtlExpressionBuilder : LtlBaseVisitor<LtlExpression>() {
    override fun visitParenthesis(ctx: ParenthesisContext?): LtlExpression =
        visit(ctx!!.getChild(1))

    override fun visitNegation(ctx: NegationContext?) =
            Not(visit(ctx!!.expression()))

    override fun visitConjunction(ctx: ConjunctionContext?) =
            And(visit(ctx!!.lhs), visit(ctx.rhs))

    override fun visitDisjunction(ctx: DisjunctionContext?) =
            Or(visit(ctx!!.lhs), visit(ctx.rhs))

    override fun visitImplication(ctx: ImplicationContext?) =
            Or(Not(visit(ctx!!.lhs)), visit(ctx.rhs))

    override fun visitNext(ctx: NextContext?) =
            Next(visit(ctx!!.expression()))

    override fun visitFuture(ctx: FutureContext?) =
            Future(visit(ctx!!.expression()))

    override fun visitGlobally(ctx: GloballyContext?) =
            Globally(visit(ctx!!.expression()))

    override fun visitUntil(ctx: UntilContext?) =
            Until(visit(ctx!!.lhs), visit(ctx.rhs))

    override fun visitRelease(ctx: ReleaseContext?) =
            Release(visit(ctx!!.lhs), visit(ctx.rhs))

    override fun visitVariable(ctx: VariableContext?) =
            Variable(ctx!!.ID().text)

    override fun visitBoolean(ctx: BooleanContext?) =
        when (ctx!!.text) {
            "false" -> False
            "true" -> True
            else -> throw AssertionError("unknown boolean value: ${ctx.text}")
        }
}