/*
 * Copyright 2000-2009 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * class NewArrayInstanceEvaluator
 * created Jun 27, 2001
 * @author Jeka
 */
package org.jf.smalidea.debugging.expression;

import com.intellij.debugger.engine.DebugProcessImpl;
import com.intellij.debugger.engine.JVMName;
import com.intellij.debugger.engine.evaluation.EvaluateException;
import com.intellij.debugger.engine.evaluation.EvaluateExceptionUtil;
import com.intellij.debugger.engine.evaluation.EvaluationContextImpl;
import com.intellij.debugger.engine.evaluation.expression.Evaluator;
import com.intellij.debugger.engine.evaluation.expression.Modifier;
import com.intellij.debugger.impl.DebuggerUtilsEx;
import com.intellij.debugger.DebuggerBundle;
import com.sun.jdi.ClassType;
import com.sun.jdi.Method;
import com.sun.jdi.ObjectReference;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/*
 * This class is copied wholesale from
 * java/debugger/impl/src/com/intellij/debugger/engine/evaluation/expression/NewClassInstanceEvaluator.java
 * at revision 0d2b409276faa90a10b5c62fb740ab07eb7fa0c3 (Oct-11 2009)
 */
public class NewClassInstanceEvaluator implements Evaluator {
  private final Evaluator myClassTypeEvaluator;
  private final JVMName myConstructorSignature;
  private final Evaluator[] myParamsEvaluators;

  public NewClassInstanceEvaluator(Evaluator classTypeEvaluator, JVMName constructorSignature, Evaluator[] argumentEvaluators) {
    myClassTypeEvaluator = classTypeEvaluator;
    myConstructorSignature = constructorSignature;
    myParamsEvaluators = argumentEvaluators;
  }

  public Object evaluate(EvaluationContextImpl context) throws EvaluateException {
    DebugProcessImpl debugProcess = context.getDebugProcess();
    Object obj = myClassTypeEvaluator.evaluate(context);
    if (!(obj instanceof ClassType)) {
      throw EvaluateExceptionUtil.createEvaluateException(DebuggerBundle.message("evaluation.error.cannot.evaluate.class.type"));
    }
    ClassType classType = (ClassType)obj;
    // find constructor
    Method method = DebuggerUtilsEx.findMethod(classType, "<init>", myConstructorSignature.getName(debugProcess));
    if (method == null) {
      throw EvaluateExceptionUtil.createEvaluateException(
        DebuggerBundle.message("evaluation.error.cannot.resolve.constructor", myConstructorSignature.getDisplayName(debugProcess)));
    }
    // evaluate arguments
    List<Object> arguments;
    if (myParamsEvaluators != null) {
      arguments = new ArrayList<Object>(myParamsEvaluators.length);
      for (Evaluator evaluator : myParamsEvaluators) {
        arguments.add(evaluator.evaluate(context));
      }
    }
    else {
      arguments = Collections.emptyList();
    }
    ObjectReference objRef;
    try {
      objRef = debugProcess.newInstance(context, classType, method, arguments);
    }
    catch (EvaluateException e) {
      throw EvaluateExceptionUtil.createEvaluateException(e);
    }
    return objRef;
  }

  public Modifier getModifier() {
    return null;
  }
}