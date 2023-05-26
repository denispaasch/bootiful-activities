package be.dpa.bootiful.activities.application.rest.validation;

import cz.jirutka.rsql.parser.RSQLParser;
import cz.jirutka.rsql.parser.ast.Node;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Makes sure that the search filters contains only allowed fields.
 *
 * @author denis
 */
@Slf4j
public class SearchValidator implements ConstraintValidator<SearchConstraint, String> {

    private static final Set<String> ALLOWED_FILTER_FIELDS = Set.of("action", "type", "details");

    @Override
    public boolean isValid(String search, ConstraintValidatorContext context) {
        // No search filter, thus nothing to validate
        if (StringUtils.isEmpty(search)) {
            return true;
        }

        // Extract all filter fields
        Node rootNode = new RSQLParser().parse(search);
        Set<String> filterFields = rootNode.accept(new SearchVisitor());
        List<String> invalidFilterFields = filterFields.stream()
                .filter(field -> !ALLOWED_FILTER_FIELDS.contains(field)).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(invalidFilterFields)) {
            return true;
        }

        log.warn("Search filter contains invalid fields {}", invalidFilterFields);
        return false;
    }
}
