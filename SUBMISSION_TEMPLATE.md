# SUBMISSION - Exit Exam MVC 1/2569 (อาทิตย์เช้า)

## 1. วิธีเปิดโปรแกรม
- ภาษา/เฟรมเวิร์ก: java
- Entry point / คำสั่งเปิดโปรแกรม: chcp 65001
javac -encoding UTF-8 -d out -sourcepath src src\Main.java src\TestRunner.java
    java -cp out Main

- หมายเหตุที่จำเป็น (ถ้ามี): 
chcp 65001 เกี่ยวกับ front ภาษาไทย ใน window

## 2. ตารางเชื่อมโยง Requirements

| Requirement | Model / Domain | Controller / Action | View / Screen |
|---|---|---|---|
| R1 |Voter, Ballot, BallotStatus | ElectionController.castVote()|	VotingView |
| R2 | Ballot, Candidate, Voter|ElectionController.castVote() |VotingView|
| R3 |Ballot, PatternGroup, GroupStatus, BallotStatus |ElectionController.closeVoting() | AdminReviewView|
| R4 |PatternGroup, GroupStatus, BallotStatus |ElectionController.resolveGroup(), getPendingGroups() |AdminReviewView |
| R5 |Election, Candidate, Ballot |Candidate, Ballot	ElectionController.computeResults(), countBallotsByStatus() |StatusResultView, CandidateListView |

## 3. ผลการทดสอบ

| กรณี | ผ่าน/ไม่ผ่าน | หมายเหตุ (เฉพาะที่จำเป็น) |
|---|---|---|
| T1 | ผ่าน| |
| T2 |ผ่าน | |
| T3 |ผ่าน | |
| T4 | ผ่าน| |
| T5 | ผ่าน| |
| T6 | ผ่าน| |

## 4. ความแตกต่างระหว่างแบบที่ออกกับโปรแกรมจริง (ถ้ามี)
ระบุไม่เกิน 3 ข้อ
1. ใน sequence diagram เเรียก setStatus(PENDING_REVIEW) แยกไปที่ PatternGroup หลัง create 
ในโค้ดจริง PatternGroup ตั้งสถานะเป็น PENDING_REVIEW ให้อัตโนมัติตั้งแต่ใน constructor ไม่มีการเรียก setStatus() แยกอีกครั้ง
2. ใน diagram หลัง closeVoting() แสดง "กลุ่มรอตรวจสอบและผลชั่วคราว" ให้เจ้าหน้าที่ทันที
ในโค้ดจริง AdminReviewView.closeVoting() จะพิมพ์แค่ข้อความสำเร็จ/ล้มเหลวเท่านั้น userต้องเลือกเมนู 5 (ดูกลุ่มรอตรวจสอบ) และเมนู 3 (ดูผลชั่วคราว) แยกเอง
3. diagram แสดงการเรียก getAllBallots() เป็น message แยกจาก Election แต่ในโค้ดจริงไม่ได้ทำmethod นี้ ElectionController เข้าถึงบัตรทั้งหมดผ่าน election.getBallots() 
## 5. บันทึกการใช้ Generative AI
หากไม่ได้ใช้ ให้ระบุ **ไม่ได้ใช้ Generative AI**

| เวลาโดยประมาณ | เครื่องมือ | ใช้เพื่ออะไร | นำคำแนะนำไปใช้อย่างไร |
|---|---|---|---|
|10:00 |claude | เลือกตั้งภาษาอังกฤษใช้คำว่า| เอาไปตั่งชื่อ class mrthod variable|
| 10:00|claude |สํญลักษณ์ลูกศรใน class diagram มีอะไรบ้างมีความหมายว่าอะไร | เช็คว่าใช้สัญลักษณ์ถูกไหม |
| 10:00|claude |สัญลักษณ์ใน sequence diagram มีอะไรบ้าง| เช็คว่าใช้สัญลักษณ์ถูกไหม |
|10:00|claude |เขียน parser เช็ค file seed_data.json| เอาไปสร้าง floder util |
|12:00|claude | เช็ค code ทำไงให้ code clean ขึ้น ควรแก้ตรงไหน หรือควรเปลี่ยนชื่อตัวแปรตรงไหนบ้าง| แก้ชื่อ ตัวแปรให้เหมือนกัน เช็ค null ให้ทุกไฟล์เหมือนกัน เรียง structure code ให้ไม่สะเปะสะปะ และให้อ่านง่าย |
