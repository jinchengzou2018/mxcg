package com.mxcg.core.data;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.mxcg.common.util.security.MD5;
import com.mxcg.io.Writable;
import org.springframework.data.annotation.Transient;



public abstract class SignatureData implements Writable
{
    private String signature;
    
    public String signature(String key)
    {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(outStream);
        try
        {
            this.write(out);
            out.writeChars(key);
            
            byte[] bytes = outStream.toByteArray();
            
            signature = MD5.getMD5(bytes);
            return signature;
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return "";
        }
    }
    
    public boolean checkSignature(String key)
    {
        if(signature == null)
            return false;
        else
        {
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            DataOutputStream out = new DataOutputStream(outStream);
            try
            {
                this.write(out);
                out.writeChars(key);
                
                byte[] bytes = outStream.toByteArray();
                
                return signature.equals(MD5.getMD5(bytes));
            }
            catch (IOException e)
            {
                e.printStackTrace();
                return false;
            }
        }
    }

    @Transient
    public String getSignature()
    {
        return signature;
    }

    @Transient
    public void setSignature(String signature)
    {
        this.signature = signature;
    }

    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("[signature=");
        builder.append(signature);
        builder.append("]");
        return builder.toString();
    }
    


}
