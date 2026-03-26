package com.bondarenko.algo.cache;

import java.util.Objects;

public class Investigation {
    public String sourceID;
    public String targetID;
    public RelationType relation;

    public Investigation(String sourceID, String targetID, RelationType relation) {
        this.sourceID = sourceID;
        this.targetID = targetID;
        this.relation = relation;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Investigation that = (Investigation) o;
        return Objects.equals(sourceID, that.sourceID) && Objects.equals(targetID, that.targetID) && relation == that.relation;
    }

    @Override
    public int hashCode() {
        return Objects.hash(sourceID, targetID, relation);
    }

    @Override
    public String toString() {
        return "Investigation{" +
                "sourceID='" + sourceID + '\'' +
                ", targetID='" + targetID + '\'' +
                ", relation=" + relation +
                '}';
    }
}
