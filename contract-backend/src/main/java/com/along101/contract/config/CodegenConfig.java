package com.along101.contract.config;

import com.along101.contract.dao.ProtoFileEntityMapper;
import com.squareup.wire.schema.DBSchemaLoader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author zhangchengxi
 * Date 2018/5/7
 */
@Configuration
public class CodegenConfig {

    @Bean
    public DBSchemaLoader dbSchemaLoader(){
        return new DBSchemaLoader();
    }

}
