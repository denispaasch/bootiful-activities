package be.dpa.bootiful.activities;

import org.flywaydb.core.internal.publishing.PublishingConfigurationExtension;
import org.springframework.aot.hint.MemberCategory;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;
import org.springframework.core.io.ClassPathResource;

/**
 * Registers runtime hints for the bootiful activation application.
 *
 * @author denis
 */
public class BootifulRuntimeHints implements RuntimeHintsRegistrar {
    @Override
    public void registerHints(RuntimeHints hints, ClassLoader classLoader) {
        // For Flyway
        hints.reflection().registerType(PublishingConfigurationExtension.class, MemberCategory.values());
        hints.resources().registerResource(new ClassPathResource("boredapi-truststore.jks"));
    }
}
