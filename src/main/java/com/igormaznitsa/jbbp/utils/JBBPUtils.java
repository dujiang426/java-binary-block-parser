/* 
 * Copyright 2014 Igor Maznitsa (http://www.igormaznitsa.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.igormaznitsa.jbbp.utils;

import com.igormaznitsa.jbbp.io.JBBPBitOrder;
import com.igormaznitsa.jbbp.model.JBBPAbstractField;
import java.io.ByteArrayOutputStream;
import java.util.*;

public final class JBBPUtils {

  private JBBPUtils() {
  }

  public static boolean isNumber(final String num){
    if (num == null || num.length() == 0) return false;
    final boolean firstIsDigit = Character.isDigit(num.charAt(0));
    if (!firstIsDigit && num.charAt(0)!='-') return false;
    boolean dig = firstIsDigit;
    for(int i=1; i<num.length(); i++){
      if (!Character.isDigit(num.charAt(i))) return false;
      dig = true;
    }
    return dig;
  }
  
  public static byte[] packInt(final int code) {
    if ((code & 0xFFFFFF80) == 0) {
      return new byte[]{(byte) code};
    }
    else if ((code & 0xFFFF0000) == 0) {
      return new byte[]{(byte) 0x80, (byte) (code >>> 8), (byte) code};
    }
    else {
      return new byte[]{(byte) 0x81, (byte) (code >>> 24), (byte) (code >>> 16), (byte) (code >>> 8), (byte) code};
    }
  }

  public static int packInt(final byte[] array, final JBBPPositionCounter position, final int code) {
    if ((code & 0xFFFFFF80) == 0) {
      array[position.getAndIncrease()] = (byte) code;
      return 1;
    }
    else if ((code & 0xFFFF0000) == 0) {
      array[position.getAndIncrease()] = (byte) 0x80;
      array[position.getAndIncrease()] = (byte) (code >>> 8);
      array[position.getAndIncrease()] = (byte) code;
      return 3;
    }
    array[position.getAndIncrease()] = (byte) 0x81;
    array[position.getAndIncrease()] = (byte) (code >>> 24);
    array[position.getAndIncrease()] = (byte) (code >>> 16);
    array[position.getAndIncrease()] = (byte) (code >>> 8);
    array[position.getAndIncrease()] = (byte) code;
    return 5;
  }

  public static int unpackInt(final byte[] array, final JBBPPositionCounter position) {
    final int code = array[position.getAndIncrease()] & 0xFF;
    if (code < 0x80) {
      return code;
    }

    final int result;
    switch (code) {
      case 0x80: {
        result = ((array[position.getAndIncrease()] & 0xFF) << 8) | (array[position.getAndIncrease()] & 0xFF);
      }
      break;
      case 0x81: {
        result = ((array[position.getAndIncrease()] & 0xFF) << 24)
                | ((array[position.getAndIncrease()] & 0xFF) << 16)
                | ((array[position.getAndIncrease()] & 0xFF) << 8)
                | (array[position.getAndIncrease()] & 0xFF);
      }
      break;
      default:
        throw new IllegalArgumentException("Unsupported packed integer prefix [0x" + Integer.toHexString(code).toUpperCase(Locale.ENGLISH) + ']');
    }
    return result;
  }

  public static String array2hex(final byte[] array) {
    return byteArray2String(array, "0x", ", ", true, 16);
  }

  public static String array2bin(final byte[] array) {
    return byteArray2String(array, "0b", ", ", true, 2);
  }

  public static String array2oct(final byte[] array) {
    return byteArray2String(array, "0o", ", ", true, 8);
  }

  public static String byteArray2String(final byte[] array, final String prefix, final String delimiter, final boolean brackets, final int radix) {
    if (array == null) {
      return null;
    }

    final int maxlen = Integer.toString(0xFF, radix).length();
    final String zero = "00000000";

    final String normDelim = delimiter == null ? " " : delimiter;
    final String normPrefix = prefix == null ? "" : prefix;

    final StringBuilder result = new StringBuilder(array.length * 4);

    if (brackets) {
      result.append('[');
    }

    boolean nofirst = false;

    for (final byte b : array) {
      if (nofirst) {
        result.append(normDelim);
      }
      else {
        nofirst = true;
      }

      result.append(normPrefix);

      final String v = Integer.toString(b & 0xFF, radix);
      if (v.length() < maxlen) {
        result.append(zero.substring(0, maxlen - v.length()));
      }
      result.append(v.toUpperCase(Locale.ENGLISH));
    }

    if (brackets) {
      result.append(']');
    }

    return result.toString();
  }

  public static byte reverseLowerBits(final byte value, final int bitsNumber) {
    if (bitsNumber <= 0 || bitsNumber > 8) {
      throw new IllegalArgumentException("Bits number must be in 1..8");
    }
    return (byte) ((reverseByte(value) & 0xFF) >>> (8 - bitsNumber));
  }

  public static byte reverseByte(final byte value) {
    final int v = value & 0xFF;
    return (byte) (((v * 0x0802 & 0x22110) | (v * 0x8020 & 0x88440)) * 0x10101 >> 16);
  }

  public static String bin2str(final byte[] values) {
    return bin2str(values, JBBPBitOrder.LSB0, false);
  }

  public static String bin2str(final byte[] values, final boolean separateBytes) {
    return bin2str(values, JBBPBitOrder.LSB0, separateBytes);
  }

  public static String bin2str(final byte[] values, final JBBPBitOrder bitOrder, final boolean separateBytes) {
    if (values == null) {
      return null;
    }

    final StringBuilder result = new StringBuilder(values.length * (separateBytes ? 9 : 8));

    boolean nofirst = false;
    for (final byte b : values) {
      if (separateBytes) {
        if (nofirst) {
          result.append(' ');
        }
        else {
          nofirst = true;
        }
      }

      int a = b;

      if (bitOrder == JBBPBitOrder.MSB0) {
        for (int i = 0; i < 8; i++) {
          result.append((a & 0x1) == 0 ? '0' : '1');
          a >>= 1;
        }
      }
      else {
        for (int i = 0; i < 8; i++) {
          result.append((a & 0x80) == 0 ? '0' : '1');
          a <<= 1;
        }
      }
    }

    return result.toString();
  }

  public static List<JBBPAbstractField> fieldsAsList(final JBBPAbstractField ... fields){
    final List<JBBPAbstractField> result = new ArrayList<JBBPAbstractField>();
    for(final JBBPAbstractField f : fields){
      result.add(f);
    }
    return result;
  }
  
  public static byte[] str2bin(final String values) {
    return str2bin(values, JBBPBitOrder.LSB0);
  }

  public static byte[] str2bin(final String values, final JBBPBitOrder bitOrder) {
    if (values == null) {
      return new byte[0];
    }

    int buff = 0;
    int cnt = 0;

    final ByteArrayOutputStream buffer = new ByteArrayOutputStream((values.length() + 7) >> 3);

    final boolean msb0 = bitOrder == JBBPBitOrder.MSB0;

    for (final char v : values.toCharArray()) {
      switch (v) {
        case '_':
        case ' ':
          continue;
        case '0':
        case 'X':
        case 'x':
        case 'Z':
        case 'z': {
          if (msb0) {
            buff >>= 1;
          }
          else {
            buff <<= 1;
          }
        }
        break;
        case '1': {
          if (msb0) {
            buff = (buff >> 1) | 0x80;
          }
          else {
            buff = (buff << 1) | 1;
          }
        }
        break;
        default:
          throw new IllegalArgumentException("Detected unsupported char '" + v + ']');
      }
      cnt++;
      if (cnt == 8) {
        buffer.write(buff);
        cnt = 0;
        buff = 0;
      }
    }
    if (cnt > 0) {
      buffer.write(msb0 ? buff>>>(8-cnt) : buff);
    }
    return buffer.toByteArray();
  }

  public static String[] splitString(final String str, final char splitChar) {
    final int length = str.length();
    final StringBuilder bulder = new StringBuilder(Math.max(8, length));

    int counter = 1;
    for (int i = 0; i < length; i++) {
      if (str.charAt(i) == splitChar) {
        counter++;
      }
    }

    final String[] result = new String[counter];

    int position = 0;
    for (int i = 0; i < length; i++) {
      final char chr = str.charAt(i);
      if (chr == splitChar) {
        result[position++] = bulder.toString();
        bulder.setLength(0);
      }else{
        bulder.append(chr);
      }
    }
    if (position < result.length) {
      result[position] = bulder.toString();
    }

    return result;
  }
  
  public static void assertNotNull(final Object object, final String message){
    if (object == null) throw new NullPointerException(message == null ? "Object is null" : message);
  }

  public static String int2msg(final int number){
    return number+" (0x"+Long.toHexString((long)number & 0xFFFFFFFFL).toUpperCase(Locale.ENGLISH)+')';
  }
  
}