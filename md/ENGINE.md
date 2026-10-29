# Engine (com.br.engine) - flow ของใบคำขอที่ทุก Form API ใช้ร่วมกัน

ตัดสินใจ 2026-10-28: ไม่ใช้ API กลาง แต่ละ Form API (itemreq_api, supplier_request_api, customerrequest_api,
userrequest_api, softwarerequest_api) รัน engine ของตัวเอง เพื่อให้ API หนึ่งล่มแล้วไม่กระทบตัวอื่น
engine ใช้วิธี **copy ไฟล์** (ไม่ใช่ jar)

## กติกา

1. แก้ engine ที่ **servicerequest_api ก่อนเสมอ** แล้วรัน test ให้ผ่าน
2. copy ทั้ง package `src/main/java/com/br/engine` ไปทับของ API อื่น **ห้ามแก้ engine ใน API อื่นตรงๆ**
   (ถ้าแก้ไม่ตรงกัน engine แต่ละที่จะเพี้ยนไปเรื่อยๆ เหมือนรุ่นเก่า)
3. งานเฉพาะ Form (M3, เช็คค่าซ้ำ) อยู่ใน `com.br.form.FormInterfaceImpl` ของ API นั้น ไม่ใส่ใน engine
4. เงื่อนไขของ flow (ข้าม status, approver ตาม field, approve ทุก sub group, M3 ที่ status ไหน) ตั้งใน
   เมนู Process Flow / Group ไม่เขียนเป็น `if status == "60"` ในโค้ด

## ไฟล์ที่ต้อง copy

| ไฟล์ | หน้าที่ |
|---|---|
| `com/br/engine/*.java` (ทั้ง package) | engine |
| `src/test/java/com/br/engine/*.java` | test ของ engine (pom ต้องมี junit 4.13.2, mockito-inline 4.11.0, jsqlparser 4.6 scope test) |
| `com/br/utility/SqlLog.java` | log ของ PreparedStatement (engine ใช้) |
| `com/br/utility/ApproveLinkToken.java` | อ่าน token ของลิงก์ approve จาก email (ใช้ใน api_data) |

engine พึ่งของที่ทุก API มีอยู่แล้ว: `com.br.connection.ConnectDB2.doConnect()`,
`com.br.utility.ConvertResultSet.convertResultSetToJson`, jettison, log4j2, jersey-multipart (`FormDataBodyPart`)

## ของที่แต่ละ API ต้องมีเอง

1. **`com.br.form.FormInterfaceImpl implements FormInterface`** (ชื่อนี้ตายตัว engine เรียกตรง)
   - ไม่มี M3: `return Answer.unchanged(request);`
   - มี M3: ดู `request.status` / `request.requesttype` / `request.mode` แล้วเรียก PostM3 เดิม
     error = `throw new Exception(ข้อความให้ user)` ระบบจะไม่บันทึก approve
   - `mode = "check"` ห้ามเขียน M3 (อ่านอย่างเดียว เช่นเช็คเลขผู้เสียภาษีซ้ำ)
   - ตอบรหัสที่ M3 สร้าง: `return new Answer(request.data, itemCode);` (ขึ้นที่ FHDES2)
2. **config.properties** ทุก env (dev / uat / prd) เพิ่ม:
   ```
   FILEBASEDIR=...            (มีแล้วใน servicerequest_api, prd = /mnt/api_files หรือ D:/files/api_project)
   FILEFOLDER=itemrequest_files   (โฟลเดอร์ไฟล์แนบของ API นี้)
   WORKFLOWTOKEN=...          (token ที่ n8n ต้องการ ค่าเดียวกับ servicerequest_api)
   ```
   `DBNAME`, `DBM3NAME`, `WORKFLOW` มีอยู่แล้วทุก API
   prd บน Ubuntu: FILEBASEDIR = share ที่ mount ไว้ (`/mnt/api_files`, uid=tomcat) ดู `md/FILE_STORE_MOUNT.md`
   API ที่ deploy บนเครื่องอื่นต้อง mount share เดียวกันก่อน
3. **pom.xml** maven-surefire-plugin ส่ง FILEBASEDIR ให้ test (ไม่งั้น `mvn -P prd` เขียนไฟล์ test ลง FILEBASEDIR ของ prd):
   ```xml
   <configuration>
     <systemPropertyVariables>
       <FILEBASEDIR>${project.build.directory}/test-files</FILEBASEDIR>
     </systemPropertyVariables>
   </configuration>
   ```
