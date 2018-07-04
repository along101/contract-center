package com.along101.contract.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zhangchengxi
 * Date 2018/6/11
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VersionInfo {
    /**
     * 用户进入系统后默认选择的version number 草稿
     */
    public static final String DRAFT = "draft";
    private String versionNumber = DRAFT;

    public static boolean isDraft(String versionNumber){
        return DRAFT.equals(versionNumber);
    }
}