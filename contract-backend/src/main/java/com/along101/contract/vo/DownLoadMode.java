package com.along101.contract.vo;

/**
 * @author zhangchengxi
 * Date 2018/6/19
 */
public enum DownLoadMode {
    /**
     * 下载服务端工程,包括proto文件和pom文件
     */
    SERVER("server"),

    /**
     * 下载客户端工程
     */
    CLIENT("client"),

    /**
     * 下载swagger.json文件
     */
    SWAGGER("swagger"),

    /**
     * 下载proto文件
     */
    FILE("file");

    private String mode;

    DownLoadMode(String mode) {
        this.mode = mode;
    }

    public String getMode() {
        return mode;
    }


}
