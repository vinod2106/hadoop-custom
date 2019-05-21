package com.vks.hbase.serialize;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.FlumeException;
import org.apache.flume.conf.ComponentConfiguration;
import org.apache.flume.sink.hbase.HbaseEventSerializer;
import org.apache.hadoop.hbase.client.Increment;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Row;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Charsets;
import com.google.common.collect.Lists;

public class BDMPHbaseEventRowKeySerializer implements HbaseEventSerializer {
    private static final Logger logger = LoggerFactory.getLogger(RegexHbaseEventRowKeySerializer.class);

    // Config vars
    /** Regular expression used to parse groups from event data. */
    private static final String REGEX_CONFIG = "regex";

    /** Whether to ignore case when performing regex matches. */
    private static final String IGNORE_CASE_CONFIG = "regexIgnoreCase";
    private static final boolean INGORE_CASE_DEFAULT = false;

    /** Comma separated list of column names to place matching groups in. */
    private static final String COL_NAME_CONFIG = "colName";

    /** Placeholder in colNames for row key */
    private static final String ROW_KEY_NAME = "ROW_KEY";

    protected byte[] columnFamily;
    private byte[] payload;
    private byte[] colName;
    private boolean regexIgnoreCase;
    private Pattern inputPattern;
    private boolean isTraceLogging;

    @Override
    public void configure(Context context) {

        String regex = context.getString(REGEX_CONFIG, "").trim();
        logger.info("regex " + regex);
        if(regex.isEmpty()) {
            throw new IllegalArgumentException(REGEX_CONFIG + " cannot be empty");
        }
        regexIgnoreCase = context.getBoolean(IGNORE_CASE_CONFIG,
                INGORE_CASE_DEFAULT);
        logger.info("regexIgnoreCase " + regexIgnoreCase);
        inputPattern = Pattern.compile(regex, Pattern.DOTALL
                + (regexIgnoreCase ? Pattern.CASE_INSENSITIVE : 0));

        String colNameStr = context.getString(COL_NAME_CONFIG, "").trim();
        isTraceLogging = logger.isTraceEnabled();
    }

    @Override
    public void configure(ComponentConfiguration componentConfiguration) {
        throw new UnsupportedOperationException("ComponentConfiguration is not supported");
    }
    @Override
    public void initialize(Event event, byte[] columnFamily) {
        this.payload = event.getBody();
        this.columnFamily = columnFamily;
    }

    @Override
    public List<Row> getActions() throws FlumeException {
        List<Row> actions = Lists.newArrayList();
        Matcher matcher = inputPattern.matcher(new String(payload, Charsets.UTF_8));
        logger.info("matcher count : " + matcher.groupCount());
       /* logger.info("matcher count 1: "+matcher.group(1));
        logger.info("matcher count 2: "+matcher.group(2));
        logger.info("matcher count 0: "+matcher.group(0));*/
        logger.info("matching :" + new String(payload, Charsets.UTF_8));
        logger.info("pattern : " + inputPattern);
        if (!matcher.matches()) {
            if(isTraceLogging) {
                logger.trace("Payload does not match regex");
            }
            return actions;
        }

        Put put = new Put((matcher.group(1)+"-"+matcher.group(2)).getBytes(Charsets.UTF_8));
        put.addImmutable(columnFamily, "payld".getBytes(Charsets.UTF_8), matcher.group(3).getBytes(Charsets.UTF_8));

        actions.add(put);
        logger.info("actions : " + actions.toString());
        return actions;
    }

    @Override
    public List<Increment> getIncrements() {
        return Lists.newArrayListWithCapacity(0);
    }

    @Override
    public void close() {
    }

}
