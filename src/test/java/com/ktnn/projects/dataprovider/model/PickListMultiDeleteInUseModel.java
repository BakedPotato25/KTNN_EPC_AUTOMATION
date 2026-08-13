package com.ktnn.projects.dataprovider.model;

import com.ktnn.datadriven.BaseModel;
import com.ktnn.datadriven.DataModel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PickListMultiDeleteInUseModel extends BaseModel {
    public DataModel name1;
    public DataModel code1;
    public DataModel name2;
    public DataModel code2;
    public DataModel searchKeyword;
    public DataModel catalogName;
    public DataModel catalogCode;
    public DataModel specificationName;
    public DataModel specificationCode;

    public PickListMultiDeleteInUseModel() {
        super();
        name1 = createDataModelObj("Name1");
        code1 = createDataModelObj("Code1");
        name2 = createDataModelObj("Name2");
        code2 = createDataModelObj("Code2");
        searchKeyword = createDataModelObj("SearchKeyword");
        catalogName = createDataModelObj("CatalogName");
        catalogCode = createDataModelObj("CatalogCode");
        specificationName = createDataModelObj("SpecificationName");
        specificationCode = createDataModelObj("SpecificationCode");
    }
}
