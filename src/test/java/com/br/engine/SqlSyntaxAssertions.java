package com.br.engine;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.SubSelect;
import net.sf.jsqlparser.statement.update.Update;
import net.sf.jsqlparser.statement.update.UpdateSet;

/**
 * Catches broken SQL syntax (missing comma, unbalanced paren, keyword in the
 * wrong place) in the string-concatenated queries built by FlowInsert /
 * FlowUpdate / FlowDelete / FlowQuery, without needing a real DB2/AS400
 * connection. JSqlParser is a dialect-agnostic syntax parser — it does not
 * know about SR_FLOWHEAD-style table/column names, so it can't catch a wrong
 * table name; pair this with an exact-match assertion on the table name for
 * that (see InsertDataAddServiceHeadTest).
 */
public final class SqlSyntaxAssertions {

	private SqlSyntaxAssertions() {
	}

	public static void assertValidSyntax(String sql) {
		try {
			CCJSqlParserUtil.parse(sql);
		} catch (Exception e) {
			throw new AssertionError("SQL has a syntax error: " + e.getMessage() + "\n\nSQL:\n" + sql, e);
		}
	}

	/**
	 * Parses an {@code INSERT INTO ... VALUES(...)} statement and, for the
	 * scalar subquery at the given 0-based position in the VALUES list (e.g.
	 * the {@code (SELECT ... WHERE ...)} some INSERTs use to compute the next
	 * auto-number), flattens its WHERE clause into left-hand-side ->
	 * right-hand-side pairs for every AND-chained comparison.
	 *
	 * <p>Only handles a top-level chain of ANDs — OR/nested-paren predicates
	 * are not split further, they're kept as a single combined entry keyed by
	 * their own left-hand text. That's enough to check "does the subquery
	 * filter on this column with this literal value", which is what every
	 * FHSRNO/PDORNO-style auto-numbering subquery in this codebase does.
	 */
	public static Map<String, String> subqueryWhereConditions(String sql, int valuesIndex) {
		try {
			Insert insert = (Insert) CCJSqlParserUtil.parse(sql);
			ExpressionList items = (ExpressionList) insert.getItemsList();
			Expression value = items.getExpressions().get(valuesIndex);

			if (!(value instanceof SubSelect)) {
				throw new IllegalArgumentException(
						"VALUES position " + valuesIndex + " is not a subquery, it's: " + value);
			}
			PlainSelect subquery = (PlainSelect) ((SubSelect) value).getSelectBody();

			Map<String, String> conditions = new LinkedHashMap<>();
			flattenAndEquals(subquery.getWhere(), conditions);
			return conditions;
		} catch (Exception e) {
			throw new AssertionError(
					"Could not extract subquery WHERE from VALUES position " + valuesIndex + ": " + e.getMessage()
							+ "\n\nSQL:\n" + sql,
					e);
		}
	}

	/**
	 * Parses an {@code INSERT INTO tbl (COL_A, COL_B, ...) VALUES(val_a, val_b, ...)}
	 * statement and zips the column list with the values list positionally,
	 * returning column name -> literal value text.
	 *
	 * <p>This is what actually proves "parameters are in the right order" — a
	 * plain {@code sql.contains("'someValue'")} check only proves the value
	 * exists somewhere in the string, not that it landed under the right
	 * column. If two values are accidentally swapped in the VALUES list
	 * relative to the column list, this map will show the wrong value under
	 * the right column name and the assertion comparing against the expected
	 * value will fail.
	 */
	public static Map<String, String> insertColumnValues(String sql) {
		try {
			Insert insert = (Insert) CCJSqlParserUtil.parse(sql);
			List<Column> columns = insert.getColumns();
			List<Expression> values = ((ExpressionList) insert.getItemsList()).getExpressions();

			if (columns.size() != values.size()) {
				throw new IllegalStateException(
						"column count (" + columns.size() + ") does not match value count (" + values.size() + ")");
			}

			Map<String, String> result = new LinkedHashMap<>();
			for (int i = 0; i < columns.size(); i++) {
				result.put(columns.get(i).getColumnName(), values.get(i).toString());
			}
			return result;
		} catch (Exception e) {
			throw new AssertionError("Could not extract INSERT column/value pairs: " + e.getMessage()
					+ "\n\nSQL:\n" + sql, e);
		}
	}

