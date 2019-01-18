/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mxcg.io;

import com.mxcg.common.util.Bytes;
import com.mxcg.core.exception.TofocusException;

import java.io.DataOutput;
import java.io.DataInput;
import java.io.IOException;
import java.util.Date;



/**
 * A serializable object which implements a simple, efficient, serialization 
 * protocol, based on {@link DataInput} and {@link DataOutput}.
 *
 * <p>Any <code>key</code> or <code>value</code> type in the Hadoop Map-Reduce
 * framework implements this interface.</p>
 * 
 * <p>Implementations typically implement a static <code>read(DataInput)</code>
 * method which constructs a new instance, calls {@link #readFields(DataInput)} 
 * and returns the instance.</p>
 * 
 * <p>Example:</p>
 * <p><blockquote><pre>
 *     public class MyWritable implements Writable {
 *       // Some data     
 *       private int counter;
 *       private long timestamp;
 *       
 *       public void write(DataOutput out) throws IOException {
 *         out.writeInt(counter);
 *         out.writeLong(timestamp);
 *       }
 *       
 *       public void readFields(DataInput in) throws IOException {
 *         counter = in.readInt();
 *         timestamp = in.readLong();
 *       }
 *       
 *       public static MyWritable read(DataInput in) throws IOException {
 *         MyWritable w = new MyWritable();
 *         w.readFields(in);
 *         return w;
 *       }
 *     }
 * </pre></blockquote></p>
 */
public interface Writable
{
    /** 
     * Serialize the fields of this object to <code>out</code>.
     * 
     * @param out <code>DataOuput</code> to serialize this object into.
     * @throws IOException
     */
    void write(DataOutput out)
        throws IOException;
    
    /** 
     * Deserialize the fields of this object from <code>in</code>.  
     * 
     * <p>For efficiency, implementations should attempt to re-use storage in the 
     * existing object where possible.</p>
     * 
     * @param in <code>DataInput</code> to deseriablize this object from.
     * @throws IOException
     */
    void readFields(DataInput in)
        throws IOException;
    
    static final int maxParamSize = 32767;
    
    default void checkSize(byte[] bytes)
    {
        if (bytes.length > maxParamSize)
        {
            throw new TofocusException(TofocusException.REQUIRED_PRARAM_ERROR, "参数长度超过" + maxParamSize + "字节");
        }
    }
    
    /**************************
     * 
     *           写
     * 
     **************************/
    
    /**
     * 把String以UTF-8格式写到变长的byte数组，以一个short和byte[]形式保存
     * 
     * @param out
     * @param str
     * @throws IOException
     */
    default void writeString(DataOutput out, String str)
        throws IOException
    {
        byte[] bytes = Bytes.toBytes(str);
        writeBytes(out, bytes);
    }
    
    default void writeLong(DataOutput out, Long v)
        throws IOException
    {
        byte[] bytes = Bytes.toBytes(v);
        writeBytes(out, bytes);
    }
    
    default void writeInt(DataOutput out, Integer v)
        throws IOException
    {
        byte[] bytes = Bytes.toBytes(v);
        writeBytes(out, bytes);
    }
    
    default void writeShort(DataOutput out, Short v)
        throws IOException
    {
        byte[] bytes = Bytes.toBytes(v);
        writeBytes(out, bytes);
    }
    
    default void writeDate(DataOutput out, Date v)
        throws IOException
    {
        byte[] bytes = Bytes.toBytes(v);
        writeBytes(out, bytes);
    }
    
    default void writeDouble(DataOutput out, Double v)
        throws IOException
    {
        byte[] bytes = Bytes.toBytes(v);
        writeBytes(out, bytes);
    }
    
    default void writeFloat(DataOutput out, Float v)
        throws IOException
    {
        byte[] bytes = Bytes.toBytes(v);
        writeBytes(out, bytes);
    }
    
    default void writeBytes(DataOutput out, byte[] bytes)
        throws IOException
    {
        if (bytes != null)
        {
            checkSize(bytes);
            out.writeShort(bytes.length);
            out.write(bytes);
        }
        else
        {
            out.writeShort(-1);
        }
    }
    
    /**************************
     * 
     *           读
     * 
     **************************/
    
    /**
     * 读取变长String
     * @param in
     * @return
     * @throws IOException
     */
    default String readString(DataInput in)
        throws IOException
    {
        byte[] bytes = readBytes(in);
        return Bytes.toString(bytes);
    }
    
    default byte[] readBytes(DataInput in)
        throws IOException
    {
        short length = in.readShort();
        if (length < 0)
            return null;
        else
        {
            byte[] bytes = new byte[length];
            in.readFully(bytes);
            return bytes;
        }
    }
    
    default Long readLong(DataInput in)
        throws IOException
    {
        byte[] bytes = readBytes(in);
        return bytes == null ? null : Bytes.toLong(bytes);
    }
    
    default Integer readInt(DataInput in)
        throws IOException
    {
        byte[] bytes = readBytes(in);
        return bytes == null ? null : Bytes.toInt(bytes);
    }
    
    default Short readShort(DataInput in)
        throws IOException
    {
        byte[] bytes = readBytes(in);
        return bytes == null ? null : Bytes.toShort(bytes);
    }
    
    default Date readDate(DataInput in)
        throws IOException
    {
        byte[] bytes = readBytes(in);
        return bytes == null ? null : Bytes.toDate(bytes);
    }
    
    default Double readDouble(DataInput in)
        throws IOException
    {
        byte[] bytes = readBytes(in);
        return bytes == null ? null : Bytes.toDouble(bytes);
    }
    
    default Float readFloat(DataInput in)
        throws IOException
    {
        byte[] bytes = readBytes(in);
        return bytes == null ? null : Bytes.toFloat(bytes);
    }
}
