# CLAUDE.md — KTNN EPC Automation

Hướng dẫn kiến trúc & convention cho project automation này. Đọc file này trước khi thêm
page, test script, hoặc bất kỳ class nào mới — mục tiêu là mọi code mới viết ra giống hệt
phong cách đã có, không tự sáng tạo pattern khác.

Project này được **scaffold từ framework của khoá học LabIT** (`labit-web-selenium-0326`),
giữ nguyên toàn bộ kiến trúc/convention của framework đó, chỉ đổi package
`com.testek` → `com.ktnn` và bỏ hết nội dung nghiệp vụ của website khoá học (Login/Product/
Order/Supplier/Customer/Category...) vì đây là project thật, không phải bài tập. Phần
`study/` (bài tập từng lesson) không được mang sang.

> Việc setup Selenium/Java/Maven extension trong VSCode (chạy được `mvn test`, debug được
> test, v.v.) là bước **làm sau**, chưa nằm trong phạm vi file này.

---

## 0. Bối cảnh nghiệp vụ & bảo mật

- **Mục đích**: Tự động hoá test case cấp **FUNC** (chức năng) từ file test log Excel
  `KTNN_EPC.xlsx` (sheet `PL_FUNC`/`CC_FUNC`/`CCG_FUNC`) — **không** tự động hoá test case
  cấp GUI (kiểm tra hiển thị/giao diện thuần tuý, nếu file test log có loại này thì bỏ qua,
  không nằm trong phạm vi project). "FUNC" ở đây là **loại test case** (nghiệp vụ), không
  phải một phương pháp automation khác: automation vẫn thao tác qua UI bằng Selenium theo
  đúng kiến trúc Page Object Model ở mục 3–4, chỉ khác là focus vào luồng nghiệp vụ mô tả
  trong test case, không cần verify pixel-perfect layout/màu sắc như test GUI.
- **Hệ thống test**: một hệ thống nội bộ **thật** của công ty (base URL thật lưu trong
  `env.json`, không commit lên git — xem mục 6), không phải môi trường demo/sandbox dùng để học.
- **Nguồn test-case**: dữ liệu test case gốc nằm ở file Excel/Google Sheet nội bộ; đường dẫn
  cụ thể được lưu ngoài phạm vi repo (xem lưu ý bảo mật ngay dưới đây) — khi cần map test case
  sang JSON test-data (mục 4.3), hỏi lại vị trí file nếu chưa có trong context. Xem cấu trúc
  file/quy trình lấy test case ở mục 0.1.

### Quirk hệ thống test (bắt buộc đọc trước khi viết Locator/Objects đầu tiên)

Hệ thống test là **single-spa micro-frontend** — khác hành vi bình thường của một SPA
thông thường ở mấy điểm sau, ảnh hưởng trực tiếp tới cách viết `driver`/`controller`/
`pages.objects`:

- **Không điều hướng bằng deep-link hash trực tiếp.** Gọi thẳng URL kiểu
  `#/<module>/characteristic-catalog` sẽ bị redirect về màn hình chủ thay vì load đúng trang —
  `PageManagement`/`BasePage.gotoXxxPage()` phải điều hướng bằng cách **click qua UI** (card
  trên grid trang chủ...), không set `driver.get(url)` thẳng tới trang con.
- **Click synthetic thường không được app nhận.** `WebElement.click()` gọi qua Selenium binding
  chuẩn có thể bị bỏ qua bởi single-spa — nếu gặp trường hợp click không có tác dụng, thử bắn
  đủ chuỗi sự kiện `mousedown` → `mouseup` → `click` bằng JS executor (`WebUI` đã có sẵn
  action gọi JS, ưu tiên tạo 1 method dùng chung trong `WebUI` thay vì viết riêng từng chỗ).
- **Panel edit khó định vị bằng locator cố định** vì cấu trúc DOM lặp lại giữa nhiều panel —
  cách ổn định hơn: đi ngược từ nút Save (`btnSubmit`) lên ancestor gần nhất có chữ "valid"
  xuất hiện trong `innerText` và độ dài dưới ~800 ký tự, dùng ancestor đó làm phạm vi tìm các
  input con thay vì tìm theo id cố định (id có thể trùng lặp giữa các panel).
- **Input ngày**: định vị qua `placeholder='dd/mm/yyyy'` thay vì `name`/`id` (không ổn định
  giữa các field ngày khác nhau).
- **Trường Version** trong panel: không có locator riêng biệt dễ nhận diện — hiện tại cách
  đáng tin cậy nhất là lấy **input text cuối cùng trong panel** (`(//input[@type='text'])[last()]`
  trong phạm vi panel đang mở).
- **Input chuỗi dài** (textarea/rich input) không nhận `sendKeys` bình thường ổn định — cần
  dùng pattern set giá trị qua **native value setter** (JS `Object.getOwnPropertyDescriptor`
  trên prototype input rồi dispatch `input` event) thay vì gọi `sendKeys` trực tiếp.

