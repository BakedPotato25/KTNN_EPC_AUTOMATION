package com.ktnn.projects.dataprovider.model;

import com.ktnn.datadriven.BaseModel;
import com.ktnn.datadriven.DataModel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PickListItemOrderModel extends BaseModel {
    public DataModel pickListName;
    public DataModel pickListCode;
    public DataModel item1NameLabel;
    public DataModel item1Code;
    public DataModel item1Value;
    public DataModel item2NameLabel;
    public DataModel item2Code;
    public DataModel item2Value;

    public PickListItemOrderModel() {
        super();
        pickListName = createDataModelObj("PickListName");
        pickListCode = createDataModelObj("PickListCode");
        item1NameLabel = createDataModelObj("Item1NameLabel");
        item1Code = createDataModelObj("Item1Code");
        item1Value = createDataModelObj("Item1Value");
        item2NameLabel = createDataModelObj("Item2NameLabel");
        item2Code = createDataModelObj("Item2Code");
        item2Value = createDataModelObj("Item2Value");
    }
}
