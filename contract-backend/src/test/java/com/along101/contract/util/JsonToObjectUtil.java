package com.along101.contract.util;

import com.alibaba.fastjson.JSON;
import com.along101.contract.entity.ProtoFileEntity;
import com.along101.contract.entity.ProtoFileEntity;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * Created by qiankai02 on 2017/11/9.
 */
@Slf4j
public class JsonToObjectUtil {

    //return object from JSON string
    public static <T> T getTestObject(String rs, Class<T> clazz) throws Exception {
        return JSON.parseObject(readSourceFile(rs), clazz);
    }

    //return object list from JSON array string
    public static <T> List<T> getTestList(String rs, Class<T> clazz) throws Exception {
        return JSON.parseArray(readSourceFile(rs), clazz);
    }

    //return JSON string from json file
    public static String readSourceFile(String rs) throws Exception {
        Class<JsonToObjectUtil> testHelperClass = JsonToObjectUtil.class;
        ClassLoader classLoader = testHelperClass.getClassLoader();
        URI uri = classLoader.getResource(rs).toURI();
        byte[] bytes = Files.readAllBytes(Paths.get(uri));
        return new String(bytes, "UTF-8");
    }

    @Test
    public void testGetTestObject() throws Exception {
        ProtoFileEntity testCloudObject = getTestObject("service/ContractDto.json", ProtoFileEntity.class);
        log.info(JSON.toJSONString(testCloudObject));
    }

    @Test
    public void testGetTestList() throws Exception {
        List<ProtoFileEntity> testCloudList = getTestList("service/ContractArray.json", ProtoFileEntity.class);
        log.info(JSON.toJSONString(testCloudList));
    }

}
