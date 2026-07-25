# Hướng dẫn cài đặt & chạy project

> **Ghi chú cho AI coding assistant (GitHub Copilot, Claude...) đang hỗ trợ setup máy này**:
> làm đúng thứ tự các bước bên dưới, chạy lệnh verify sau mỗi bước để xác nhận trước khi qua
> bước tiếp theo — đừng giả định bước trước đã đúng nếu chưa thấy output verify khớp. Nếu 1
> lệnh cài đặt (winget/choco) fail, xem ngay mục "Lỗi hay gặp" ở cuối file trước khi thử cách
> khác, vì các lỗi phổ biến nhất đã được ghi lại sẵn ở đó cùng cách xử lý đã kiểm chứng.

Project: automation Selenium + TestNG cho một hệ thống nội bộ thật của công ty. Đọc
[README.md](README.md) và [CLAUDE.md](CLAUDE.md) để hiểu kiến trúc/convention — file này chỉ
tập trung vào việc **cài đặt môi trường và chạy được test**, không nói về convention code.

## 1. Yêu cầu môi trường

| Phần mềm | Phiên bản | Lệnh kiểm tra |
|---|---|---|
| JDK | 17 | `java -version` |
| Maven | 3.6+ | `mvn -version` |
| Google Chrome | bản mới nhất | Chrome → menu ⋮ → Help → About Google Chrome |

Selenium 4 có **Selenium Manager tích hợp sẵn**, tự tải ChromeDriver khớp bản Chrome đã cài —
không cần cài ChromeDriver tay, không cần `WebDriverManager.chromedriver().setup()`.

## 2. Cài JDK 17

1. Verify trước: `java -version`. Nếu đã ra `17.x.x` → bỏ qua bước này.
2. Nếu chưa có hoặc bản khác 17: tải JDK 17 (Adoptium Temurin hoặc Oracle JDK) —
   https://adoptium.net/temurin/releases/?version=17
3. Set biến môi trường `JAVA_HOME` trỏ tới thư mục JDK vừa cài, thêm `%JAVA_HOME%\bin` vào PATH.
4. Đóng hẳn terminal/IDE rồi mở lại, verify lại `java -version`.

## 3. Cài Maven

1. Verify trước: `mvn -version`. Nếu đã có Maven 3.6+ và Java 17 → bỏ qua bước này.
2. Thử cài qua trình quản lý gói trước:
   - Windows: `winget install Apache.Maven`
   - Nếu có Chocolatey: `choco install maven`
3. **Nếu bước 2 fail** (package không có trong winget repo, hoặc choco cần quyền admin mà
   session không có) — tải thẳng bản zip, không cần quyền admin:
   1. Tải `apache-maven-3.9.x-bin.zip` tại https://maven.apache.org/download.cgi
   2. Giải nén vào 1 thư mục cố định, ví dụ `C:\tools\apache-maven-3.9.x`
   3. Set biến môi trường `MAVEN_HOME` trỏ vào thư mục đó, thêm `%MAVEN_HOME%\bin` vào PATH ở
      scope **User** (đủ quyền, không cần admin):
      ```powershell
      [Environment]::SetEnvironmentVariable("MAVEN_HOME", "C:\tools\apache-maven-3.9.x", "User")
      [Environment]::SetEnvironmentVariable("PATH", $env:PATH + ";C:\tools\apache-maven-3.9.x\bin", "User")
      ```
   4. **Đóng hẳn VSCode/terminal rồi mở lại** (không chỉ mở tab mới) để nhận PATH mới — đây là
      bước hay bị bỏ sót nhất, xem thêm ở mục "Lỗi hay gặp".
4. Verify: `mvn -version` phải ra đúng bản Maven và Java 17.

## 4. Cài Google Chrome

Tải và cài bản mới nhất tại https://www.google.com/chrome/ nếu máy chưa có.

## 5. Cấu hình tài khoản test (bắt buộc trước khi chạy được)

Project **không** commit tài khoản/mật khẩu thật lên git — cần điền tay trên từng máy:

1. Copy file mẫu (nếu máy chưa có sẵn `env.json`/`database.json` thật):
   - `src/test/resources/config/env.json.example` → `src/test/resources/config/env.json`
   - `src/test/resources/config/database.json.example` → `src/test/resources/config/database.json`
     (chỉ cần nếu muốn bật verify qua DB — mặc định `exeDBVerification=false` trong
     `config.properties` nên có thể bỏ qua file này)
2. Mở `env.json`, điền `baseUrl`/tài khoản thật vào khối `"sit"` (khớp `exeEnv=sit` trong
   `config.properties`):
   ```json
   "sit": {
     "baseUrl": "<base URL thật>",
     "account": "<tài khoản thật>",
     "password": "<mật khẩu thật>"
   }
   ```
3. Không cần thêm thao tác gì để giữ bí mật — `env.json`/`database.json` đã nằm trong
   `.gitignore`, chỉ cần không cố tình `git add -f` 2 file này.

## 6. Chạy test

Từ thư mục gốc project (nơi có `pom.xml`):
```bash
mvn test
```

Chạy đúng suite khai báo sẵn:
```bash
mvn test -DsuiteXmlFile=src/test/resources/suites/ExecutionSuite.xml
```

Chạy 1 class cụ thể (ví dụ chỉ chạy PickList):
```bash
mvn test -Dtest=PickListTest
```

Verify chạy thành công: cuối log thấy `Tests run: N, Failures: 0, Errors: 0`.

## 7. Xem kết quả

Report HTML nằm ở thư mục `reports/` (ExtentReports, tự sinh sau khi chạy xong, đã gitignore
nên chỉ có local — chạy lại suite để tự tạo ra).

## 8. Chạy bằng nút "Run Test" trong VSCode thay vì `mvn test`

VSCode Java Test Runner (nút ▶ trong Testing panel/CodeLens) launch JVM riêng, **không tự áp
dụng** flag JVM mà `pom.xml` đã cấu hình cho Maven Surefire. Project đã thêm sẵn flag đó vào
`.vscode/settings.json` (`java.test.config.vmArgs`) nên bấm nút Run Test vẫn chạy bình thường —
nếu thấy lỗi `InaccessibleObjectException`/Gson ở cuối bài test, kiểm tra `.vscode/settings.json`
có bị mất mục `java.test.config` không trước khi tra lại từ đầu.

## 9. Lỗi hay gặp

| Triệu chứng | Nguyên nhân | Cách xử lý |
|---|---|---|
| Problems panel báo lỗi đỏ khắp file dùng Lombok (`@Getter`, `@Slf4j`...) dù `mvn compile` chạy sạch | Extension "Oracle Java Platform" (`oracle.oracle-java`) cài song song với Java Extension Pack, không tương thích Lombok | Extensions panel → Disable (Workspace) `oracle.oracle-java`, giữ `redhat.java`/Java Extension Pack |
| `mvn`/`java` báo "not recognized" dù vừa cài xong | Terminal/IDE đang chạy từ trước khi PATH được cập nhật, chưa nhận biến môi trường mới | Đóng hẳn IDE/terminal rồi mở lại (không chỉ mở tab mới), verify lại bằng `mvn -version`/`java -version` |
| Test fail ngay từ bước login | `env.json` chưa điền tài khoản thật, hoặc tài khoản hết hạn/bị khoá trên hệ thống test | Kiểm tra lại mục 5 phía trên |
| `winget install Apache.Maven` hoặc `choco install maven` fail | Package không có trong winget repo, hoặc Chocolatey cần quyền admin mà session không có | Dùng cách cài qua zip ở mục 3, bước 3 — không cần quyền admin |
