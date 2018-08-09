package xyz.faizaan.prison.integration;

import xyz.faizaan.prison.internal.Player;

import java.util.function.Function;

/**
 * An integration into a placeholder plugin.
 */
public interface PlaceholderIntegration extends Integration {

    void registerPlaceholder(String placeholder, Function<Player, String> action);

    @Override
    default IntegrationType getType() {
        return IntegrationType.PLACEHOLDER;
    }

}
