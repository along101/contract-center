package com.along101.contract.vo;

import lombok.Data;

/**
 * 定义 api 接口
 */
@Data
public class ApiVO extends BaseVO{

    //标志 entity, 如 User/Org
    //private String modelName;
    private String apiName;

    private String apiURL;

}
