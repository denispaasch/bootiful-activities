package be.dpa.bootiful.activities.sadp.jpa.filter;

import cz.jirutka.rsql.parser.ast.ComparisonOperator;
import cz.jirutka.rsql.parser.ast.RSQLOperators;
import lombok.Getter;

/**
 * The supported RSQL search operations.
 */
public enum RsqlSearchOperation {
    EQUAL(RSQLOperators.EQUAL),
    NOT_EQUAL(RSQLOperators.NOT_EQUAL),
    GREATER_THAN(RSQLOperators.GREATER_THAN),
    GREATER_THAN_OR_EQUAL(RSQLOperators.GREATER_THAN_OR_EQUAL),
    LESS_THAN(RSQLOperators.LESS_THAN),
    LESS_THAN_OR_EQUAL(RSQLOperators.LESS_THAN_OR_EQUAL),
    IN(RSQLOperators.IN),
    NOT_IN(RSQLOperators.NOT_IN);

    @Getter
    private ComparisonOperator operator;

    RsqlSearchOperation(ComparisonOperator operator) {
        this.operator = operator;
    }

    /**
     * Gets the corresponding RSQL search operation for a comparison operator.
     *
     * @param operator the comparison operation
     * @return the RSQL search operation
     */
    public static RsqlSearchOperation getSimpleOperator(ComparisonOperator operator) {
        for (RsqlSearchOperation operation : values()) {
            if (operation.getOperator() == operator) {
                return operation;
            }
        }
        return null;
    }
}
