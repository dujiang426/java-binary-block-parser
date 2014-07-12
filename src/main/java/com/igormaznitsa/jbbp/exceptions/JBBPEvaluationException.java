/*
 * Copyright 2014 Igor Maznitsa.
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

package com.igormaznitsa.jbbp.exceptions;

import com.igormaznitsa.jbbp.compiler.varlen.JBBPLengthEvaluator;

/**
 * The Exception is thrown if any errors during execution of an array length evaluator.
 */
public class JBBPEvaluationException extends JBBPException {
  private static final long serialVersionUID = -8580688001091915787L;
  private final JBBPLengthEvaluator evaluator;
  
  public JBBPEvaluationException(final String message, final JBBPLengthEvaluator evaluator) {
    this(message,evaluator,null);
  }

  public JBBPEvaluationException(String message, final JBBPLengthEvaluator evaluator, Throwable cause) {
    super(message, cause);
    this.evaluator = evaluator;
  }
  
  public JBBPLengthEvaluator getEvaluator(){
    return this.evaluator;
  }
  
}
