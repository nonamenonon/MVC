package view;

import controller.ElectionController;
import model.Candidate;

public class CandidateListView {
    public void render(ElectionController controller) {
        System.out.println("\n=== รายชื่อผู้สมัคร ===");
        for (Candidate c : controller.getElection().getCandidates().values()) {
            System.out.println(c.getId() + " - " + c.getName());
        }
    }
}
