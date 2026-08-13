package com.ktnn.projects.dataprovider.model;

import com.ktnn.datadriven.BaseModel;
import com.ktnn.datadriven.DataModel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PickListDeleteInUseModel extends BaseModel {
    public DataModel pickListName;
    public DataModel pickListCode;
    public DataModel catalogName;
    public DataModel catalogCode;
    public DataModel specificationName;
    public DataModel specificationCode;

    public PickListDeleteInUseModel() {
        super();
        pickListName = createDataModelObj("PickListName");
        pickListCode = createDataModelObj("PickListCode");
        catalogName = createDataModelObj("CatalogName");
        catalogCode = createDataModelObj("CatalogCode");
        specificationName = createDataModelObj("SpecificationName");
        specificationCode = createDataModelObj("SpecificationCode");
    }
}
