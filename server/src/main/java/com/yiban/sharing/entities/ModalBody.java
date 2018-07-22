package com.yiban.sharing.entities;

import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ModalBody {

    private String name;
    private String value;
    private boolean required;
    private boolean isSelector;
    private List<String> options;
    private String selectValues; //辅助创建使用，多个值以";"切割

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public boolean isSelector() {
        return isSelector;
    }

    public void setSelector(boolean selector) {
        isSelector = selector;
    }

    public List<String> getOptions() {
        if (!StringUtils.isEmpty(this.selectValues)) {
            return Arrays.asList(selectValues.split(";"));
        }else {
            return new ArrayList<>();
        }
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

    public String getSelectValues() {
        return selectValues;
    }

    public void setSelectValues(String selectValues) {
        this.selectValues = selectValues;
    }
}