Những điểm trên nên đóng gói thành method dùng chung trong `WebUI` (`controller` package,
mục 3 CLAUDE.md) — ví dụ `clickByJS(WebElement)`, `findEditPanelByAncestor(...)`,
`setValueByNativeSetter(WebElement, String)` — để mọi `Objects` class dùng lại thay vì mỗi
page tự implement riêng.

### Lưu ý bảo mật (đọc kỹ trước khi code)

- Đây là hệ thống **thật** của công ty, không phải bài tập — mọi thao tác automation phải giới
  hạn trong phạm vi môi trường dev/sit được cấp quyền test, **không** nhắm vào production trừ
  khi được yêu cầu rõ ràng.
- **Không hard-code tài khoản/mật khẩu** trong code (Page/Objects/testscript) — luôn đọc qua
  `env.json`/`database.json` (mục 6). Hai file này sẽ chứa credentials thật khi điền dữ liệu —
  cân nhắc thêm vào `.gitignore` (hoặc tách file `.example` không có giá trị thật) **trước khi
  commit lần đầu có dữ liệu thật**.
- **Không log** thông tin nhạy cảm (mật khẩu, token, dữ liệu cá nhân/khách hàng thật) ra
  ExtentReport/log/screenshot — cẩn thận khi bật `screenshotAllSteps`/`videoRecord` ở màn hình
  có dữ liệu nhạy cảm.
- **Không đưa link/tài liệu nội bộ** (Google Sheet test log, đường dẫn thư mục máy cá nhân...)
  vào bất kỳ file nào trong repo (kể cả CLAUDE.md, comment code, commit message) — repo có thể
  được chia sẻ rộng hơn phạm vi cần thiết.
- Ưu tiên dùng **dữ liệu test giả lập** (`FakerUtils`) thay vì dữ liệu thật của khách hàng/đối
  tác khi tạo test data, kể cả trên môi trường dev.
- **Dọn dẹp dữ liệu sau khi test**: mọi test case tạo/import dữ liệu mới trên hệ thống dev đều
  phải xoá lại sau khi chạy xong (xem quy tắc cụ thể ở mục 5) — dev là môi trường dùng chung
  cho cả team, không được để lại rác làm nhiễu dữ liệu của người khác.
- **Mã package được bảo vệ, không bao giờ tạo/sửa/xoá liên quan tới các mã này khi test**
  (dù là test data giả lập hay thao tác trên UI): `VD129`, `DN101`, `YOLO125G`, `BIG60`, `SP7`,
  `B6VIP`, `GMAX250`, `D159SC`, `YOLO90`. Nếu 1 test case cần thao tác trên bản ghi có mã
  trùng dải này, dùng `FakerUtils` sinh mã khác thay thế, không dùng nguyên mã trong test case
  gốc.

> **Phạm vi vs. GUI compliance testing**: Ngoài project Selenium/FUNC này, team còn có một
> hướng kiểm thử GUI/giao diện riêng (font, màu sắc, alignment...) dùng GitHub Copilot CLI +
> Playwright MCP, điều khiển bằng prompt tự nhiên (theo tài liệu mentor cung cấp) — **không**
> thuộc phạm vi repo này, không dùng Java/Selenium/TestNG. Không trộn 2 luồng này vào cùng
> codebase; nếu cần tham khảo cách viết prompt/quy chuẩn giao diện, xem tài liệu riêng ngoài
> repo (không commit tài liệu đó vào đây vì có thể chứa thông tin nhạy cảm như credentials mẫu).

---

## 0.1. Nguồn test case — KTNN_EPC.xlsx

Test case gốc (do QA thiết kế thủ công, đã review) nằm trong file Excel nội bộ
`KTNN_EPC.xlsx` — **đường dẫn local và link Google Sheet không ghi ở đây** (repo này public,
xem lưu ý bảo mật ở mục 0); vị trí cụ thể hỏi lại nếu chưa có trong context.

3 sheet cần tự động hoá (đều đã hoàn thiện phần thiết kế case, cùng convention viết case,
tính đến 22/07/2026):

| Sheet | Số case | Ghi chú |
|---|---|---|
| `PL_FUNC` | 101 | Testcase_ID `PL_FUNC-1`..`PL_FUNC-101`, đánh số tuần tự đúng thứ tự dòng |
| `CC_FUNC` | 78 | Testcase_ID `CC_FUNC-1`..`CC_FUNC-78` |
| `CCG_FUNC` | 76 | Testcase_ID `CCG_FUNC-1`..`CCG_FUNC-76`, đang có thể tăng nếu QA thêm case mới — luôn đếm lại thay vì tin số này nếu cách xa ngày trên |

**Không sheet nào đã có case được đánh dấu tự động hoá** tính đến hiện tại (cột `AJ` trống ở
cả 3 sheet) — đây là điểm bắt đầu từ đầu, chưa có case nào "đã xong" cần tránh trùng.

### Cấu trúc cột (áp dụng cho cả 3 sheet, cùng layout)

