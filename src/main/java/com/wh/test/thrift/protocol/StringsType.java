package com.wh.test.thrift.protocol;

import org.apache.thrift.TBase;
import org.apache.thrift.TException;
import org.apache.thrift.TFieldIdEnum;
import org.apache.thrift.TSerializable;
import org.apache.thrift.protocol.TProtocol;

/**
 * 2018/4/26
 */
public class StringsType implements TBase<StringsType, StringsType.Fields>, TSerializable, Comparable<StringsType> {

  private String[] strs;

  public StringsType() {
    strs = new String[0];
  }

  public StringsType(String... strs) {
    this.strs = strs;
  }

  public String[] getStrs() {
    return strs;
  }

  @Override
  public StringsType.Fields fieldForId(int fieldId) {
    return StringsType.Fields.values()[fieldId - 1];
  }

  @Override
  public boolean isSet(StringsType.Fields field) {
    return true;
  }

  @Override
  public Object getFieldValue(StringsType.Fields field) {
    switch (field.id) {
      case 1:
        return strs;
      default:
        return null;
    }
  }

  @Override
  public void setFieldValue(StringsType.Fields field, Object object) {
    switch (field.id) {
      case 1:
        strs = (String[]) object;
      default:
        throw new IllegalArgumentException("unknow field: " + field);
    }
  }

  @Override
  public StringsType deepCopy() {
    String[] tmp = new String[strs.length];
    System.arraycopy(strs, 0, tmp, 0, strs.length);
    return new StringsType(tmp);
  }

  @Override
  public void clear() {
    strs = new String[0];
  }

  /*
   * TSerializable
   */

  @Override
  public void read(TProtocol iprot) throws TException {
    final short length = iprot.readI16();
    strs = new String[length];
    for (int i = 0; i < length; i++) {
      strs[i] = iprot.readString();
    }
  }

  @Override
  public void write(TProtocol oprot) throws TException {
    oprot.writeI16((short) strs.length);
    for (String str : strs) {
      oprot.writeString(str);
    }
  }

  /*
   * Comparable
   */

  @Override
  public int compareTo(StringsType o) {
    if (o == null) {
      return 1;
    }
    if (strs.length < o.strs.length) {
      return -1;
    } else if (strs.length > o.strs.length) {
      return 1;
    } else {
      int r;
      for (int i = 0; i < strs.length; i++) {
        r = strs[i].compareTo(o.strs[i]);
        if (r != 0) {
          return r;
        }
      }
      return 0;
    }
  }

  public enum Fields implements TFieldIdEnum {
    STRS(1, "strs");

    private final short id;
    private final String name;

    Fields(int id, String name) {
      this.id = (short) id;
      this.name = name;
    }

    public short getThriftFieldId() {
      return id;
    }

    public String getFieldName() {
      return name;
    }

    @Override
    public String toString() {
      return "Fields{" +
          "id=" + id +
          ", name='" + name + '\'' +
          '}';
    }
  }
}
