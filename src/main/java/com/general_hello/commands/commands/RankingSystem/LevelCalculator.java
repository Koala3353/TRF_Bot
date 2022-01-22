package com.general_hello.commands.commands.RankingSystem;

import java.util.function.Function;

public class LevelCalculator {
    private static final Function<Long, Long> CALCULATE_LEVEL = ep -> (long) (1 / (float) (8) * Math.sqrt(ep));
    private static final Function<Long, Long> CALCULATE_EP = level -> (long) 64 * (long) Math.pow(level, 2);

    public static long calculateLevel(long levelPoints){
        return CALCULATE_LEVEL.apply(levelPoints);
    }

    public static long calculateLevelMax(long level){
        return CALCULATE_EP.apply(level + 1);
    }
}
