package view;

import controller.ElectionController;
import java.util.List;
import java.util.Map;
import model.BallotStatus;
import model.Candidate;
import model.Election;
import model.ElectionStatus;
import model.PatternGroup;

public class StatusResultView {
    public void render(ElectionController controller) {
        Election e = controller.getElection();
        System.out.println("\n=== สถานะการเลือกตั้ง: " + e.getStatus() + " ===");

        if (e.getStatus() == ElectionStatus.OPEN) {
            System.out.println("จำนวนบัตรที่รับแล้ว: " + e.getBallots().size());

        } else if (e.getStatus() == ElectionStatus.CLOSED) {
            List<PatternGroup> pending = controller.getPendingGroups();
            System.out.println("กลุ่มรอตรวจสอบ: " + pending.size() + " กลุ่ม");
            for (PatternGroup g : pending) {
                System.out.println("  " + g.getId() + " : " + String.join(">", g.getPattern())
                        + " (" + g.getBallotIds().size() + " บัตร)");
            }
            System.out.println("ผลชั่วคราว (นับเฉพาะบัตรที่รับรองแล้ว):");
            printScores(controller.computeResults(), e);

        } else if (e.getStatus() == ElectionStatus.FINALIZED) {
            System.out.println("คะแนนรวมสุดท้าย:");
            printScores(controller.computeResults(), e);
            System.out.println("จำนวนบัตรที่รับรอง: " + controller.countBallotsByStatus(BallotStatus.CERTIFIED));
            System.out.println("จำนวนบัตรที่ไม่นับ: " + controller.countBallotsByStatus(BallotStatus.DISCARDED));
        }
    }

    private void printScores(Map<String, Integer> scores, Election e) {
        for (Map.Entry<String, Integer> entry : scores.entrySet()) {
            Candidate c = e.getCandidates().get(entry.getKey());
            String name = (c != null) ? c.getName() : entry.getKey();
            System.out.println("  " + entry.getKey() + " (" + name + "): " + entry.getValue());
        }
    }
}
