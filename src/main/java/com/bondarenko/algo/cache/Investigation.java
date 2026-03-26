package com.bondarenko.algo.cache;

import java.util.Objects;

public class Investigation {
    public String sourceID;
    public String targetID;
    public RelationType relation;
    public String reason;

    public Investigation(String sourceID, String targetID, RelationType relation) {
        this(sourceID, targetID, relation, null);
    }

    public Investigation(String sourceID, String targetID, RelationType relation, String reason) {
        this.sourceID = sourceID;
        this.targetID = targetID;
        this.relation = relation;
        this.reason = reason;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Investigation that = (Investigation) o;
        return Objects.equals(sourceID, that.sourceID) && Objects.equals(targetID, that.targetID) && relation == that.relation && Objects.equals(reason, that.reason);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sourceID, targetID, relation, reason);
    }

    @Override
    public String toString() {
        return "Investigation{" +
                "sourceID='" + sourceID + '\'' +
                ", targetID='" + targetID + '\'' +
                ", relation=" + relation +
                ", reason='" + reason + '\'' +
                '}';
    }
}
