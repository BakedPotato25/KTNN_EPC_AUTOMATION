# PickList seed data inventory (env: sit)

Danh sách bản ghi PickList **có sẵn trên hệ thống dev dùng chung** mà bộ automation
`PickListTest.java` đang phụ thuộc để chạy các case Search/Filter (KHÔNG phải data do
automation tự tạo — automation không được tự ý xoá/sửa các bản ghi này).

Lý do có file này: nếu ai đó xoá/sửa 1 trong các bản ghi dưới đây, các case tương ứng sẽ fail
mà không rõ nguyên nhân ngay. Trước khi xoá/sửa bản ghi PickList nào trên môi trường `sit`,
kiểm tra lại bảng dưới. Nếu phát hiện bị mất, tạo lại đúng theo cột "Cách tạo lại" rồi báo lại
để cập nhật file này.

Dữ liệu chốt lúc kiểm tra trực tiếp trên UI ngày 2026-08-10.

## Bảng 1 — bản ghi / điều kiện seed

| Bản ghi / điều kiện | Field | Giá trị | Case phụ thuộc | Cách nhận biết còn tồn tại | Cách tạo lại nếu mất |
|---|---|---|---|---|---|
| "Loại đường truyền" | Name | `Loại đường truyền` | PL_FUNC-1, 2, 5, 6, 8 | Search exact "Loại đường truyền" → đúng 1 kết quả | Add New: Name=`Loại đường truyền`, Code=`LineConnect_Type`, Description=`test` |
| (cùng bản ghi trên) | Code | `LineConnect_Type` | PL_FUNC-5, 6 | Search exact theo Code "LineConnect_Type" → đúng 1 kết quả | (cùng bản ghi) |
| "Test_đa_trường" | Name | `Test_đa_trường` | PL_FUNC-8 | Search exact "Test_đa_trường" → đúng 1 kết quả | Add New: Name=`Test_đa_trường`, Code=`Loại_đa_trường` |
| Description = "test" | Description | `test` (exact) | PL_FUNC-16 | Filter Description = "test" → hiện có **3** bản ghi thoả (Loại đường truyền/LineConnect_Type, test/test 123124, anhnk test 1/anhnk test) — chỉ cần **≥1** bản ghi còn Description đúng "test", không bắt buộc đúng 1 | Không cần tạo riêng — bản ghi "Loại đường truyền" ở trên đã tự thoả điều kiện này (Description="test") |
| Description like "test" | Description | chứa `test` | PL_FUNC-15, 18 | Filter Description like "test" → ≥3 bản ghi (superset của dòng trên) | Không cần tạo riêng — cùng lý do trên |
| Version = 1 | Version | `1` | PL_FUNC-17, 18 | Filter Version = "1" → hiện có **14** bản ghi, dư khá nhiều so với yêu cầu ≥1 | Rủi ro thấp — cần rất nhiều bản ghi Version=1 biến mất cùng lúc mới ảnh hưởng case |

**Ghi chú quan trọng:**
- Bản ghi "Loại đường truyền" (Code `LineConnect_Type`) đang gánh nhiều điều kiện cùng lúc
  (Name, Code, VÀ Description="test") — đây là bản ghi **quan trọng nhất** trong danh sách,
  mất bản ghi này ảnh hưởng tới 5 case (PL_FUNC-1, 2, 5, 6, 8) chứ không chỉ Search.
- PL_FUNC-18 (kết hợp AND: Description like "test" **và** Version=1 cùng lúc) phụ thuộc
  **tổ hợp** 2 điều kiện đồng thời trên cùng 1 bản ghi — chưa xác nhận trực tiếp có bản ghi
  nào thoả cả 2 cùng lúc hay không (có khả năng cao vì Version=1 có 14 bản ghi và Description
  like "test" có ≥3 bản ghi, nhưng chưa cross-check giao của 2 tập). Đây cũng là giới hạn đã
  biết của cơ chế fail-fast `verifySeedDataAvailable()` — method đó chỉ check từng điều kiện
  đơn lẻ, không check tổ hợp AND.

## Bảng 2 — map Testcase_ID → JSON provider key → field

| Testcase_ID | `@DataProvider` name (JSON key trong `pickList.json`) | Field JSON | Giá trị |
|---|---|---|---|
| PL_FUNC-1 | `KTNN_PickListSearch_001_Relative` | `SearchKeyword` | `Loại` |
| PL_FUNC-2 | `KTNN_PickListSearch_002_Exact` | `SearchKeyword` | `Loại đường truyền` |
| PL_FUNC-5 | `KTNN_PickListSearch_005_CodeRelative` | `SearchKeyword` | `LineConnect` |
| PL_FUNC-6 | `KTNN_PickListSearch_006_CodeExact` | `SearchKeyword` | `LineConnect_Type` |
| PL_FUNC-8 | `KTNN_PickListSearch_008_MultiField` | `SearchKeyword` / `ExpectedNameA` / `ExpectedNameB` | `Loại` / `Loại đường truyền` / `Test_đa_trường` |
| PL_FUNC-15 | `KTNN_PickListFilter_001_DescriptionLike` | `FilterValue` | `test` |
| PL_FUNC-16 | `KTNN_PickListFilter_002_DescriptionExact` | `FilterValue` | `test` |
| PL_FUNC-17 | `KTNN_PickListFilter_003_VersionExact` | `FilterValue` | `1` |
| PL_FUNC-18 | `KTNN_PickListFilter_004_CombineWithAnd` | `Value1` / `Value2` | `test` (Description like) + `1` (Version =) |
| PL_FUNC-20 | `KTNN_PickListRefresh_001_ResetSearch` | `SearchKeyword` | `Loại` (rủi ro thấp — case chỉ verify grid reset sau refresh, không assert nội dung kết quả search) |

Case Sort (PL_FUNC-9..14) và Filter-Or (PL_FUNC-19) không phụ thuộc bản ghi cụ thể theo tên
nên không đưa vào bảng này.

## Cơ chế fail-fast liên quan

`PickListTest.beforeClass()` gọi `verifySeedDataAvailable()` (thêm trong
`PickListPage.java`) — check nhanh các bản ghi ở Bảng 1 còn tồn tại trước khi chạy 34 case.
Nếu thiếu, toàn bộ class bị SKIP với message trỏ về đúng file này, thay vì để nhiều case fail
rải rác khó truy vết.
