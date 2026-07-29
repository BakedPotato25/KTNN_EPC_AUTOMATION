package com.ktnn.projects.dataprovider;

import com.ktnn.consts.FrameConst;

/**
 * Mỗi hằng số path ứng với 1 file JSON test-data, khớp file dưới data/{env}/json/.
 */
public interface DataPath {
    String env = FrameConst.ExecuteConfig.EXE_ENV.toLowerCase();

    String DATA_PICK_LIST = "data/" + env + "/json/pickList.json";
}
