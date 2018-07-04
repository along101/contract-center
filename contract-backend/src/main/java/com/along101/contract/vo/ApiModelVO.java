package com.along101.contract.vo;

import lombok.Data;

import java.util.List;

/**
 * 表示模型 api,如 user、app 实体,每个实体包括 createUser, deleteUser 等若干个 api 接口
 */
@Data
public class ApiModelVO extends BaseVO{

    //private String appId;

    private String modelName;

    private List<ApiVO> apiVOList;

}
