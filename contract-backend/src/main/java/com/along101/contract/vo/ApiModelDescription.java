package com.along101.contract.vo;

import lombok.Data;

/**
 * 用户展示接口基本信息
 */
@Data
public class ApiModelDescription extends BaseVO{

    private String modelName;

    private String description;

}
