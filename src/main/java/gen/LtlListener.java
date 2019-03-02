package gen;// Generated from /home/dmitry/Documents/work/git/ltl-verifier/src/main/antlr4/Ltl.g4 by ANTLR 4.7.2
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link LtlParser}.
 */
public interface LtlListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by the {@code next}
	 * labeled alternative in {@link LtlParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterNext(LtlParser.NextContext ctx);
	/**
	 * Exit a parse tree produced by the {@code next}
	 * labeled alternative in {@link LtlParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitNext(LtlParser.NextContext ctx);
	/**
	 * Enter a parse tree produced by the {@code negation}
	 * labeled alternative in {@link LtlParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterNegation(LtlParser.NegationContext ctx);
	/**
	 * Exit a parse tree produced by the {@code negation}
	 * labeled alternative in {@link LtlParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitNegation(LtlParser.NegationContext ctx);
	/**
	 * Enter a parse tree produced by the {@code boolean}
	 * labeled alternative in {@link LtlParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterBoolean(LtlParser.BooleanContext ctx);
	/**
	 * Exit a parse tree produced by the {@code boolean}
	 * labeled alternative in {@link LtlParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitBoolean(LtlParser.BooleanContext ctx);
	/**
	 * Enter a parse tree produced by the {@code conjunction}
	 * labeled alternative in {@link LtlParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterConjunction(LtlParser.ConjunctionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code conjunction}
	 * labeled alternative in {@link LtlParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitConjunction(LtlParser.ConjunctionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code disjunction}
	 * labeled alternative in {@link LtlParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterDisjunction(LtlParser.DisjunctionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code disjunction}
	 * labeled alternative in {@link LtlParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitDisjunction(LtlParser.DisjunctionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code future}
	 * labeled alternative in {@link LtlParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterFuture(LtlParser.FutureContext ctx);
	/**
	 * Exit a parse tree produced by the {@code future}
	 * labeled alternative in {@link LtlParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitFuture(LtlParser.FutureContext ctx);
	/**
	 * Enter a parse tree produced by the {@code globally}
	 * labeled alternative in {@link LtlParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterGlobally(LtlParser.GloballyContext ctx);
	/**
	 * Exit a parse tree produced by the {@code globally}
	 * labeled alternative in {@link LtlParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitGlobally(LtlParser.GloballyContext ctx);
	/**
	 * Enter a parse tree produced by the {@code release}
	 * labeled alternative in {@link LtlParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterRelease(LtlParser.ReleaseContext ctx);
	/**
	 * Exit a parse tree produced by the {@code release}
	 * labeled alternative in {@link LtlParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitRelease(LtlParser.ReleaseContext ctx);
	/**
	 * Enter a parse tree produced by the {@code implication}
	 * labeled alternative in {@link LtlParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterImplication(LtlParser.ImplicationContext ctx);
	/**
	 * Exit a parse tree produced by the {@code implication}
	 * labeled alternative in {@link LtlParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitImplication(LtlParser.ImplicationContext ctx);
	/**
	 * Enter a parse tree produced by the {@code variable}
	 * labeled alternative in {@link LtlParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterVariable(LtlParser.VariableContext ctx);
	/**
	 * Exit a parse tree produced by the {@code variable}
	 * labeled alternative in {@link LtlParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitVariable(LtlParser.VariableContext ctx);
	/**
	 * Enter a parse tree produced by the {@code until}
	 * labeled alternative in {@link LtlParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterUntil(LtlParser.UntilContext ctx);
	/**
	 * Exit a parse tree produced by the {@code until}
	 * labeled alternative in {@link LtlParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitUntil(LtlParser.UntilContext ctx);
	/**
	 * Enter a parse tree produced by the {@code parenthesis}
	 * labeled alternative in {@link LtlParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterParenthesis(LtlParser.ParenthesesContext ctx);
	/**
	 * Exit a parse tree produced by the {@code parenthesis}
	 * labeled alternative in {@link LtlParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitParenthesis(LtlParser.ParenthesesContext ctx);
}