package com.hbase;

import java.util.LinkedList;
import java.util.List;

import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.FlumeException;
import org.apache.flume.conf.ComponentConfiguration;
import org.apache.hadoop.hbase.client.Increment;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Row;

import com.google.common.base.Charsets;

public class NewHbaseEventSerializer implements HbaseEventSerializer  {
    private String rowPrefix;
    private byte[] incrementRow;
    private byte[] cf;
    private byte[] plCol;
    private byte[] incCol;
    private KeyType keyType;
    private byte[] payload;

    public NewHbaseEventSerializer(){

    }

    @Override
    public void configure(Context context) {
        rowPrefix = context.getString("rowPrefix", "default");
        incrementRow =
                context.getString("incrementRow", "incRow").getBytes(Charsets.UTF_8);
        String suffix = context.getString("suffix", "uuid");

        String payloadColumn = context.getString("payloadColumn");
        String incColumn = context.getString("incrementColumn");
        if(payloadColumn != null && !payloadColumn.isEmpty()) {
            if(suffix.equals("timestamp")){
                keyType = KeyType.TS;
            } else if (suffix.equals("random")) {
                keyType = KeyType.RANDOM;
            } else if(suffix.equals("nano")){
                keyType = KeyType.TSNANO;
            } else {
                keyType = KeyType.UUID;
            }
            plCol = payloadColumn.getBytes(Charsets.UTF_8);
        }
        if(incColumn != null && !incColumn.isEmpty()) {
            incCol = incColumn.getBytes(Charsets.UTF_8);
        }
    }

    @Override
    public void configure(ComponentConfiguration conf) {
    }

    @Override
    public void initialize(Event event, byte[] cf) {
        this.payload = event.getBody();
        this.cf = cf;
    }

    @Override
    public List<Row> getActions() throws FlumeException {
        List<Row> actions = new LinkedList<Row>();
        if(plCol != null){
            byte[] rowKey;
            try {
                if (keyType == KeyType.TS) {
                    rowKey = SimpleRowKeyGenerator.getTimestampKey(rowPrefix);
                } else if(keyType == KeyType.RANDOM) {
                    rowKey = SimpleRowKeyGenerator.getRandomKey(rowPrefix);
                } else if(keyType == KeyType.TSNANO) {
                    rowKey = SimpleRowKeyGenerator.getNanoTimestampKey(rowPrefix);
                } else {
                    rowKey = SimpleRowKeyGenerator.getUUIDKey(rowPrefix);
                }
                Put put = new Put(rowKey);
                put.add(cf, plCol, payload);
                actions.add(put);
            } catch (Exception e){
                throw new FlumeException("Could not get row key!", e);
            }

        }
        return actions;
    }
    @Override
    public List<Increment> getIncrements(){
        List<Increment> incs = new LinkedList<Increment>();
        if(incCol != null) {
            Increment inc = new Increment(incrementRow);
            inc.addColumn(cf, incCol, 1);
            incs.add(inc);
        }
        return incs;
    }

    @Override
    public void close() {
    }

    public enum KeyType{
        UUID,
        RANDOM,
        TS,
        TSNANO
    }
}