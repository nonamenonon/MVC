package view;

import controller.ActionResult;
import controller.ElectionController;
import java.util.List;
import java.util.Scanner;
import model.PatternGroup;

public class AdminReviewView {
    private final Scanner scanner;

    public AdminReviewView(Scanner scanner) {
        this.scanner = scanner;
    }

    public void closeVoting(ElectionController controller) {
        ActionResult result = controller.closeVoting();
        printResult(result);
    }

    public void listPendingGroups(ElectionController controller) {
        List<PatternGroup> groups = controller.getPendingGroups();
        System.out.println("\n=== กลุ่มบัตรรอตรวจสอบ ===");
        if (groups.isEmpty()) {
            System.out.println("ไม่มีกลุ่มรอตรวจสอบ");
            return;
        }
        for (PatternGroup g : groups) {
            System.out.println(g.getId() + " : " + String.join(">", g.getPattern())
                    + " (" + g.getBallotIds().size() + " บัตร: " + g.getBallotIds() + ")");
        }
    }

    public void resolveGroup(ElectionController controller) {
        System.out.print("รหัสกลุ่มที่จะตัดสิน (เช่น G01): ");
        String groupId = scanner.nextLine().trim();
        System.out.print("ตัดสินเป็น (1 = รับรอง, 2 = ไม่นับ): ");
        String choice = scanner.nextLine().trim();
        boolean certify = choice.equals("1");

        ActionResult result = controller.resolveGroup(groupId, certify);
        printResult(result);
    }

    private void printResult(ActionResult result) {
        if (result.isSuccess()) {
            System.out.println("[สำเร็จ] " + result.getMessage());
        } else {
            System.out.println("[ปฏิเสธ] " + result.getMessage());
        }
    }
}
