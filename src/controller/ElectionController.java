package controller;

import java.util.*;
import model.*;

public class ElectionController {
    private final Election election;

    public ElectionController(Election election) {
        this.election = election;
    }

    public Election getElection() {
        return election;
    }

    public ActionResult castVote(String voterId, List<String> ranking) {
        if (election.getStatus() != ElectionStatus.OPEN) {
            return ActionResult.fail("การเลือกตั้งไม่ได้อยู่ในสถานะ OPEN จึงลงคะแนนไม่ได้");
        }

        Voter voter = election.getVoters().get(voterId);
        if (voter == null) {
            return ActionResult.fail("ไม่พบผู้มีสิทธิ์เลือกตั้งรหัส " + voterId);
        }
        if (!voter.isActive()) {
            return ActionResult.fail(voterId + " ไม่ใช่ผู้มีสิทธิ์ที่ Active");
        }
        if (voter.hasVoted()) {
            return ActionResult.fail(voterId + " เคยลงคะแนนแล้ว");
        }

        if (ranking == null || ranking.size() != 3) {
            return ActionResult.fail("ต้องจัดอันดับผู้สมัครให้ครบ 3 คน");
        }
        Set<String> distinct = new LinkedHashSet<>(ranking);
        if (distinct.size() != 3) {
            return ActionResult.fail("ผู้สมัครในบัตรต้องแตกต่างกันทั้ง 3 อันดับ");
        }
        for (String candidateId : ranking) {
            if (!election.getCandidates().containsKey(candidateId)) {
                return ActionResult.fail("ไม่พบผู้สมัครรหัส " + candidateId);
            }
        }

        String ballotId = election.nextBallotId();
        Ballot ballot = new Ballot(ballotId, voterId, new ArrayList<>(ranking), BallotStatus.SAVED);
        election.getBallots().put(ballotId, ballot);
        voter.setHasVoted(true);

        return ActionResult.ok("รับบัตร " + ballotId + " สำเร็จ");
    }

    public ActionResult closeVoting() {
        if (election.getStatus() != ElectionStatus.OPEN) {
            return ActionResult.fail("การเลือกตั้งไม่ได้อยู่ในสถานะ OPEN จึงปิดรับคะแนนไม่ได้");
        }
        election.setStatus(ElectionStatus.CLOSED);

        Map<String, List<Ballot>> byPattern = new LinkedHashMap<>();
        for (Ballot ballot : election.getBallots().values()) {
            String key = String.join(">", ballot.getRanking());
            byPattern.computeIfAbsent(key, k -> new ArrayList<>()).add(ballot);
        }

        int threshold = election.getDuplicatePatternThreshold();
        for (List<Ballot> group : byPattern.values()) {
            if (group.size() >= threshold) {
                String groupId = election.nextGroupId();
                List<String> pattern = new ArrayList<>(group.get(0).getRanking());
                List<String> ballotIds = new ArrayList<>();
                for (Ballot ballot : group) {
                    ballotIds.add(ballot.getId());
                }
                PatternGroup patternGroup = new PatternGroup(groupId, pattern, ballotIds);
                election.getPatternGroups().put(groupId, patternGroup);
                for (Ballot ballot : group) {
                    ballot.setStatus(BallotStatus.PENDING_REVIEW);
                    ballot.setGroupId(groupId);
                }
            } else {
                for (Ballot ballot : group) {
                    ballot.setStatus(BallotStatus.CERTIFIED);
                }
            }
        }

        return ActionResult.ok("ปิดรับคะแนนสำเร็จ พบ " + countPendingGroups() + " กลุ่มที่รอตรวจสอบ");
    }

    public ActionResult resolveGroup(String groupId, boolean certify) {
        if (election.getStatus() != ElectionStatus.CLOSED) {
            return ActionResult.fail("ตรวจกลุ่มบัตรได้เฉพาะขณะสถานะ CLOSED เท่านั้น (ปิดรับคะแนนก่อน หรือสรุปผลไปแล้ว)");
        }

        PatternGroup group = election.getPatternGroups().get(groupId);
        if (group == null) {
            return ActionResult.fail("ไม่พบกลุ่มบัตรรหัส " + groupId);
        }
        if (group.getStatus() != GroupStatus.PENDING_REVIEW) {
            return ActionResult.fail("กลุ่ม " + groupId + " ไม่ได้อยู่ในสถานะรอตรวจสอบ");
        }

        GroupStatus newGroupStatus = certify ? GroupStatus.CERTIFIED : GroupStatus.DISCARDED;
        BallotStatus newBallotStatus = certify ? BallotStatus.CERTIFIED : BallotStatus.DISCARDED;

        group.setStatus(newGroupStatus);
        for (String ballotId : group.getBallotIds()) {
            Ballot ballot = election.getBallots().get(ballotId);
            if (ballot != null) {
                ballot.setStatus(newBallotStatus);
            }
        }

        if (countPendingGroups() == 0) {
            election.setStatus(ElectionStatus.FINALIZED);
        }

        return ActionResult.ok("ตัดสินกลุ่ม " + groupId + " เป็น " + (certify ? "รับรอง" : "ไม่นับ") + " แล้ว");
    }

    public Map<String, Integer> computeResults() {
        Map<String, Integer> scores = new LinkedHashMap<>();
        for (String candidateId : election.getCandidates().keySet()) {
            scores.put(candidateId, 0);
        }
        int[] points = election.getRankingPoints();
        for (Ballot ballot : election.getBallots().values()) {
            if (ballot.getStatus() == BallotStatus.CERTIFIED) {
                List<String> ranking = ballot.getRanking();
                for (int i = 0; i < ranking.size() && i < points.length; i++) {
                    String candidateId = ranking.get(i);
                    scores.put(candidateId, scores.getOrDefault(candidateId, 0) + points[i]);
                }
            }
        }
        return scores;
    }

    public List<PatternGroup> getPendingGroups() {
        List<PatternGroup> result = new ArrayList<>();
        for (PatternGroup group : election.getPatternGroups().values()) {
            if (group.getStatus() == GroupStatus.PENDING_REVIEW) {
                result.add(group);
            }
        }
        return result;
    }

    public int countPendingGroups() {
        return getPendingGroups().size();
    }

    public int countBallotsByStatus(BallotStatus status) {
        int count = 0;
        for (Ballot ballot : election.getBallots().values()) {
            if (ballot.getStatus() == status) {
                count++;
            }
        }
        return count;
    }
}
