package com.hbase;

import java.util.List;

import org.apache.flume.Event;
import org.apache.flume.conf.Configurable;
import org.apache.flume.conf.ConfigurableComponent;
import org.apache.hadoop.hbase.client.Increment;
import org.apache.hadoop.hbase.client.Row;

/**
 * Interface for an event serializer which serializes the headers and body
 * of an event to write them to hbase. This is configurable, so any config
 * params required should be taken through this. Only the column family is
 * passed in. The columns should exist in the table and column family
 * specified in the configuration for the HbaseSink.
 *
 */
public interface HbaseEventSerializer extends Configurable,
        ConfigurableComponent {
    /**
     * Initialize the event serializer.
     */
    public void initialize(Event event, byte[] columnFamily);

    /**
     * Get the actions that should be written out to hbase as a result of this
     * event. This list is written to hbase using the HBase batch API.
     * @return List of {@link org.apache.hadoop.hbase.client.Row} which
     * are written as such to HBase.
     *
     * 0.92 increments do not implement Row, so this is not generic.
     *
     */
    public List<Row> getActions();

    public List<Increment> getIncrements();
    /*
     * Clean up any state. This will be called when the sink is being stopped.
     */
    public void close();


}