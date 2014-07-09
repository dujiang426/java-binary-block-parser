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
package com.igormaznitsa.jbbp.it;

import java.io.Closeable;
import java.io.InputStream;

public abstract class AbstractParserIntegrationTest {

  public InputStream getResourceAsInputStream(final String resourceName) throws Exception {
    final InputStream result = this.getClass().getResourceAsStream(resourceName);
    if (result == null) {
      throw new NullPointerException("Can't find resource '" + resourceName + '\'');
    }
    return result;
  }
  
  public void closeResource(final Closeable resource){
    try{
      resource.close();
    }catch(Throwable thr){
      thr.printStackTrace();
    }
  }
}