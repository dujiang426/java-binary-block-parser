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
package com.igormaznitsa.jbbp.model;

import com.igormaznitsa.jbbp.compiler.JBBPNamedFieldInfo;
import org.junit.Test;
import static org.junit.Assert.*;

public class JBBPFieldIntTest {

  @Test
  public void testNameField() {
    final JBBPFieldInt field = new JBBPFieldInt(new JBBPNamedFieldInfo("test.field", "field", 123), 123456);
    final JBBPNamedFieldInfo namedField = field.getNameInfo();
    assertEquals("test.field", namedField.getFieldPath());
    assertEquals("field", namedField.getFieldName());
    assertEquals(123, namedField.getFieldOffsetInCompiledBlock());
  }

  @Test
  public void testgetAsBool_True() {
    assertTrue(new JBBPFieldInt(new JBBPNamedFieldInfo("test.field", "field", 123), 32423).getAsBool());
  }

  @Test
  public void testgetAsBool_False() {
    assertFalse(new JBBPFieldInt(new JBBPNamedFieldInfo("test.field", "field", 123), 0).getAsBool());
  }

  @Test
  public void testgetAsInt() {
    assertEquals(234324, new JBBPFieldInt(new JBBPNamedFieldInfo("test.field", "field", 123), 234324).getAsInt());
    assertEquals(-234324, new JBBPFieldInt(new JBBPNamedFieldInfo("test.field", "field", 123), -234324).getAsInt());
  }

  @Test
  public void testgetAsLong() {
    assertEquals(234324L, new JBBPFieldInt(new JBBPNamedFieldInfo("test.field", "field", 123), 234324).getAsLong());
    assertEquals(-234324L, new JBBPFieldInt(new JBBPNamedFieldInfo("test.field", "field", 123), -234324).getAsLong());
  }

  
  
}