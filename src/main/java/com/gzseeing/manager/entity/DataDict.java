package com.gzseeing.manager.entity;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 字典表
 * </p>
 *
 * @author Haowen
 * @since 2018-11-02
 */
@TableName("data_dict")
public class DataDict extends Model<DataDict> {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 模块
     */
    private String module;
    /**
     * 代码名
     */
    private String code;
    /**
     * 名称
     */
    private String name;
    /**
     * 值
     */
    private String value;

    private String script;

    private Integer type;

    /**
     * 排序
     */
    private Integer sort;
    /**
     * 备注
     */
    private String remark;


    public Integer getId() {
        return id;
    }

    public DataDict setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getModule() {
        return module;
    }

    public DataDict setModule(String module) {
        this.module = module;
        return this;
    }

    public String getCode() {
        return code;
    }

    public DataDict setCode(String code) {
        this.code = code;
        return this;
    }

    public String getName() {
        return name;
    }

    public DataDict setName(String name) {
        this.name = name;
        return this;
    }

    public String getValue() {
        return value;
    }

    public DataDict setValue(String value) {
        this.value = value;
        return this;
    }

    public Integer getSort() {
        return sort;
    }

    public DataDict setSort(Integer sort) {
        this.sort = sort;
        return this;
    }

    public String getScript() {
        return script;
    }

    public void setScript(String script) {
        this.script = script;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getRemark() {
        return remark;
    }

    public DataDict setRemark(String remark) {
        this.remark = remark;
        return this;
    }

    public static final String ID = "id";

    public static final String MODULE = "module";

    public static final String CODE = "code";

    public static final String NAME = "name";

    public static final String VALUE = "value";

    public static final String SORT = "sort";

    public static final String REMARK = "remark";

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "DataDict{" +
        ", id=" + id +
        ", module=" + module +
        ", code=" + code +
        ", name=" + name +
        ", value=" + value +
        ", sort=" + sort +
        ", remark=" + remark +
        "}";
    }
}
