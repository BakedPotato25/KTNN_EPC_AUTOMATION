package com.ktnn.consts;

import lombok.Getter;
import lombok.Setter;

/**
 * Constant riêng của project KTNN_EPC_AUTO.
 * Thêm 1 enum value cho mỗi module/page/schema khi được implement -
 * không tạo trước entry cho page chưa tồn tại.
 */
@Getter
@Setter
public class ProjectConst {

    /**
     * Mỗi entry ứng với 1 page/module cấp cao nhất của ứng dụng đang test.
     * Dùng bởi các shortcut BasePage.gotoXxxPage(), vd:
     * <pre>
     * DASHBOARD("Dashboard", APP_DOMAIN + "/dashboard"),
     * </pre>
     */
    @Getter
    public enum ModuleURL {
        PICK_LIST("PickList", "/epc/pick-list"),
        CHARACTERISTIC_CATALOG("Characteristic Catalog", "/epc/characteristic-catalog"),
        ;

        private final String name;
        private final String path;

        ModuleURL(String name, String path) {
            this.name = name;
            this.path = path;
        }
    }

    /**
     * Mỗi entry ứng với 1 kết nối database khai báo trong config/database.json.
     * Tên ở đây phải khớp với DatabaseInfo.name dùng khi đăng ký kết nối.
     */
    @Getter
    public enum Databases {
        ;

        private final String name;

        Databases(String name) {
            this.name = name;
        }
    }

    /**
     * Mỗi entry ứng với 1 nhóm DB schema/table cần map tên theo từng environment
     * (dev/sit/uat/prd), dùng qua BaseRepository.getDBSchema(...).
     */
    @Getter
    public enum DBSchema {
        ;
        private final String devEnv;
        private final String sitEnv;
        private final String uatEnv;
        private final String prdEnv;

        DBSchema(String devEnv, String sitEnv, String uatEnv, String prdEnv) {
            this.devEnv = devEnv;
            this.sitEnv = sitEnv;
            this.uatEnv = uatEnv;
            this.prdEnv = prdEnv;
        }
    }
}
