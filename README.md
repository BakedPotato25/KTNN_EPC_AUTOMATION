# KTNN EPC Automation

Automation framework (Java Selenium + TestNG) dùng để tự động hoá test case cấp **FUNC**
(chức năng) cho một hệ thống nội bộ thật của công ty. Đây là project thật phục vụ công việc
thực tập, không phải bài tập.

Framework được scaffold từ `labit-web-selenium-0326` (framework khoá học LabIT), giữ nguyên
kiến trúc/convention, đã đổi package `com.testek` → `com.ktnn` và bỏ toàn bộ nội dung nghiệp vụ
của website khoá học.

**Đọc [CLAUDE.md](CLAUDE.md) trước khi thêm page, test script hoặc bất kỳ class nào mới** —
đó là tài liệu kiến trúc/convention đầy đủ, có cả lưu ý bảo mật bắt buộc phải theo.

## Bối cảnh dự án

- **Nguồn test case**: file test log Excel `KTNN_EPC.xlsx` (sheet `PL_FUNC`/`CC_FUNC`/
  `CCG_FUNC`) — chỉ tự động hoá test case cấp **FUNC** (nghiệp vụ), không làm test case cấp GUI
  (kiểm tra hiển thị/giao diện thuần tuý — hướng đó dùng công cụ khác, xem ghi chú bên dưới).
- **Hệ thống test**: một hệ thống nội bộ **thật** của công ty, không phải môi trường demo.
  Chi tiết quirk hệ thống được bổ sung dần khi viết test case thật.
- **Automation vẫn thao tác qua UI** bằng Selenium theo đúng kiến trúc Page Object Model
  (Locator → Objects → Page) — "FUNC" ở đây là loại test case, không phải phương pháp
  automation khác.
- **Không nằm trong repo này**: kiểm thử GUI/giao diện (font, màu sắc, alignment...) — team có
  một hướng riêng dùng GitHub Copilot CLI + Playwright MCP điều khiển bằng prompt, tách biệt
  hoàn toàn với project Java/Selenium này.

## Bảo mật & vệ sinh dữ liệu test

Vì đây là hệ thống thật của công ty, **bắt buộc đọc mục 0 trong [CLAUDE.md](CLAUDE.md)** trước
khi code. Tóm tắt:
- Không hard-code tài khoản/mật khẩu — luôn qua `env.json`/`database.json`, không commit giá
  trị thật lên git.
- Không log dữ liệu nhạy cảm ra report/screenshot/video.
- Không đưa link/tài liệu nội bộ (Google Sheet test log, path máy cá nhân...) vào file trong repo.
- **Test tạo dữ liệu mới trên hệ thống dev phải tự dọn dẹp (xoá) sau khi chạy xong** — dev là môi
  trường dùng chung cho cả team, không được để lại rác. Xem quy tắc cleanup cụ thể ở mục 5 của
  CLAUDE.md.

## Tech Stack
- **Ngôn ngữ / Build**: Java 17, Maven
- **UI Automation**: Selenium WebDriver 4, WebDriverManager (tự tải driver)
- **Test Runner**: TestNG (`ExecutionSuite.xml`), Cucumber (optional, chưa dùng)
- **API Testing**: REST Assured
- **Data-driven**: TestNG DataProvider, JSON (chính), Excel (Apache POI/FastExcel/JXLS), JavaFaker
- **Database**: JDBC (MSSQL/MySQL/PostgreSQL/Oracle) + Hibernate, verify dữ liệu sau khi thao tác UI
- **Report**: ExtentReports (HTML + screenshot/video khi fail), Allure, email report
- **Log**: Log4j2 / SLF4J

## Cấu trúc project
```
src/main/java/com/ktnn/
├── driver/            Factory tạo WebDriver (Chrome/Firefox/Edge/Safari) + DriverManager
├── controller/        WebUI - wrapper toàn bộ thao tác Selenium (click, input, wait...)
├── database/          Factory kết nối multi-DB + repository pattern để verify dữ liệu
├── datadriven/        Base class cho data provider/model
├── report/            ExtentReports + email report
├── listeners/         TestNG listener (retry, annotation transformer)
├── annotations/       Custom annotation (@TFSLink, @FrameAnnotation)
├── exceptions/        Exception riêng của framework
└── utils/             Config loader, date/time, faker, Excel, JSON helper

src/test/java/com/ktnn/projects/
├── common/            TestBase, BasePage, PropertiesUtils, TestListener
├── pages/              locator / objects / pages (Page Object Model, 3 tầng bắt buộc)
├── dataprovider/       model / providers (input cho data-driven test)
└── testscript/         Test class TestNG

src/test/resources/
├── config/config.properties   Config chạy: browser, env, wait, report flags
├── config/env.json             Base URL/account theo từng env (chưa điền giá trị thật)
├── config/database.json        Danh sách kết nối DB theo từng env (chưa điền giá trị thật)
├── data/                        Test data (JSON/Excel) theo từng env
├── script/                      JS snippet inject qua Selenium
└── suites/ExecutionSuite.xml   Entry point chạy bằng TestNG
```

Scaffold hiện tại mới có framework layer, chưa có page nghiệp vụ nào. Theo
[CLAUDE.md](CLAUDE.md) để thêm page đầu tiên (Locator → Objects → Page → Model → Provider →
Test) đúng convention.

## Yêu cầu môi trường

Xem hướng dẫn cài đặt chi tiết từng bước + lỗi hay gặp ở [SETUP.md](SETUP.md). Tóm tắt:
- JDK 17+
- Maven 3.6+
- Google Chrome (browser mặc định; driver tự tải qua WebDriverManager)

## Chạy test
Chạy suite mặc định (cấu hình trong `pom.xml` qua `<suite.name>`):
```bash
mvn test
```

Chạy chỉ định 1 file suite TestNG:
```bash
mvn test -DsuiteXmlFile=src/test/resources/suites/ExecutionSuite.xml
```

Các config chạy chính nằm ở `src/test/resources/config/config.properties`:
- `exeBrowser` / `exeHeadlessMode` — browser & bật/tắt headless
- `exeTarget` / `exeRemoteURL` / `exeRemotePort` — chạy local hay remote (Selenium Grid)
- `exeLanguage` — `vi` / `en` (xem `language_vi.properties` / `language_en.properties`)
- `screenshotFailedSteps` / `videoRecord` — chụp/quay khi test fail
- `retryFailedTests` / `retryCount` — tự retry test flaky
- `exeDBVerification` — bật `true` khi `config/database.json` đã có kết nối thật

## Report
Sau khi chạy, report nằm ở `reports/` (ExtentReports HTML + screenshot/video khi fail) và
Allure results ở `target/allure-results`. Cả 2 thư mục đều bị git-ignore — chạy lại suite để tự
sinh ra local.
