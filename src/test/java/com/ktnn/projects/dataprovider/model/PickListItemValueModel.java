package com.ktnn.projects.dataprovider.model;

import com.ktnn.datadriven.BaseModel;
import com.ktnn.datadriven.DataModel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PickListItemValueModel extends BaseModel {
    public DataModel pickListName;
    public DataModel pickListCode;
    public DataModel itemNameLabel;
    public DataModel itemCode;
    public DataModel valueInput;
    public DataModel expectedValue;

    public PickListItemValueModel() {
        super();
        pickListName = createDataModelObj("PickListName");
        pickListCode = createDataModelObj("PickListCode");
        itemNameLabel = createDataModelObj("ItemNameLabel");
        itemCode = createDataModelObj("ItemCode");
        valueInput = createDataModelObj("ValueInput");
        expectedValue = createDataModelObj("ExpectedValue");
    }
}
