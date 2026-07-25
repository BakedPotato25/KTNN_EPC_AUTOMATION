package com.ktnn.projects.dataprovider.model;

import com.ktnn.datadriven.BaseModel;
import com.ktnn.datadriven.DataModel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PickListSortModel extends BaseModel {
    public DataModel orderField;
    public DataModel sortDirection;

    public PickListSortModel() {
        super();
        orderField = createDataModelObj("OrderField");
        sortDirection = createDataModelObj("SortDirection");
    }
}
