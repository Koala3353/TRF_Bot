package com.general_hello.bot.objects;

import org.jetbrains.annotations.NotNull;

public record SpecialPost(long unixTime, int interactionCount) implements Comparable<SpecialPost> {

    public long getUnixTime() {
        return unixTime;
    }

    public int getInteractionCount() {
        return interactionCount;
    }

    @Override
    public int compareTo(@NotNull SpecialPost o) {
        return this.getInteractionCount() - o.getInteractionCount();
    }
}
