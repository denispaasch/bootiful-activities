package be.dpa.bootiful.activities;

import org.jeasy.random.randomizers.AbstractRandomizer;


public class NoOfParticipantsRandomizer extends AbstractRandomizer<Integer> {

    private final int min;

    private final int max;

    public NoOfParticipantsRandomizer(int pMin, int pMax) {
        min = pMin;
        max = pMax;
    }

    @Override
    public Integer getRandomValue() {
        return random.nextInt(max - min + 1) + min;
    }
}


