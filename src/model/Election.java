package model;

import java.util.LinkedHashMap;
import java.util.Map;

public class Election {
    private final String id;
    private final String title;
    private ElectionStatus status;
    private final int[] rankingPoints;
    private final int duplicatePatternThreshold;

    private final Map<String, Candidate> candidates = new LinkedHashMap<>();
    private final Map<String, Voter> voters = new LinkedHashMap<>();
    private final Map<String, Ballot> ballots = new LinkedHashMap<>();
    private final Map<String, PatternGroup> patternGroups = new LinkedHashMap<>();

    public Election(String id, String title, ElectionStatus status, int[] rankingPoints, int duplicatePatternThreshold) {
        this.id = id;
        this.title = title;
        this.status = status;
        this.rankingPoints = rankingPoints;
        this.duplicatePatternThreshold = duplicatePatternThreshold;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public ElectionStatus getStatus() {
        return status;
    }

    public void setStatus(ElectionStatus status) {
        this.status = status;
    }

    public int[] getRankingPoints() {
        return rankingPoints;
    }

    public int getDuplicatePatternThreshold() {
        return duplicatePatternThreshold;
    }

    public Map<String, Candidate> getCandidates() {
        return candidates;
    }

    public Map<String, Voter> getVoters() {
        return voters;
    }

    public Map<String, Ballot> getBallots() {
        return ballots;
    }

    public Map<String, PatternGroup> getPatternGroups() {
        return patternGroups;
    }

    public String nextBallotId() {
        int max = 0;
        for (String id : ballots.keySet()) {
            max = Math.max(max, extractNumber(id));
        }
        return String.format("B%02d", max + 1);
    }

    public String nextGroupId() {
        int max = 0;
        for (String id : patternGroups.keySet()) {
            max = Math.max(max, extractNumber(id));
        }
        return String.format("G%02d", max + 1);
    }

    private int extractNumber(String id) {
        try {
            return Integer.parseInt(id.substring(1));
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
