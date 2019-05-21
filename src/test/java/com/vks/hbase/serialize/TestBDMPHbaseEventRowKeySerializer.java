package com.vks.hbase.serialize;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.apache.flume.Context;
import org.apache.flume.event.EventBuilder;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Row;
import org.apache.hadoop.hbase.util.Bytes;
import org.junit.Test;

import com.google.common.base.Charsets;
import com.google.common.base.Joiner;
import com.google.common.collect.Maps;
import com.vks.hbase.serialize.BDMPHbaseEventRowKeySerializer;

public class TestBDMPHbaseEventRowKeySerializer {
    private static final byte[] COL_FAM = "cf".getBytes();
    private static final String COL1 = "payload";

    @Test
    public void test() throws Exception {
        Map<String, String> properties = Maps.newHashMap();
        //properties.put("regex", "(\\\\w*)\\\\|(\\\\d*)\\\\|(.*)");
        //properties.put("regex","((\\w*)+)\\|(\\d*))\\|([\\.*])");
        properties.put("regex","^([^\t]+)\\|([^\t]+)\\|([^\t]+)$");
        properties.put("colName", COL1);
        Context context = new Context(properties);
        BDMPHbaseEventRowKeySerializer serializer = new BDMPHbaseEventRowKeySerializer();
        serializer.configure(context);
        String body = "row1|val1|val2";
        serializer.initialize(EventBuilder.withBody(body.getBytes(Charsets.UTF_8)), COL_FAM);
        List<Row> result = serializer.getActions();
        Put put = (Put)result.get(0);
        System.out.println(" column name " + getCol(put, "payld"));
        System.out.println("row key "+ Bytes.toString(put.getRow()));
        //Assert.assertEquals("val2", getCol(put, "payload"));
        //Assert.assertEquals("row1-val1", Bytes.toString(put.getRow()));
    }

    private String getCol(Put put, String col) {
        return Bytes.toString(put.get(COL_FAM, col.getBytes()).get(0).getValueArray());
    }
}
