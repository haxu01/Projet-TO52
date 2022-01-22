package com.haoran.Brainstorming.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;


public class SystemConfig implements Serializable {

    @TableId(type = IdType.AUTO)
    private Integer id;

    // clé de la configuration
    @TableField("`key`")
    private String key;

    //valeur de la configuration
    @TableField("`value`")
    private String value;
    // description de la configuration
    private String description;
    private Integer pid;

    // Type de configuration, les plus courants sont select, input[type=text,url,number,radio,password,email]
    @TableField("`type`")
    private String type;

    //Valeurs dans des types spéciaux, tels que option de radio，select
    @TableField("`option`")
    private String option;

    //besoin de redémarrer après modification?
    @TableField("`reboot`")
    private String reboot;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }

    public String getReboot() {
        return reboot;
    }

    public void setReboot(String reboot) {
        this.reboot = reboot;
    }

    @Override
    public String toString() {
        return "SystemConfig{" + "id=" + id + ", key='" + key + '\'' + ", value='" + value + '\'' + ", description='" +
                description + '\'' + ", pid=" + pid + ", type='" + type + '\'' + ", option='" + option + '\'' + ", reboot='"
                + reboot + '\'' + '}';
    }
}
