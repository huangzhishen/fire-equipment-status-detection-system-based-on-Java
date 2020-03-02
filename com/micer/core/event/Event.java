package com.micer.core.event;

import org.apache.avro.Schema;
import org.apache.avro.Schema.Parser;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.message.BinaryMessageDecoder;
import org.apache.avro.message.BinaryMessageEncoder;
import org.apache.avro.message.SchemaStore;
import org.apache.avro.specific.SpecificData;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Map;

@org.apache.avro.specific.AvroGenerated
public class Event extends org.apache.avro.specific.SpecificRecordBase implements org.apache.avro.specific.SpecificRecord{

    private static final long serialVersionUID = 573204842567330846L;
    public static final Schema SCHEMA$ = new Schema.Parser().parse("{\"type\":\"record\",\"name\":\"Event\",\"namespace\":\"com.micer.core.event\",\"doc\":\"This is Event POJO\",\"fields\":[{\"name\":\"deviceConfigId\",\"type\":\"string\"},{\"name\":\"deviceProtocolId\",\"type\":\"string\"},{\"name\":\"eventId\",\"type\":\"string\"},{\"name\":\"timestamp\",\"type\":\"long\"},{\"name\":\"values\",\"type\":{\"type\":\"map\",\"values\":\"string\"}}]}");
    public static Schema getClassSchema() { return SCHEMA$; }
    private static SpecificData MODEL$ = new SpecificData();
    private static final BinaryMessageEncoder<Event> ENCODER = new BinaryMessageEncoder(MODEL$, SCHEMA$);
    private static final BinaryMessageDecoder<Event> DECODER = new BinaryMessageDecoder(MODEL$, SCHEMA$);
    @Deprecated
    public CharSequence deviceConfigId;
    @Deprecated
    public CharSequence deviceProtocolId;


    public static BinaryMessageDecoder<Event> getDecoder() {
        return DECODER;
    }
    public static BinaryMessageDecoder createDecoder(SchemaStore resolver)
    {
        return new BinaryMessageDecoder(MODEL$, SCHEMA$, resolver);
    }

    public ByteBuffer toByteBuffer() throws IOException
    {
        return ENCODER.encode(this);
    }
    public static Event fromByteBuffer(ByteBuffer b) throws IOException
    {
        return (Event)DECODER.decode(b);
    }
    public Event() {}

    public Event(CharSequence deviceConfigId, CharSequence deviceProtocolId, CharSequence eventId, Long timestamp, Map<CharSequence, CharSequence> values)
    {
        this.deviceConfigId = deviceConfigId;
        this.deviceProtocolId = deviceProtocolId;
        this.eventId = eventId;
        this.timestamp = timestamp;
        this.values = values;
    }

    public Schema getSchema() { return SCHEMA$; }

    public Object get(int field$) {
        switch (field$) {
            case 0:  return deviceConfigId;
            case 1:  return deviceProtocolId;
            case 2:  return eventId;
            case 3:  return timestamp;
            case 4:  return values; }
        throw new org.apache.avro.AvroRuntimeException("Bad index");
    }

    public void put(int field$, Object value$)
    {
        switch (field$) {
            case 0:  deviceConfigId = ((CharSequence)value$); break;
            case 1:  deviceProtocolId = ((CharSequence)value$); break;
            case 2:  eventId = ((CharSequence)value$); break;
            case 3:  timestamp = ((Long)value$).longValue(); break;
            case 4:  values = ((Map)value$); break;
            default:  throw new org.apache.avro.AvroRuntimeException("Bad index");
        }
    }

    public CharSequence getDeviceConfigId()
    {
        return deviceConfigId;
    }

    public void setDeviceConfigId(CharSequence value)
    {
        deviceConfigId = value;
    }

    public CharSequence getDeviceProtocolId()
    {
        return deviceProtocolId;
    }

    public void setDeviceProtocolId(CharSequence value)
    {
        deviceProtocolId = value;
    }

    public CharSequence getEventId()
    {
        return eventId;
    }

    public void setEventId(CharSequence value)
    {
        eventId = value;
    }

    public Long getTimestamp()
    {
        return Long.valueOf(timestamp);
    }


    public void setTimestamp(Long value)
    {
        timestamp = value.longValue();
    }

    public Map<CharSequence, CharSequence> getValues()
    {
        return values;
    }


    public void setValues(Map<CharSequence, CharSequence> value)
    {
        values = value;
    }

    /*public static Event.Builder newBuilder()
    {
        return new Event.Builder(null);
    }*/

    /*public static Event.Builder newBuilder(Event.Builder other)
    {
        return new Event.Builder(other, null);
    }*/

    /*public static Event.Builder newBuilder(Event other)
    {
        return new Event.Builder(other, null);
    }*/

    @Deprecated
    public CharSequence eventId;

    @Deprecated
    public long timestamp;

    @Deprecated
    public Map<CharSequence, CharSequence> values;

    private static final DatumWriter<Event> WRITER$ = MODEL$.createDatumWriter(SCHEMA$);

    public void writeExternal(java.io.ObjectOutput out) throws IOException
    {
        WRITER$.write(this, SpecificData.getEncoder(out));
    }

    private static final org.apache.avro.io.DatumReader<Event> READER$ = MODEL$.createDatumReader(SCHEMA$);

    public void readExternal(java.io.ObjectInput in) throws IOException, IOException {
        READER$.read(this, SpecificData.getDecoder(in));
    }

}
