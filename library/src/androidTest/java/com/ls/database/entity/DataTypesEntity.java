/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Lemberg Solutions
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.ls.database.entity;

import com.ls.database.util.Logger;

import java.util.Arrays;

/**
 * @author Stanislav Bodnar, Lemberg Solutions
 */
public class DataTypesEntity {

    private static Logger mLogger = new Logger(DataTypesEntity.class.getCanonicalName());

    private Long mId;
    private String mString;
    private Byte mByte;
    private Short mShort;
    private Integer mInt;
    private Long mLong;
    private Float mFloat;
    private Double mDouble;
    private Boolean mBoolean;
    private byte[] mBytes;
    private EnumValue mEnumValue;

    public long getId() {
        return mId;
    }

    public void setId(long id) {
        mId = id;
    }

    public String getString() {
        return mString;
    }

    public void setString(String string) {
        mString = string;
    }

    public byte getByte() {
        return mByte;
    }

    public void setByte(byte aByte) {
        mByte = aByte;
    }

    public short getShort() {
        return mShort;
    }

    public void setShort(short aShort) {
        mShort = aShort;
    }

    public int getInt() {
        return mInt;
    }

    public void setInt(int anInt) {
        mInt = anInt;
    }

    public long getLong() {
        return mLong;
    }

    public void setLong(long aLong) {
        mLong = aLong;
    }

    public float getFloat() {
        return mFloat;
    }

    public void setFloat(float aFloat) {
        mFloat = aFloat;
    }

    public double getDouble() {
        return mDouble;
    }

    public void setDouble(double aDouble) {
        mDouble = aDouble;
    }

    public boolean isBoolean() {
        return mBoolean;
    }

    public void setBoolean(boolean aBoolean) {
        mBoolean = aBoolean;
    }

    public byte[] getBytes() {
        return mBytes;
    }

    public void setBytes(byte[] bytes) {
        mBytes = bytes;
    }

    public EnumValue getEnumValue() {
        return mEnumValue;
    }

    public void setEnumValue(EnumValue enumValue) {
        mEnumValue = enumValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DataTypesEntity)) {
            mLogger.error(DataTypesEntity.class.getSimpleName() + " not equal");
            return false;
        }

        DataTypesEntity entity = (DataTypesEntity) o;

        if (mBoolean != null ? !mBoolean.equals(entity.mBoolean) : entity.mBoolean != null) {
            mLogger.error("Boolean not equal");
            return false;
        }
        if (mByte != null ? !mByte.equals(entity.mByte) : entity.mByte != null) {
            mLogger.error("Byte not equal");
            return false;
        }
        if (!Arrays.equals(mBytes, entity.mBytes)) {
            mLogger.error("Bytes not equal");
            return false;
        }
        if (mDouble != null ? !mDouble.equals(entity.mDouble) : entity.mDouble != null) {
            mLogger.error("Double not equal");
            return false;
        }
        if (mEnumValue != entity.mEnumValue) {
            mLogger.error("EnumValue not equal");
            return false;
        }
        if (mFloat != null ? !mFloat.equals(entity.mFloat) : entity.mFloat != null) {
            mLogger.error("Float not equal");
            return false;
        }
        if (mId != null ? !mId.equals(entity.mId) : entity.mId != null) {
            mLogger.error("Id not equal");
            return false;
        }
        if (mInt != null ? !mInt.equals(entity.mInt) : entity.mInt != null) {
            mLogger.error("Integer not equal");
            return false;
        }
        if (mLong != null ? !mLong.equals(entity.mLong) : entity.mLong != null) {
            mLogger.error("Long not equal");
            return false;
        }
        if (mShort != null ? !mShort.equals(entity.mShort) : entity.mShort != null) {
            mLogger.error("Short not equal");
            return false;
        }
        if (mString != null ? !mString.equals(entity.mString) : entity.mString != null) {
            mLogger.error("String not equal");
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = mLogger != null ? mLogger.hashCode() : 0;
        result = 31 * result + (mId != null ? mId.hashCode() : 0);
        result = 31 * result + (mString != null ? mString.hashCode() : 0);
        result = 31 * result + (mByte != null ? mByte.hashCode() : 0);
        result = 31 * result + (mShort != null ? mShort.hashCode() : 0);
        result = 31 * result + (mInt != null ? mInt.hashCode() : 0);
        result = 31 * result + (mLong != null ? mLong.hashCode() : 0);
        result = 31 * result + (mFloat != null ? mFloat.hashCode() : 0);
        result = 31 * result + (mDouble != null ? mDouble.hashCode() : 0);
        result = 31 * result + (mBoolean != null ? mBoolean.hashCode() : 0);
        result = 31 * result + (mBytes != null ? Arrays.hashCode(mBytes) : 0);
        result = 31 * result + (mEnumValue != null ? mEnumValue.hashCode() : 0);
        return result;
    }
}
