package model;

import java.util.List;

public class Ballot {
    private final String id;
    private final String voterId;
    private final List<String> ranking;
    private BallotStatus status;
    private String groupId;

    public Ballot(String id, String voterId, List<String> ranking, BallotStatus status) {
        this.id = id;
        this.voterId = voterId;
        this.ranking = ranking;
        this.status = status;
        this.groupId = null;
    }

    public String getId() {
        return id;
    }

    public String getVoterId() {
        return voterId;
    }

    public List<String> getRanking() {
        return ranking;
    }

    public BallotStatus getStatus() {
        return status;
    }

    public void setStatus(BallotStatus status) {
        this.status = status;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
}
