package com.ktnn.projects.dataprovider.model;

import com.ktnn.datadriven.BaseModel;
import com.ktnn.datadriven.DataModel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PickListMultiDeleteModel extends BaseModel {
    public DataModel name1;
    public DataModel code1;
    public DataModel name2;
    public DataModel code2;
    public DataModel searchKeyword;

    public PickListMultiDeleteModel() {
        super();
        name1 = createDataModelObj("Name1");
        code1 = createDataModelObj("Code1");
        name2 = createDataModelObj("Name2");
        code2 = createDataModelObj("Code2");
        searchKeyword = createDataModelObj("SearchKeyword");
    }
}
