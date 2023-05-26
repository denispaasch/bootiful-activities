package be.dpa.bootiful.activities.sadp.jpa.filter;

import cz.jirutka.rsql.parser.ast.AndNode;
import cz.jirutka.rsql.parser.ast.ComparisonNode;
import cz.jirutka.rsql.parser.ast.OrNode;
import cz.jirutka.rsql.parser.ast.RSQLVisitor;
import org.springframework.data.jpa.domain.Specification;


/**
 * RSQL query visitor.
 *
 * @param <T> the entity type
 */
public class CustomRsqlVisitor<T> implements RSQLVisitor<Specification<T>, Void> {

    private GenericRsqlSpecBuilder<T> builder;

    public CustomRsqlVisitor() {
        builder = new GenericRsqlSpecBuilder<T>();
    }

    @Override
    public Specification<T> visit(AndNode node, Void unused) {
        return builder.createSpecification(node);
    }

    @Override
    public Specification<T> visit(OrNode node, Void unused) {
        return builder.createSpecification(node);
    }

    @Override
    public Specification<T> visit(ComparisonNode node, Void unused) {
        return builder.createSpecification(node);
    }
}