4. **api_data** endpoint ของ form เรียก engine แทน prepareAdd / prepareUpdate เดิม:
   - POST: `ServiceFormEngine.prepareAddServiceForm(..., token, username ของ token)`
   - PUT: `ServiceFormEngine.prepareUpdateServiceForm(..., token, linkToken, caller)` โดย caller มาจาก
     `approveCaller(token, linkToken, serviceno, status)` (copy จาก servicerequest_api api_data)
   - GET ไฟล์: `ServiceFile.prepareGetToFileServer(...)`
   - DELETE: `ServiceFormEngine.cancelServiceForm(...)`
   - GET ฟอร์ม / ประวัติ: `FlowQuery.getServiceForm(...)`, `FlowQuery.getServiceFormHistory(...)`
   - GET flags (approve mode ALL): `MultiApproveData.approverFlags(...)`
   - path ของ endpoint ใช้ชื่อเดียวกับ servicerequest_api (`/data/serviceform`, `/serviceformhistory`,
     `/serviceformflags`, `/serviceformfile`) frontend เลือก API จาก `SERVICE_FORM_API` ใน constants
   - CORSFilter ต้องอนุญาต header `x-approve-token` (approve จากลิงก์ email)

## class ใน engine

| class | หน้าที่ |
|---|---|
| `ServiceFormEngine` | submit ครั้งแรก, approve / reject, cancel, เดิน flow ข้าม VACANT / condition |
| `ApproveGuard` | ตรวจก่อนทำอะไร: ใบคำขออยู่ status นี้, ยังไม่ approve, ผู้เรียก = vApproveBy อยู่ใน FAAPLI, submit = vRequester |
| `FormInterface` / `FormInterfaceCall` | สัญญากับ FormInterfaceImpl ของ API / การเรียกเมื่อ status ตั้ง PMM3IF |
| `MultiApproveData` | approve mode ALL (ทุก sub group ต้อง approve) |
| `FlowQuery` / `FlowInsert` / `FlowUpdate` / `FlowDelete` | SQL ของ SR_FLOWHEAD / SR_FLOWDETAIL / SR_FLOWAPPROVE / SR_FILE |
| `ServiceFile` | ไฟล์แนบ (เขียน / อ่าน / แทน / ลบ) ที่ FILEBASEDIR/FILEFOLDER |
| `Workflow` | แจ้ง n8n ส่ง email ให้ approver คนถัดไป (ส่งไม่ได้ = log เท่านั้น) |
| `ServiceForm`, `FlowStep`, `Result`, `EngineConfig` | key ของฟอร์ม, 1 status ของ flow, คำตอบ ok / nok, config |

## ลำดับย้ายแต่ละ API (ทีละตัว: TST, UAT แล้ว PRD)

1. branch ใหม่ใน API นั้น, copy engine + test, เพิ่ม config, เขียน `FormInterfaceImpl` (ย้าย block PostM3 เดิม)
2. api_data ชี้ endpoint form ไปที่ engine, ลบ prepareAdd / prepareUpdate เดิม
3. ตั้ง Process Flow ของ form บน TST: group type 1/2 + GMDES1, PMM3IF ตาม status ที่ M3 ทำงาน, condition
   (แถว default = ไม่ตรงแล้วข้าม status, sub group อื่นของ status = lane: ตรงแล้วอนุมัติแทน default,
   หลาย lane ตรง = รวมทุกคน เช่น CMRQ COLLECTION 30 LOCAL `saleTypeMain <> 2`, EXPORT `saleTypeMain = 2`,
   ITRQ WHS 20 `warehouseMain = A41`)
4. frontend ของ form ส่ง `vTitle` (FHDES1, ถ้า title ไม่ใช่ reasonMain), `vReference` (FHDES2 = code ที่ฟอร์ม
   ตัดสิน เช่น code preview ของ ICT panel, staff code; ไม่มี = เว้นว่าง) และ `vRequester` = user ที่ login
   engine เขียน FHDES1 / FHDES2 ใหม่ทุก submit / resubmit / approve (ค่าว่าง = คงค่าเดิม)
5. ทดสอบ submit / approve / reject / resubmit / ลิงก์ email / ไฟล์ / M3 ครบทุก request type
