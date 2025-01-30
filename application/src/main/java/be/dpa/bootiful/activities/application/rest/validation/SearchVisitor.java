package be.dpa.bootiful.activities.application.rest.validation;

import cz.jirutka.rsql.parser.ast.AndNode;
import cz.jirutka.rsql.parser.ast.ComparisonNode;
import cz.jirutka.rsql.parser.ast.OrNode;
import cz.jirutka.rsql.parser.ast.RSQLVisitor;

import java.util.HashSet;
import java.util.Set;

/**
 * Search visitor to extract all filter fields.
 *
 * @author denis
 */
public class SearchVisitor implements RSQLVisitor<Set<String>, Void> {

    private final FieldExtractor fieldExtractor = new FieldExtractor();

    private final Set<String> filterFields = new HashSet<>();

    @Override
    public Set<String> visit(AndNode node, Void param) {
        filterFields.addAll(fieldExtractor.extractFrom(node));
        return filterFields;
    }

    @Override
    public Set<String> visit(OrNode node, Void param) {
        filterFields.addAll(fieldExtractor.extractFrom(node));
        return filterFields;
    }

    @Override
    public Set<String> visit(ComparisonNode node, Void param) {
        String selector = fieldExtractor.extractFrom(node);
        filterFields.add(selector);
        return filterFields;
    }
}
