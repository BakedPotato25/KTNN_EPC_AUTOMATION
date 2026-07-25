package com.ktnn.projects.dataprovider;

import com.ktnn.consts.FrameConst;

/**
 * One path constant per test-data JSON file, matching files under data/{env}/json/.
 */
public interface DataPath {
    String env = FrameConst.ExecuteConfig.EXE_ENV.toLowerCase();

    String DATA_PICK_LIST = "data/" + env + "/json/pickList.json";
}
