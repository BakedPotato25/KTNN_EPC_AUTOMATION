package com.ktnn.datadriven;

import lombok.*;
import org.apache.logging.log4j.util.Strings;

import java.util.Objects;

import static com.ktnn.controller.WebUI.getLanguageValue;

/**
 * DataModel: saving data for each web element
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DataModel {
    public String devName;         // DEV Name
    public String description;     // The content at web element
    public String value;           // The value inserted
    public String title;
    public boolean fill;           // Determine the column need to fill, or not
    public boolean verify;         // Determine the column need to verify, or not
    public String langProperty;    // The property in bundle


    /**
     * Init a new DataModel
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
     * Get title of the data
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
     * Update the title when re-change langProperty
     *
     * @param langProperty : property in bundle
     */
    public void updateLangProperty(String langProperty) {
        this.langProperty = langProperty;
        getTitle();
    }
}
