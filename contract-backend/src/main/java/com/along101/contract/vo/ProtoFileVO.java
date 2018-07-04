package com.along101.contract.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import sun.rmi.runtime.NewThreadAction;

import java.util.Objects;
import java.util.function.Predicate;

/**
 * Created by qiankai02 on 2017/8/17.
 */
@Data
public class ProtoFileVO extends BaseVO {

    private Long id;

    private String name;

    private Integer status;

    private String appid;

    private Long orgId;

    private String summary;

    private String content;

    private String description;

    private String packageName;

    public enum Status {
        //草稿
        NEW(0),
        //已发布
        RELEASED(1),
        //修改
        UPDATE(2),
        //删除
        DELETE(3);

        private int code;

        Status(int code) {
            this.code = code;
        }

        public int getCode() {
            return this.code;
        }

        public Predicate<ProtoFileVO> filter(){
            return protoFileVO -> protoFileVO.getStatus() == code;
        }

        public static Predicate<ProtoFileVO> filterByOr(Status ... statuses) {
            return protoFileVO -> {
                for (Status stats : statuses) {
                    boolean result  = stats.getCode() == protoFileVO.getStatus();
                    if(result){
                        return true;
                    }
                }
                return false;
            };
        }

        public static Predicate<ProtoFileVO> newOrUpdated() {
            return filterByOr(NEW, UPDATE);
        }

        public static Predicate<ProtoFileVO> notDelted(){
            return filterByOr(DELETE).negate();
        }
    }

    public enum FileHistoryStatus {
        //新增/修改
        CREATE(1),
        //删除
        DELETE(2);

        private int code;

        FileHistoryStatus(int code) {
            this.code = code;
        }

        public int getCode() {
            return this.code;

        }
    }
}
