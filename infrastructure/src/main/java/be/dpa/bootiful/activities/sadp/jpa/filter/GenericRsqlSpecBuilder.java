package be.dpa.bootiful.activities.sadp.jpa.filter;

import cz.jirutka.rsql.parser.ast.ComparisonNode;
import cz.jirutka.rsql.parser.ast.LogicalNode;
import cz.jirutka.rsql.parser.ast.LogicalOperator;
import cz.jirutka.rsql.parser.ast.Node;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Builder to create a specification from RSQL nodes.
 *
 * @param <T> the entity type
 */
public class GenericRsqlSpecBuilder<T> {

    /**
     * Creates a specification for the passed node.
     *
     * @param node a node implementation
     * @return a specification
     */
    public Specification<T> createSpecification(Node node) {
        if (node instanceof LogicalNode) {
            return createSpecification((LogicalNode) node);
        }
        if (node instanceof ComparisonNode) {
            return createSpecification((ComparisonNode) node);
        }
        return null;
    }

    /**
     * Creates a specification for a logical node.
     *
     * @param logicalNode the logical node
     * @return a specification
     */
    public Specification<T> createSpecification(LogicalNode logicalNode) {
        List<Specification> specs = logicalNode.getChildren()
                .stream()
                .map(node -> createSpecification(node))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        Specification<T> result = specs.get(0);
        if (logicalNode.getOperator() == LogicalOperator.AND) {
            for (int i = 1; i < specs.size(); i++) {
                result = Specification.where(result).and(specs.get(i));
            }
        } else if (logicalNode.getOperator() == LogicalOperator.OR) {
            for (int i = 1; i < specs.size(); i++) {
                result = Specification.where(result).or(specs.get(i));
            }
        }

        return result;
    }

    /**
     * Creates a specification for a comparison node.
     *
     * @param comparisonNode the comparison node
     * @return a specification
     */
    public Specification<T> createSpecification(ComparisonNode comparisonNode) {
        Specification<T> result = Specification.where(
                new GenericRsqlSpecification<T>(
                        comparisonNode.getSelector(),
                        comparisonNode.getOperator(),
                        comparisonNode.getArguments()
                )
        );
        return result;
    }
}