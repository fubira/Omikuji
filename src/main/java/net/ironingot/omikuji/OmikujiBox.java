package net.ironingot.omikuji;

import java.util.Random;
import java.util.Map;
import java.lang.Integer;

public class OmikujiBox {
    private Map<String, Object> mapElements;

    public OmikujiBox(Map<String, Object> elements) {
        mapElements = elements;
    }

    private long getTotalWeight() {
        long totalWeight = 0;

        for (Map.Entry<String, Object> e: mapElements.entrySet()) {
            totalWeight = totalWeight + (Integer)(e.getValue());
        }

        return totalWeight;
    }

    public String draw(long seed) {
        long value = (long)(new Random(seed).nextDouble() * getTotalWeight() + 1);

        for (Map.Entry<String, Object> e: mapElements.entrySet()) {
            value = value - (Integer)(e.getValue());

            if (value <= 0) {
                return e.getKey();
            }
        }

        return "Null";
    }
}
