import controller.ElectionController;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import model.Election;
import util.SeedDataLoader;
import view.AdminReviewView;
import view.CandidateListView;
import view.StatusResultView;
import view.VotingView;

public class Main {
    public static void main(String[] args) throws Exception {

        System.setOut(new PrintStream(System.out, true, StandardCharsets.UTF_8));
        System.setErr(new PrintStream(System.err, true, StandardCharsets.UTF_8));

        String seedPath = args.length > 0 ? args[0] : "seed_data.json";

        Election election = SeedDataLoader.load(seedPath);
        ElectionController controller = new ElectionController(election);
        Scanner scanner = new Scanner(System.in, StandardCharsets.UTF_8);

        CandidateListView candidateListView = new CandidateListView();
        VotingView votingView = new VotingView(scanner);
        AdminReviewView adminReviewView = new AdminReviewView(scanner);
        StatusResultView statusResultView = new StatusResultView();

        boolean running = true;
        while (running) {
            System.out.println("\n=== " + election.getTitle() + " ===");
            System.out.println("1. ดูรายชื่อผู้สมัคร");
            System.out.println("2. ลงคะแนนเสียง (โหมดผู้มีสิทธิ์)");
            System.out.println("3. ดูสถานะ/ผลการเลือกตั้ง");
            System.out.println("4. ปิดรับคะแนน (โหมดเจ้าหน้าที่)");
            System.out.println("5. ดูกลุ่มบัตรรอตรวจสอบ (โหมดเจ้าหน้าที่)");
            System.out.println("6. ตัดสินกลุ่มบัตร (โหมดเจ้าหน้าที่)");
            System.out.println("7. รันชุดทดสอบ T1-T6 อัตโนมัติ (demo)");
            System.out.println("0. ออกจากโปรแกรม");
            System.out.print("เลือกเมนู: ");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    candidateListView.render(controller);
                    break;
                case "2":
                    votingView.render(controller);
                    break;
                case "3":
                    statusResultView.render(controller);
                    break;
                case "4":
                    adminReviewView.closeVoting(controller);
                    break;
                case "5":
                    adminReviewView.listPendingGroups(controller);
                    break;
                case "6":
                    adminReviewView.resolveGroup(controller);
                    break;
                case "7":
                    TestRunner.run(seedPath);
                    break;
                case "0":
                    running = false;
                    break;
                default:
                    System.out.println("กรุณาเลือกเมนูที่ถูกต้อง");
            }
        }

        scanner.close();
        System.out.println("จบการทำงานโปรแกรม");
    }
}
