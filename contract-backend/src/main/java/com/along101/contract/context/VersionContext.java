package com.along101.contract.context;

import com.along101.contract.vo.VersionInfo;

import java.util.Optional;

/**
 * @author zhangchengxi
 * Date 2018/6/11
 */
public class VersionContext {

    private static ThreadLocal<VersionInfo> versionInfo = new ThreadLocal<>();


    public static Optional<VersionInfo> getVersionInfo() {
        return Optional.ofNullable(versionInfo.get());
    }

    public static void setVersionInfo(VersionInfo versionInfo) {
        VersionContext.versionInfo.set(versionInfo);
    }

    public static void remove() {
        versionInfo.remove();
    }

    public static boolean isDraft(){
        return VersionInfo.DRAFT.equals(getVersionInfo().map(VersionInfo::getVersionNumber).orElse("draft"));
    }





}
