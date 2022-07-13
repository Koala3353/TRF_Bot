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
    public boolean equals(Object obj) {
        if (obj instanceof SpecialPost other) {
            return this.unixTime == other.unixTime && this.interactionCount == other.interactionCount;
        }
        return false;
    }

    @Override
    public int compareTo(@NotNull SpecialPost o) {
        return this.getInteractionCount() - o.getInteractionCount();
    }
}
