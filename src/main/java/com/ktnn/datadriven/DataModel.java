package com.ktnn.datadriven;

import lombok.*;
import org.apache.logging.log4j.util.Strings;

import java.util.Objects;

import static com.ktnn.controller.WebUI.getLanguageValue;

/**
 * DataModel: lưu dữ liệu cho từng web element
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DataModel {
    public String devName;         // DEV Name
    public String description;     // Nội dung tại web element
    public String value;           // Giá trị được nhập
    public String title;
    public boolean fill;           // Xác định cột có cần fill hay không
    public boolean verify;         // Xác định cột có cần verify hay không
    public String langProperty;    // Property trong bundle


    /**
     * Khởi tạo DataModel mới
     */
    public DataModel(DataModel model) {
        this.devName = model.getDevName();
        this.description = model.getDescription();
        this.value = model.getValue();
        this.title = model.getTitle();
        this.fill = model.isFill();
        this.verify = model.isVerify();
        this.langProperty = model.getLangProperty();
    }

    /**
     * Lấy title của data
     */
    public String getTitle() {
        if (Objects.nonNull(this.langProperty) && !Strings.isEmpty(this.langProperty)) {
            this.title = getLanguageValue(this.langProperty);
            return this.title;
        }

        if (Objects.nonNull(this.title) && !Strings.isEmpty(this.title)) {
            return this.title;
        }
        return Strings.EMPTY;
    }

    /**
     * Cập nhật title khi đổi lại langProperty
     *
     * @param langProperty : property trong bundle
     */
    public void updateLangProperty(String langProperty) {
        this.langProperty = langProperty;
        getTitle();
    }
}
