package gen;// Generated from /home/dmitry/Documents/work/git/ltl-verifier/src/main/antlr4/Ltl.g4 by ANTLR 4.7.2
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link LtlParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface LtlVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by the {@code next}
	 * labeled alternative in {@link LtlParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNext(LtlParser.NextContext ctx);
	/**
	 * Visit a parse tree produced by the {@code negation}
	 * labeled alternative in {@link LtlParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNegation(LtlParser.NegationContext ctx);
	/**
	 * Visit a parse tree produced by the {@code boolean}
	 * labeled alternative in {@link LtlParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBoolean(LtlParser.BooleanContext ctx);
	/**
	 * Visit a parse tree produced by the {@code conjunction}
	 * labeled alternative in {@link LtlParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConjunction(LtlParser.ConjunctionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code disjunction}
	 * labeled alternative in {@link LtlParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDisjunction(LtlParser.DisjunctionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code future}
	 * labeled alternative in {@link LtlParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFuture(LtlParser.FutureContext ctx);
	/**
	 * Visit a parse tree produced by the {@code globally}
	 * labeled alternative in {@link LtlParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGlobally(LtlParser.GloballyContext ctx);
	/**
	 * Visit a parse tree produced by the {@code release}
	 * labeled alternative in {@link LtlParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRelease(LtlParser.ReleaseContext ctx);
	/**
	 * Visit a parse tree produced by the {@code implication}
	 * labeled alternative in {@link LtlParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitImplication(LtlParser.ImplicationContext ctx);
	/**
	 * Visit a parse tree produced by the {@code variable}
	 * labeled alternative in {@link LtlParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVariable(LtlParser.VariableContext ctx);
	/**
	 * Visit a parse tree produced by the {@code until}
	 * labeled alternative in {@link LtlParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUntil(LtlParser.UntilContext ctx);
	/**
	 * Visit a parse tree produced by the {@code parenthesis}
	 * labeled alternative in {@link LtlParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParentheses(LtlParser.ParenthesesContext ctx);
}