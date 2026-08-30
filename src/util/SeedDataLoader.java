package util;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import model.*;

public class SeedDataLoader {

    @SuppressWarnings("unchecked")
    public static Election load(String path) throws IOException {
        String content = new String(Files.readAllBytes(Paths.get(path)), StandardCharsets.UTF_8);
        Map<String, Object> root = (Map<String, Object>) SimpleJsonParser.parse(content);
        Map<String, Object> electionJson = (Map<String, Object>) root.get("election");

        String id = (String) electionJson.get("id");
        String title = (String) electionJson.get("title");
        ElectionStatus status = ElectionStatus.valueOf((String) electionJson.get("status"));

        List<Object> pointsRaw = (List<Object>) electionJson.get("ranking_points");
        int[] rankingPoints = new int[pointsRaw.size()];
        for (int i = 0; i < pointsRaw.size(); i++) {
            rankingPoints[i] = ((Double) pointsRaw.get(i)).intValue();
        }
        int threshold = ((Double) electionJson.get("duplicate_pattern_threshold")).intValue();

        Election election = new Election(id, title, status, rankingPoints, threshold);

        List<Object> candidatesRaw = (List<Object>) root.get("candidates");
        for (Object o : candidatesRaw) {
            Map<String, Object> c = (Map<String, Object>) o;
            Candidate candidate = new Candidate((String) c.get("id"), (String) c.get("name"));
            election.getCandidates().put(candidate.getId(), candidate);
        }

        List<Object> votersRaw = (List<Object>) root.get("voters");
        for (Object o : votersRaw) {
            Map<String, Object> v = (Map<String, Object>) o;
            boolean active = Boolean.TRUE.equals(v.get("active"));
            Voter voter = new Voter((String) v.get("id"), (String) v.get("name"), active);
            election.getVoters().put(voter.getId(), voter);
        }

        List<Object> ballotsRaw = (List<Object>) root.get("ballots");
        for (Object o : ballotsRaw) {
            Map<String, Object> b = (Map<String, Object>) o;
            String ballotId = (String) b.get("id");
            String voterId = (String) b.get("voter_id");
            List<Object> rankingRaw = (List<Object>) b.get("ranking");
            List<String> ranking = new ArrayList<>();
            for (Object r : rankingRaw) {
                ranking.add((String) r);
            }
            Ballot ballot = new Ballot(ballotId, voterId, ranking, BallotStatus.SAVED);
            election.getBallots().put(ballotId, ballot);

            Voter voter = election.getVoters().get(voterId);
            if (voter != null) {
                voter.setHasVoted(true);
            }
        }

        return election;
    }
}
