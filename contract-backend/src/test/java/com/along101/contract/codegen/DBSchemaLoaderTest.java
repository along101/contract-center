package com.along101.contract.codegen;

import com.along101.contract.ContractBackendApplicationTests;
import com.squareup.wire.schema.DBSchemaLoader;
import com.squareup.wire.schema.Schema;
import com.sun.tools.javac.util.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author zhangchengxi
 * Date 2018/5/7
 */
public class DBSchemaLoaderTest extends ContractBackendApplicationTests{

    @Autowired
    private DBSchemaLoader dbSchemaLoader;

    @Test
    public void  testLoad(){
        Schema schema = dbSchemaLoader.loadFromDBByAppIdAndVersion("google","");
        Assert.checkNonNull(schema);
    }
}
