package com.along101.contract.vo;

import lombok.Data;

import java.util.Date;

/**
 * Created by qiankai02 on 2017/8/17.
 */
@Data
public class BaseVO {

    protected String insertBy;

    protected Date insertTime;

    protected String updateBy;

    protected Date updateTime;

    protected Boolean isActive;

}
