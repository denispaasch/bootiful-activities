package be.dpa.bootiful.activities;

import lombok.NoArgsConstructor;
import org.jeasy.random.api.Randomizer;

import java.util.UUID;

@NoArgsConstructor
public class AlternateKeyRandomizer implements Randomizer<String> {
    @Override
    public String getRandomValue() {
        return UUID.randomUUID().toString();
    }
}
