package model;

import java.util.List;

public class PatternGroup {
    private final String id;
    private final List<String> pattern; // อันดับ 1,2,3 ของรูปแบบที่ซ้ำกัน
    private final List<String> ballotIds;
    private GroupStatus status;

    public PatternGroup(String id, List<String> pattern, List<String> ballotIds) {
        this.id = id;
        this.pattern = pattern;
        this.ballotIds = ballotIds;
        this.status = GroupStatus.PENDING_REVIEW;
    }

    public String getId() {
        return id;
    }

    public List<String> getPattern() {
        return pattern;
    }

    public List<String> getBallotIds() {
        return ballotIds;
    }

    public GroupStatus getStatus() {
        return status;
    }

    public void setStatus(GroupStatus status) {
        this.status = status;
    }
}
