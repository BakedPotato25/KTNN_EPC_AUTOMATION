package com.ktnn.projects.dataprovider.model;

import com.ktnn.datadriven.BaseModel;
import com.ktnn.datadriven.DataModel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PickListMultiFieldSearchModel extends BaseModel {
    public DataModel searchKeyword;
    public DataModel expectedNameA;
    public DataModel expectedNameB;

    public PickListMultiFieldSearchModel() {
        super();
        searchKeyword = createDataModelObj("SearchKeyword");
        expectedNameA = createDataModelObj("ExpectedNameA");
        expectedNameB = createDataModelObj("ExpectedNameB");
    }
}
