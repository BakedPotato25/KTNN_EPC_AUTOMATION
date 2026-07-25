package com.ktnn.projects.dataprovider.model;

import com.ktnn.datadriven.BaseModel;
import com.ktnn.datadriven.DataModel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PickListFilterModel extends BaseModel {
    public DataModel filterField;
    public DataModel filterOperator;
    public DataModel filterValue;

    public PickListFilterModel() {
        super();
        filterField = createDataModelObj("FilterField");
        filterOperator = createDataModelObj("FilterOperator");
        filterValue = createDataModelObj("FilterValue");
    }
}
