package view;

import controller.ActionResult;
import controller.ElectionController;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class VotingView {
    private final Scanner scanner;

    public VotingView(Scanner scanner) {
        this.scanner = scanner;
    }

    public void render(ElectionController controller) {
        System.out.println("\n=== ลงคะแนนเสียง ===");
        System.out.print("รหัสผู้มีสิทธิ์ (เช่น V04): ");
        String voterId = scanner.nextLine().trim();

        List<String> ranking = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            System.out.print("อันดับ " + i + " (รหัสผู้สมัคร): ");
            ranking.add(scanner.nextLine().trim());
        }

        ActionResult result = controller.castVote(voterId, ranking);
        printResult(result);
    }

    public void printResult(ActionResult result) {
        if (result.isSuccess()) {
            System.out.println("[สำเร็จ] " + result.getMessage());
        } else {
            System.out.println("[ปฏิเสธ] " + result.getMessage());
        }
    }
}