| Cột | Ý nghĩa |
|---|---|
| A | Testcase_ID |
| B | Tên testcase (bắt đầu "Kiểm tra...") |
| C | Tiền điều kiện (chỉ ghi ở dòng đầu nhóm nếu áp dụng cho cả nhóm) |
| D | Các bước thực hiện (đánh số 1. 2. 3...) |
| E | Data test (dữ liệu VN thực tế, không placeholder) |
| F | Kết quả mong đợi |
| G | Phụ thuộc Testcase_ID (thường trống) |
| H | Mức độ nghiêm trọng lỗi |
| I | Độ ưu tiên |
| J–AH | 5 khối "Lần 1"–"Lần 5" (mỗi khối 4 cột: Người test / Ngày test / Bug ID / Notes) — hiện tại chỉ "Lần 1" (J–N) có dữ liệu, Lần 2–5 chưa dùng |
| AI | Kết quả hiện tại (Pass/Fail/N-A/Untested) |
| AJ | **Auto test** — Yes/No, đánh dấu **Yes** cho case đã viết xong automation. Đánh dấu ngay sau khi viết + verify xong 1 test method, không đánh trước |
| AK | Ghi chú |

### Phân biệt dòng test case thật vs dòng nhóm/header khi đọc sheet

Mỗi sheet có các dòng **header nhóm** tô màu xen giữa các dòng test case thật (dùng để phân
cấp, không phải data) — cột A/F của dòng header luôn trống, chỉ dòng test case thật mới có
Testcase_ID ở cột A. Khi đọc thủ công để chuyển thành `DataModel`/JSON test-data, bỏ qua các
dòng header này. Phân cấp màu (không liên quan tới màu ExtentReport pass/fail):

| Cấp | Màu fill | Hình thức tên |
|---|---|---|
| 1 (nhóm lớn nhất) | xanh lá `FFC5E0B3` | CHỮ HOA TOÀN BỘ |
| 2 (nhóm con) | cam `FFF7CAAC` | CHỮ HOA TOÀN BỘ |
| 3 (nhóm con của con) | vàng `FFFFFF00` | "Trường hợp thành công/không thành công..." |
| 4 (hiếm) | xanh dương nhạt `FFC6D9F1` | tuỳ ngữ cảnh |

### Quy trình khi lấy 1 test case từ sheet để viết automation

1. Đọc dòng test case (Tên, Tiền điều kiện, Các bước, Data test, Kết quả mong đợi) từ file
   trên — **chỉ đọc**, không parse bằng code lúc runtime (xem lý do bảo mật ở mục 0).
2. Chuyển nội dung thành Model (`dataprovider/model`) + JSON test-data
   (`src/test/resources/data/{env}/json/`) theo đúng convention mục 4.3 CLAUDE.md.
3. Viết/mở rộng Locator → Objects → Page (mục 4.1) nếu page đó chưa có.
4. Viết Test script (mục 4.4), chạy thử pass.
5. Đánh dấu `AJ = Yes` cho đúng dòng Testcase_ID đó trong `KTNN_EPC.xlsx` — việc này làm ở
   Excel/Google Sheet thật (ngoài phạm vi repo code), không phải việc của Claude Code trong
   project này.

**Sheet/case cụ thể để bắt đầu chưa được chốt** — hỏi lại nếu chưa thấy chỉ định rõ trong yêu
cầu, đừng tự chọn ngẫu nhiên.

---

## 1. Tech stack

| Layer | Công nghệ |
|---|---|
| Ngôn ngữ / Build | Java 17, Maven |
| UI Automation | Selenium WebDriver 4.11, WebDriverManager (tự tải driver, không cần cài driver tay) |
| Test Runner | TestNG (`ExecutionSuite.xml`), Cucumber (optional, chưa dùng) |
| API Testing | REST Assured |
| Data-driven | TestNG `@DataProvider`, JSON (chính), Excel (Apache POI/FastExcel/JXLS), JavaFaker |
| Database | JDBC (MSSQL/MySQL/PostgreSQL/Oracle) + Hibernate, dùng để verify dữ liệu sau khi thao tác UI |
| Report | ExtentReports (HTML + screenshot/video khi fail), Allure, email report (`EmailReporter`) |
| Log | Log4j2 / SLF4J |
| Boilerplate | Lombok (`@Getter`, `@Setter`, `@Builder`, `@Slf4j`, ...) — dùng ở khắp nơi, luôn dùng thay vì viết getter/setter tay |

`pom.xml` giữ nguyên toàn bộ dependency của bản gốc (kể cả cucumber/excel/database dù chưa
dùng ngay) vì đây là *tech stack chuẩn* của convention này — đừng xoá bớt trừ khi chắc chắn
sẽ không bao giờ cần.

---

## 2. Cấu trúc thư mục

