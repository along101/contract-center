package com.along101.contract.vo;

import lombok.Data;

import java.util.List;

@Data
public class ApiDocumentVO {

    private String appId;

    private String appName;

    private List<ApiModelVO> apiModelVOList;

}
