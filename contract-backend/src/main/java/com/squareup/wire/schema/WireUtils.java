package com.squareup.wire.schema;

import com.squareup.wire.schema.internal.parser.ProtoFileElement;
import com.squareup.wire.schema.internal.parser.ProtoParser;

/**
 * @author zhangchengxi
 * Date 2018/5/8
 */
public class WireUtils {

    public static ProtoFile getProtoFile(String data){
        ProtoFileElement parse = ProtoParser.parse(Location.get(""), data);
        return ProtoFile.get(parse);
    }
}