```
KTNN_EPC_AUTO/
├── pom.xml
├── CLAUDE.md                  ← file này
├── README.md
├── .gitignore
├── .vscode/settings.json
├── reports/ExtentReports/     ← output, KHÔNG commit (đã .gitignore)
├── src/
│   ├── main/
│   │   ├── java/com/ktnn/     ← FRAMEWORK LAYER (generic, tái sử dụng cho mọi project)
│   │   └── resources/log4j2.xml
│   └── test/
│       ├── java/com/ktnn/projects/   ← PROJECT LAYER (page object + test script thật)
│       └── resources/
│           ├── config/        ← config.properties, env.json, database.json
│           ├── data/          ← dữ liệu test JSON/Excel theo từng env
│           ├── script/        ← JS snippet inject qua Selenium
│           └── suites/        ← file suite TestNG (ExecutionSuite.xml)
└── target/                    ← build output Maven, KHÔNG commit
```

**Nguyên tắc tách lớp quan trọng nhất:** `src/main/java` chỉ chứa code framework — không
được import bất kỳ class nào ở `src/test/java`, và không được chứa logic đặc thù của một
page/module cụ thể (ví dụ không được hard-code URL `/login`, `/dashboard` trong
`src/main`). Toàn bộ thứ đặc thù nghiệp vụ nằm ở `src/test/java/com/ktnn/projects`.

---

## 3. Framework layer — `src/main/java/com/ktnn/`

Đây là phần **không nên sửa tuỳ tiện**, chỉ mở rộng khi thực sự cần thêm khả năng chung
(ví dụ thêm 1 loại DB mới, thêm 1 loại report mới). Mỗi package có đúng 1 trách nhiệm:

| Package | Trách nhiệm | File chính |
|---|---|---|
| `driver` | Tạo & quản lý `WebDriver` theo từng thread (`ThreadLocal`) | `BrowserFactory` (Factory pattern chọn Chrome/Edge/Firefox/Safari qua `EnumMap`), `DriverManager` (get/set/quit driver theo thread), `XxxBrowserDriver` (mỗi browser 1 class, extends `BrowserDriver`) |
| `controller` | `WebUI` — wrapper toàn bộ thao tác Selenium (click, input, wait, assert, alert, JS executor...). **Mọi Objects/Page class đều gọi qua đây, không gọi thẳng Selenium API.** | `WebUI` |
| `consts` | Hằng số toàn cục | `FrameConst` (config framework: browser, wait, report flags...), `ProjectConst` (constant riêng của project: `ModuleURL`, `Databases`, `DBSchema` — **file này bạn sẽ điền dần** khi có page/DB thật), `AuthorType` (enum tên người viết TC, thêm thành viên vào đây) |
| `database` | Kết nối & query DB để verify dữ liệu sau khi thao tác UI | `config/` = factory kết nối theo `DatabaseType` (MYSQL/POSTGRES/ORACLE) qua Hibernate `SessionFactory`; `repo/BaseRepository` = resolve tên schema theo env (dev/sit/uat/prd) |
| `datadriven` | Base class cho data-driven testing | `DataModel` (1 field = 1 ô dữ liệu, có `devName`/`value`/`fill`/`verify`/`langProperty`), `BaseModel` (mọi Model kế thừa, có sẵn `testId`/`account`/`category`...), `BaseProvider` (merge dữ liệu JSON vào Model bằng reflection) |
| `report` | ExtentReports + email report | `ExtentReportManager` (tạo report, log pass/fail/screenshot), `ExtentTestManager` (`ThreadLocal<ExtentTest>`), `ReportConfig` (toàn bộ flag bật/tắt tính năng report), `EmailReporter` (implements `IReporter`, sinh file HTML tổng hợp cho email) |
| `listeners` | TestNG listener | `AnnotationTransformer` (gán group tự động từ `@FrameAnnotation`), `Retry` (`IRetryAnalyzer`, hiện đang tắt — bật lại khi cần retry test fail) |
| `annotations` | Custom annotation gắn lên method test | `@FrameAnnotation(category, author, reviewer)`, `@TFSLink("...")` (link sang hệ thống quản lý bug/TMS) |
| `exceptions` | Exception riêng của framework | `FrameworkException` (base, extends `RuntimeException`), và các exception cụ thể extends nó |
| `utils` | Helper thuần tuý, không phụ thuộc Selenium/TestNG (trừ `CaptureUtils`) | `DateTimeUtils`, `FakerUtils` (JavaFaker), `LanguageUtils` (bỏ dấu tiếng Việt, đổi charset), `IconUtils` (icon cho report) |
| `utils.configloader` | Đọc config/property/json/excel | `AbsPropertyUtils` (abstract, định nghĩa hợp đồng load config), `ResourceReader` (đọc file trong resources qua ClassLoader), `JsonUtils` (Singleton, parse JSON test-data thành `DataModel`), `CaptureUtils` (screenshot + quay video màn hình), `ExcelHelpers` |
| `clients.api` | Gọi REST API (khi cần verify qua API thay vì UI) | `APIBase` (wrapper RestAssured: tạo request, gọi API, log request/response vào report, convert JSON↔Object) |

