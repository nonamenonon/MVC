import controller.ActionResult;
import controller.ElectionController;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import model.Election;
import model.ElectionStatus;
import model.PatternGroup;
import util.SeedDataLoader;

public class TestRunner {

    public static void run(String seedPath) {
        try {
            System.out.println("\n================ เริ่มรันชุดทดสอบ T1-T6 ================");
            Election election = SeedDataLoader.load(seedPath);
            ElectionController controller = new ElectionController(election);

            ActionResult r1 = controller.castVote("V04", Arrays.asList("C01", "C02", "C03"));
            boolean t1Pass = r1.isSuccess() && election.getVoters().get("V04").hasVoted();
            report("T1", t1Pass, r1.getMessage());

            ActionResult r2 = controller.castVote("V04", Arrays.asList("C04", "C05", "C01"));
            boolean t2Pass = !r2.isSuccess();
            report("T2", t2Pass, r2.getMessage());

            ActionResult r3 = controller.castVote("V05", Arrays.asList("C04", "C04", "C02"));
            boolean t3Pass = !r3.isSuccess() && !election.getVoters().get("V05").hasVoted();
            report("T3", t3Pass, r3.getMessage());

            ActionResult r4 = controller.castVote("V05", Arrays.asList("C04", "C05", "C01"));
            boolean t4Pass = r4.isSuccess();
            report("T4", t4Pass, r4.getMessage());

            ActionResult r5 = controller.closeVoting();
            List<PatternGroup> pending = controller.getPendingGroups();
            boolean t5Pass = r5.isSuccess()
                    && pending.size() == 1
                    && pending.get(0).getBallotIds().size() == 3
                    && String.join(">", pending.get(0).getPattern()).equals("C01>C02>C03");
            report("T5", t5Pass, "กลุ่มรอตรวจสอบ: " + pending.size() + " กลุ่ม");

            String groupId = pending.get(0).getId();
            ActionResult r6 = controller.resolveGroup(groupId, true);
            Map<String, Integer> scores = controller.computeResults();
            boolean t6Pass = r6.isSuccess()
                    && election.getStatus() == ElectionStatus.FINALIZED
                    && scores.get("C01") == 10
                    && scores.get("C02") == 9
                    && scores.get("C03") == 5
                    && scores.get("C04") == 4
                    && scores.get("C05") == 2;
            report("T6", t6Pass, "คะแนน: " + scores);

            System.out.println("================ จบชุดทดสอบ ================\n");
        } catch (Exception ex) {
            System.out.println("เกิดข้อผิดพลาดระหว่างรันชุดทดสอบ: " + ex.getMessage());
        }
    }

    private static void report(String caseName, boolean pass, String detail) {
        System.out.println(caseName + " : " + (pass ? "ผ่าน" : "ไม่ผ่าน") + " (" + detail + ")");
    }
}
