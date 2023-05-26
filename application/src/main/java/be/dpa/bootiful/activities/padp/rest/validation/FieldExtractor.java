package be.dpa.bootiful.activities.padp.rest.validation;

import cz.jirutka.rsql.parser.ast.ComparisonNode;
import cz.jirutka.rsql.parser.ast.LogicalNode;

import java.util.HashSet;
import java.util.Set;

/**
 * Extracts the fields from the passed nodes.
 *
 * @author denis
 */
public class FieldExtractor {

    /**
     * Extracts the fields from the children of a logical node.
     *
     * @param logicalNode the logical node to get the filter fields from
     * @return all contained filter fields
     */
    public Set<String> extractFrom(LogicalNode logicalNode) {
        Set<String> selectors = new HashSet<>();
        logicalNode.getChildren()
                .stream().forEach(node -> {
                    if (node instanceof ComparisonNode) {
                        selectors.add(extractFrom((ComparisonNode) node));
                    } else if (node instanceof LogicalNode) {
                        selectors.addAll(extractFrom((LogicalNode) node));
                    }
                });
        return selectors;
    }

    /**
     * Extracts the field from a comparison node which is actually only the selector.
     *
     * @param comparisonNode the comparison node to get the selector from
     * @return the filter field
     */
    public String extractFrom(ComparisonNode comparisonNode) {
        return comparisonNode.getSelector();
    }

}