### Pattern lặp lại nhiều lần trong framework layer — nhớ để áp dụng đúng khi mở rộng
- **Factory + `EnumMap`/`Map`**: `BrowserFactory`, `DatabaseFactory` — map 1 enum sang 1 instance implementation.
- **`ThreadLocal`**: `DriverManager`, `ExtentTestManager`, `DatabaseManager` — bắt buộc để chạy `parallel="classes"` an toàn (mỗi thread test 1 driver/1 report/1 session riêng).
- **Singleton nội bộ**: `JsonUtils.getInstance()`, `PropertiesUtils.instance` — style Lombok `@Getter private static final X instance = new X()` + constructor `private`.
- **Static utility class**: đa số method trong `WebUI`, `AbsPropertyUtils`... là `static`.

---

## 4. Project layer — `src/test/java/com/ktnn/projects/`

```
projects/
├── common/
│   ├── TestBase.java          ← mọi Test class extends class này
│   ├── BasePage.java          ← mọi Page class extends class này
│   ├── PropertiesUtils.java   ← load config.properties + env.json + database.json
│   └── TestListener.java      ← implements ITestListener, gắn report cho từng TC
├── pages/
│   ├── locator/                ← 1 file / 1 page, CHỈ chứa String locator
│   ├── objects/                ← 1 file / 1 page, chứa find + action nguyên tử (click/input)
│   └── pages/                  ← 1 file / 1 page, chứa flow nghiệp vụ (gộp nhiều action + verify)
├── dataprovider/
│   ├── DataPath.java            ← hằng số đường dẫn tới file JSON test-data
│   ├── model/                   ← 1 file / 1 test case model, extends BaseModel
│   └── providers/               ← 1 file / 1 test case, extends BaseProvider, đọc JSON → Model
└── testscript/                  ← 1 file / 1 test class, extends TestBase
```

### 4.1. Page Object Model — 3 tầng bắt buộc (Locator → Objects → Page)

Đây là convention **quan trọng nhất** của project. Mỗi page trên UI luôn tách thành đúng
3 file, không được gộp lại, không được bỏ tầng nào:

**Tầng 1 — `Locator`** (chỉ chứa locator, không có logic):
```java
package com.ktnn.projects.pages.locator;

import lombok.Getter;

@Getter
public class XxxLocator extends BaseLocator {     // extends BaseLocator nếu cần locator dùng chung
    @Getter
    public static XxxLocator instance = new XxxLocator();

    private XxxLocator() {}

    // "ID|value" hoặc "CSS|value" -> WebUI.findWebElement(String) tự parse theo prefix
    // không có prefix -> coi như XPath
    String txtSomeInput = "ID|form_item_something";
    String btnSubmit = "//button[@testek='btn-submit']";
    String rowByKeyword = "//tr[contains(@class,'row-table') and contains(.,'%s')]"; // %s = dynamic locator
}
```

**Tầng 2 — `Objects`** (find element + action nguyên tử trên 1 element, KHÔNG verify, KHÔNG
điều hướng sang page khác trừ khi action đó tự nhiên chuyển trang):
```java
package com.ktnn.projects.pages.objects;

import com.ktnn.projects.pages.locator.XxxLocator;
import lombok.Getter;
import org.openqa.selenium.WebElement;

@Getter
public class XxxObjects extends BaseObjects {
    @Getter
    public static XxxObjects instance = new XxxObjects();

    private final XxxLocator xxxLocator;

    private XxxObjects() {
        xxxLocator = XxxLocator.getInstance();
    }

    /* --- Element finders (package-private hoặc public, đặt tên findXxxEle/findXxx...) --- */
    public WebElement findSomeInput() {
        return findWebElement(xxxLocator.getTxtSomeInput());
    }

    /* --- Action methods: trả về `this` để chain, hoặc trả Page khác nếu action điều hướng --- */
    public XxxObjects inputSomeValue(String value) {
        inputText(findSomeInput(), "Some Label", value);
        return this;
    }
}
```

**Tầng 3 — `Page`** (gộp nhiều action từ Objects thành 1 flow nghiệp vụ, có verify, đây là
tầng mà `testscript` gọi trực tiếp):
```java
package com.ktnn.projects.pages.pages;

import com.ktnn.consts.FrameConst.FailureHandling;
import com.ktnn.projects.common.BasePage;
import com.ktnn.projects.pages.objects.XxxObjects;
import org.openqa.selenium.support.PageFactory;

public class XxxPage extends BasePage {
    private final XxxObjects xxxObjects;

    public XxxPage() {
        super();                                   // hoặc: webDriver = DriverManager.getDriver();
        PageFactory.initElements(webDriver, this);
        xxxObjects = XxxObjects.getInstance();
    }

    /** Luôn trả `this` (hoặc Page tiếp theo) để test script viết theo kiểu chain. */
    public XxxPage fillForm(XxxModel model) {
        xxxObjects.inputSomeValue(model.getSomeField().getValue());
        return this;
    }

    public XxxPage verifySomethingDisplayed() {
        var el = xxxObjects.findSomeInput();
        assertTrueCondition(el, el != null && el.isDisplayed(),
                FailureHandling.CONTINUE_ON_FAILURE, "Verify something is displayed");
        return this;
    }
}
```

