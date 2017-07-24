package com.hbase;

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

public class TestRegexHbaseEventRowKeySerializer {

    private static final byte[] COL_FAM = "colFam1".getBytes();
    private static final String COL1 = "col1";
    private static final String COL2 = "col2";

    @Test
    public void test() throws Exception {
        Map<String, String> properties = Maps.newHashMap();
        //properties.put("regex", "^([^\t]+)\t([^\t]+)\t([^\t]+)$");
        properties.put("regex","^([^\\|]+)\\|([^\\|]+)\\|([^\\|]+)$");
        properties.put("rowKeyIndex", "0");
        properties.put("colNames",Joiner.on(",").join("ROW_KEY",COL1, COL2));
        Context context = new Context(properties);
        RegexHbaseEventRowKeySerializer serializer = new RegexHbaseEventRowKeySerializer();
        serializer.configure(context);
        String body = "row1|val1|val2";
        serializer.initialize(EventBuilder.withBody(body.getBytes(Charsets.UTF_8)), COL_FAM);
        List<Row> result = serializer.getActions();
        Put put = (Put)result.get(0);
        Assert.assertEquals("val1", getCol(put, "col1"));
        Assert.assertEquals("val2", getCol(put, "col2"));
        Assert.assertEquals("row1", Bytes.toString(put.getRow()));
    }

    private String getCol(Put put, String col) {
        return Bytes.toString(put.get(COL_FAM, col.getBytes()).get(0).getValue());
    }
}