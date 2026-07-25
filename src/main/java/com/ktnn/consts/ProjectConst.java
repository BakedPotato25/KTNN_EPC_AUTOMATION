package com.ktnn.consts;

import lombok.Getter;
import lombok.Setter;

/**
 * Project-specific constants for KTNN_EPC_AUTO.
 * Add one enum value per module/page/schema as they are implemented -
 * do not pre-create entries for pages that do not exist yet.
 */
@Getter
@Setter
public class ProjectConst {

    /**
     * One entry per top-level page/module of the application under test.
     * Used by BasePage.gotoXxxPage() shortcuts, e.g.:
     * <pre>
     * DASHBOARD("Dashboard", APP_DOMAIN + "/dashboard"),
     * </pre>
     */
    @Getter
    public enum ModuleURL {
        PICK_LIST("PickList", "/epc/pick-list"),
        ;

        private final String name;
        private final String path;

        ModuleURL(String name, String path) {
            this.name = name;
            this.path = path;
        }
    }

    /**
     * One entry per logical database connection declared in config/database.json.
     * The name here must match DatabaseInfo.name used when the connection is registered.
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
     * One entry per DB schema/table group that needs per-environment name mapping
     * (dev/sit/uat/prd), consumed via BaseRepository.getDBSchema(...).
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