**Quy tắc đặt tên bắt buộc:**
- Prefix locator theo loại element: `txt` (input/text), `btn` (button), `mnu` (menu),
  `row`/`popUp`/`err`/`lbl`... — nhìn tên là biết loại element ngay không cần mở DevTools.
  Xem `pages/locator/BaseLocator.java` để biết các dynamic-locator dùng chung sẵn có
  (`DYNAMIC_INPUT_TYPE_FORM`, `DYNAMIC_SPAN_TEXT_FORM`...).
- Method find: `findXxxEle()` hoặc `findXxx...()`.
- Method action ở Objects: `inputXxx`, `clickXxx`, `selectXxx` — verb + tên field.
- Method flow ở Page: verb mô tả hành vi nghiệp vụ, không mô tả implementation
  (`fillCustomerInfo`, `verifyCustomerCreatedSuccess`, không phải `clickButtonThenWait`).
- Class Singleton: field `instance` dùng `@Getter public static X instance = new X();`
  và constructor `private` — **không** dùng `getInstance()` viết tay, để Lombok tự sinh.

### 4.2. `PageManagement` — điểm vào của flow (tạo khi có page đầu tiên)

Bản gốc có `PageManagement` để đi từ "chưa đăng nhập" → `LoginPage` → `HomePage`. File này
**chưa được tạo** trong scaffold vì chưa có page thật. Khi bạn có `LoginPage`/`HomePage` đầu
tiên, tạo `src/test/java/com/ktnn/projects/pages/PageManagement.java` theo mẫu:
```java
package com.ktnn.projects.pages;

import com.ktnn.driver.DriverManager;
import com.ktnn.projects.pages.pages.HomePage;
import com.ktnn.projects.pages.pages.LoginPage;

import static com.ktnn.consts.FrameConst.AppConfig.APP_DOMAIN;

public class PageManagement {
    public static LoginPage accessWebPage() {
        DriverManager.getDriver().get(APP_DOMAIN);
        return new LoginPage();
    }

    public static HomePage gotoHomePage() {
        return new HomePage();
    }
}
```
Sau đó khôi phục lại đoạn auto-login trong `TestBase.beforeClass()` (đã để sẵn comment TODO
chỉ chỗ cần sửa) và thêm các method `gotoXxxPage()` vào `BasePage` (cũng đã để sẵn mẫu comment
trong file) khi thêm `ProjectConst.ModuleURL` cho từng module.

### 4.3. Data-driven testing — Model → Provider → JSON → Test

**Bước 1 — thêm path vào `DataPath`:**
```java
String DATA_XXX = "data/" + env + "/json/xxx.json";
```

**Bước 2 — tạo Model (`dataprovider/model/XxxModel.java`), field kiểu `DataModel`, extends
`BaseModel`:**
```java
package com.ktnn.projects.dataprovider.model;

import com.ktnn.datadriven.BaseModel;
import com.ktnn.datadriven.DataModel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class XxxModel extends BaseModel {
    public DataModel fieldA;
    public DataModel fieldB;

    public XxxModel() {
        super();
        fieldA = createDataModelObj("FieldA");   // "FieldA" phải khớp key trong JSON
        fieldB = createDataModelObj("FieldB");
    }
}
```

**Bước 3 — tạo Provider (`dataprovider/providers/XxxProvider.java`), 1 `@DataProvider` /
1 test case, tên `@DataProvider` = tên method test tương ứng:**
```java
package com.ktnn.projects.dataprovider.providers;

import com.ktnn.datadriven.BaseProvider;
import com.ktnn.projects.dataprovider.DataPath;
import com.ktnn.projects.dataprovider.model.XxxModel;
import com.ktnn.utils.configloader.JsonUtils;
import org.testng.annotations.DataProvider;

import java.lang.reflect.Method;

public class XxxProvider extends BaseProvider {
    JsonUtils jsonUtils = JsonUtils.getInstance();

    @DataProvider(name = "KTNN_Xxx_001_Valid")
    public Object[][] KTNN_Xxx_001_Valid(Method method) {
        var dataList = jsonUtils.readDataTestFromJSON(DataPath.DATA_XXX, method.getName());
        return updateDataModel(new XxxModel(), dataList);
    }
}
```

**Bước 4 — file JSON test-data** tại `src/test/resources/data/{env}/json/xxx.json`, key
ngoài cùng = tên `@DataProvider`, mỗi phần tử = 1 dòng dữ liệu, có `master` (dữ liệu chính)
và `detail` (dữ liệu con dạng bảng nếu có):
```json
{
  "KTNN_Xxx_001_Valid": [
    {
      "Description": "Mô tả test case",
      "Id": "Valid-01",
      "Category": "REGRESSION",
      "Executed": true,
      "AccountTest": "",
      "TestScenario": "",
      "Preconditions": "",
      "master": {
        "config": { "isFill": [], "isVerify": [] },
        "FieldA": "some value",
        "FieldB": "other value"
      },
      "detail": {}
    }
  ]
}
```
`config.isFill`/`isVerify` dùng để bật/tắt fill hoặc verify theo cột — để trống `[]` nghĩa là
áp dụng mặc định cho mọi field; xem `JsonUtils.checkIsExist()` nếu cần hiểu cơ chế `ALL`/`^`.

