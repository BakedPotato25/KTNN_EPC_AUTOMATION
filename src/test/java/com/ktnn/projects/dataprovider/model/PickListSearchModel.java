package com.ktnn.projects.dataprovider.model;

import com.ktnn.datadriven.BaseModel;
import com.ktnn.datadriven.DataModel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PickListSearchModel extends BaseModel {
    public DataModel searchKeyword;

    public PickListSearchModel() {
        super();
        searchKeyword = createDataModelObj("SearchKeyword");
    }
}