	/**
	 * Parses an {@code UPDATE tbl SET COL_A = val_a, COL_B = val_b, ... WHERE ...}
	 * statement and returns its SET-clause column name -> literal value text —
	 * the UPDATE-statement counterpart to {@link #insertColumnValues}.
	 *
	 * <p>JSqlParser 4.6 represents each comma-separated {@code col = val} entry
	 * in the SET clause as its own {@link UpdateSet}, one column/expression
	 * pair each — {@code Update.getColumns()}/{@code getExpressions()} only
	 * reflect the *first* entry and silently drop the rest, so this iterates
	 * {@code getUpdateSets()} instead.
	 */
	public static Map<String, String> updateSetValues(String sql) {
		try {
			Update update = (Update) CCJSqlParserUtil.parse(sql);
			Map<String, String> result = new LinkedHashMap<>();
			for (UpdateSet updateSet : update.getUpdateSets()) {
				List<Column> columns = updateSet.getColumns();
				List<Expression> values = updateSet.getExpressions();
				for (int i = 0; i < columns.size(); i++) {
					result.put(columns.get(i).getColumnName(), values.get(i).toString());
				}
			}
			return result;
		} catch (Exception e) {
			throw new AssertionError("Could not extract UPDATE SET column/value pairs: " + e.getMessage()
					+ "\n\nSQL:\n" + sql, e);
		}
	}

	/**
	 * Parses an {@code UPDATE tbl SET ... WHERE ...} statement and flattens its
	 * WHERE clause into left-hand-side -> right-hand-side pairs for every
	 * AND-chained comparison — the UPDATE-statement counterpart to
	 * {@link #deleteWhereConditions}.
	 */
	public static Map<String, String> updateWhereConditions(String sql) {
		try {
			Update update = (Update) CCJSqlParserUtil.parse(sql);
			Map<String, String> conditions = new LinkedHashMap<>();
			flattenAndEquals(update.getWhere(), conditions);
			return conditions;
		} catch (Exception e) {
			throw new AssertionError("Could not extract UPDATE WHERE conditions: " + e.getMessage()
					+ "\n\nSQL:\n" + sql, e);
		}
	}

	/**
	 * Parses a {@code DELETE tbl WHERE ...} statement and flattens its WHERE
	 * clause into left-hand-side -> right-hand-side pairs for every AND-chained
	 * comparison — the DELETE-statement counterpart to {@link #insertColumnValues}:
	 * proves a parameter landed under the right WHERE column instead of just
	 * being present somewhere in the string.
	 */
	public static Map<String, String> deleteWhereConditions(String sql) {
		try {
			Delete delete = (Delete) CCJSqlParserUtil.parse(sql);
			Map<String, String> conditions = new LinkedHashMap<>();
			flattenAndEquals(delete.getWhere(), conditions);
			return conditions;
		} catch (Exception e) {
			throw new AssertionError("Could not extract DELETE WHERE conditions: " + e.getMessage()
					+ "\n\nSQL:\n" + sql, e);
		}
	}

	private static void flattenAndEquals(Expression expr, Map<String, String> out) {
		if (expr instanceof AndExpression) {
			AndExpression and = (AndExpression) expr;
			flattenAndEquals(and.getLeftExpression(), out);
			flattenAndEquals(and.getRightExpression(), out);
		} else if (expr instanceof EqualsTo) {
			EqualsTo eq = (EqualsTo) expr;
			out.put(eq.getLeftExpression().toString(), eq.getRightExpression().toString());
		} else {
			out.put(expr.toString(), "");
		}
	}
}