### 4.4. Test script

```java
package com.ktnn.projects.testscript;

import com.ktnn.annotations.FrameAnnotation;
import com.ktnn.consts.AuthorType;
import com.ktnn.consts.FrameConst.CategoryType;
import com.ktnn.projects.common.TestBase;
import com.ktnn.projects.dataprovider.model.XxxModel;
import com.ktnn.projects.dataprovider.providers.XxxProvider;
import com.ktnn.projects.pages.pages.HomePage;
import com.ktnn.projects.pages.pages.XxxPage;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class XxxTest extends TestBase {
    XxxPage xxxPage;
    HomePage homePage;

    @BeforeClass(alwaysRun = true)
    public void beforeClass() {
        super.beforeClass();
        homePage = new HomePage();
    }

    @BeforeMethod(alwaysRun = true)
    public void beforeMethod() {
        xxxPage = homePage.gotoXxxPage();
    }

    @FrameAnnotation(category = {CategoryType.REGRESSION}, author = {AuthorType.ADMIN}, reviewer = {AuthorType.ADMIN})
    @Test(description = "Verify ...", dataProvider = "KTNN_Xxx_001_Valid", dataProviderClass = XxxProvider.class)
    public void KTNN_Xxx_001_Valid(XxxModel model) {
        xxxPage
                .fillForm(model)          // mỗi bước 1 comment ngắn giải thích action
                .verifySomethingDisplayed();
    }
}
```

**Quy tắc đặt tên test method / `@DataProvider` name:** `{PREFIX}_{Module}_{STT}_{Trạng
thái}`, ví dụ `TK_CreateCustomer_001_Valid`, `TK_CreateCustomer_002_InvalidPhone`. Chọn 1
prefix cố định cho project này (gợi ý: `KTNN`) và dùng xuyên suốt.

Cuối cùng, đăng ký class test vào `src/test/resources/suites/ExecutionSuite.xml`:
```xml
<class name="com.ktnn.projects.testscript.XxxTest"/>
```

---

## 5. Coding conventions chung (áp dụng mọi layer)

- **Lombok mọi nơi**: `@Getter/@Setter` thay vì viết tay, `@Slf4j` thay vì tự khai báo
  `Logger`, `@Builder` cho object cần build linh hoạt (`DataModel`, `DatabaseInfo`).
- **Method chaining**: action method trong Objects/Page trả về `this` (hoặc Page kế tiếp nếu
  có điều hướng) để test script viết thành chuỗi `.a().b().c()` dễ đọc như kịch bản test.
- **Assert luôn qua `WebUI`**: dùng `assertTrueCondition` / `assertEqualCondition` /
  `assertFalseCondition` với `FailureHandling.CONTINUE_ON_FAILURE` (soft assert, tiếp tục
  chạy dù fail bước này) hoặc `STOP_ON_FAILURE` (dừng ngay). Không dùng thẳng
  `org.testng.Assert` trong Page/Objects.
- **Không tự gọi `Thread.sleep`**: dùng `WebUI.waitFor(seconds)` nếu bắt buộc phải chờ cứng,
  ưu tiên các hàm wait có điều kiện (`waitForElementVisible`, `waitForElementClickable`,
  `waitForInputValueNotEmpty`...).
- **Locator string**: `"ID|value"` hoặc `"CSS|value"` khi có thể (nhanh hơn XPath), XPath
  thuần khi cần điều kiện phức tạp; locator động dùng `%s`/`%d` + `String.format` hoặc
  `getByXpathDynamic`.
- **Javadoc ngắn**: comment `/** ... */` 1-2 dòng mô tả *mục đích* method, không mô tả lại
  từng dòng code. Nhiều method không cần javadoc nếu tên đã đủ rõ.
- **`region`/`endregion` comment**: dùng `//region Xxx` ... `//endregion` để nhóm method liên
  quan trong file dài (xem `WebUI`, `FrameConst`, `BasePage`).
- **Không hard-code URL/domain** trong Page/Objects — luôn lấy qua
  `FrameConst.AppConfig.APP_DOMAIN` hoặc `ProjectConst.ModuleURL`.
- **Log bằng `log.info/error` (SLF4J qua `@Slf4j`)**, không dùng `System.out.println` (trừ vài
  chỗ hiếm trong framework layer đã có sẵn).
- **Test tạo dữ liệu mới phải tự dọn dẹp**: bất kỳ test case nào tạo/import bản ghi mới trên
  hệ thống test (form create, import file...) đều phải xoá lại dữ liệu đó sau khi test xong
  — dùng `@AfterMethod`/`@AfterClass` gọi lại action xoá qua UI (ưu tiên, vì gần với luồng thật
  nhất), hoặc query xoá qua DB (`database` package) nếu hệ thống không có chức năng xoá qua UI.
  Không để lại rác trên môi trường dev dùng chung — xem thêm mục 0.

---

## 6. Configuration

