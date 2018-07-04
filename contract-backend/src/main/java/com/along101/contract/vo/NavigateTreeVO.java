package com.along101.contract.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class NavigateTreeVO {

    private Long id;

    private Long parentId;

    private List<NavigateTreeVO> children;

    private Long protoId;

    private String name;

    private String summary;

    private String type;

    /**
     *  proto file 的 state
     */
    private Integer status;

    private String label;

    private String createdBy;

    private String updatedBy;

    private Date updatedTime;

    private Date createdTime;

    public static enum TypeEnum{
        PROTO("proto"),
        SERVICE("service"),
        METHOD("method");

        private String name;

        TypeEnum(String name) {
            this.name = name;
        }

        public String getName(){
            return name;
        }
    }
}