| File | Vai trò |
|---|---|
| `src/test/resources/config/config.properties` | Config chạy: browser, headless, wait, report flags, retry, `exeEnv`, `exeLanguage` |
| `src/test/resources/config/env.json` | Base URL / account / apiUrl theo từng env (`sit`/`uat`/`prod`) — **điền giá trị thật vào đây** trước khi chạy |
| `src/test/resources/config/database.json` | Danh sách kết nối DB theo từng env, dùng khi `exeDBVerification=true`. Mẫu 1 entry: `{"type":"postgres","name":"...","url":"jdbc:postgresql://...","username":"...","password":"...","config":"hibernate/hibernate.conf.xml"}` |
| `src/test/resources/suites/ExecutionSuite.xml` | Entry point chạy bằng TestNG/Maven, khai báo `<class>` cho từng test class |
| `src/test/resources/language_vi.properties` / `language_en.properties` | Label hiển thị trong report theo `DataModel.langProperty`, chọn qua `exeLanguage` |
| `src/main/resources/log4j2.xml` | Cấu hình log console |
| `pom.xml` → `<suite.name>` | Suite mặc định khi chạy `mvn test` không truyền tham số |

Đọc `PropertiesUtils.updateMavenProperties()` nếu cần thêm 1 key config mới — pattern chung:
đọc từ system property trước (`-Dkey=value` khi chạy Maven), fallback sang
`config.properties`, cuối cùng fallback giá trị mặc định hard-code.

> `env.json`/`database.json` sẽ chứa tài khoản/mật khẩu thật của hệ thống — xem lưu ý
> bảo mật ở mục 0 trước khi điền giá trị thật và commit.

---

## 7. Những gì khác so với bản gốc (labit-web-selenium-0326)

Ghi lại để không nhầm là bug khi so sánh 2 project:

- `groupId`/package đổi `com.testek` → `com.ktnn`, `artifactId` → `ktnn-epc-auto`.
- Bỏ toàn bộ `study/` (bài tập theo lesson) và toàn bộ page/test nghiệp vụ của website khoá
  học (Login/Product/Order/Supplier/Category/Customer, `finalproject/`, `client/rest/`) —
  đây là dữ liệu của web khoá học, không liên quan tới KTNN EPC.
  - `ProductRepository.java` (ví dụ nghiệp vụ) không được copy sang; `BaseRepository.java`
    (generic) thì giữ.
  - `PageManagement.java` chưa tồn tại — xem mục 4.2 để tạo khi có page đầu tiên.
- `ProjectConst.ModuleURL` / `Databases` / `DBSchema` để **rỗng** (enum không có constant) —
  điền dần khi có page/DB thật, đừng để rỗng mãi.
- `AuthorType` chỉ còn `ADMIN` — thêm thành viên team vào đây.
- `TestBase.beforeClass()` bỏ đoạn auto-login (phụ thuộc `LoginPage`/`HomePage` chưa tồn
  tại), để lại comment TODO chỉ đúng chỗ khôi phục.
- `BasePage` bỏ các `gotoXxxPage()` cụ thể (Product/Order/Supplier/...), để lại comment mẫu.
- `FrameConst`/`ReportConfig`: xoá khối `INFLUXDB_*` (config + token chết, không dùng vì
  `DEVELOP_STATE=false` — không nên copy token của hệ thống người khác sang project mới),
  `APP_DOMAIN`/`API_DOMAIN`/`JIRA_DOMAIN`/`PROJECT_RESULT_DB` trả về rỗng thay vì trỏ sang
  domain testek.vn.
- `pom.xml`: xoá 1 dependency `log4j-core` bản `2.19.0` bị khai báo trùng với bản `2.23.1`
  (lỗi thừa trong bản gốc); sửa `<suite.name>` trỏ đúng
  `src/test/resources/suites/ExecutionSuite.xml` (bản gốc trỏ nhầm sang file
  `genspark-suite.xml` không tồn tại).
- `config.properties`: `exeDBVerification=false` mặc định (vì `database.json` đang rỗng, bật
  `true` sẽ lỗi lúc kết nối) — nhớ đổi lại `true` khi đã điền DB thật.
- Thư mục rỗng theo convention (`pages/pages`, `dataprovider/model`, `dataprovider/providers`,
  `testscript`, `data/sit/json`, `data/sit/xls`, `script`, `reports/ExtentReports`) được giữ
  bằng file `.gitkeep` — xoá file `.gitkeep` khi thư mục đã có nội dung thật.

---

## 8. Việc tiếp theo (chưa làm trong bước này)

Theo yêu cầu, các việc sau **để làm sau**, chưa thực hiện ở bước scaffold này:
1. Setup VSCode để chạy/debug được Selenium (Extension Pack for Java, Maven for Java, cấu
   hình chạy TestNG...).
2. Cài Google Chrome / trình duyệt test nếu máy chưa có.
3. Điền `env.json`/`database.json` với giá trị thật của hệ thống KTNN EPC.
4. Tạo page đầu tiên (thường là Login) theo mục 4.1–4.2 ở trên.
