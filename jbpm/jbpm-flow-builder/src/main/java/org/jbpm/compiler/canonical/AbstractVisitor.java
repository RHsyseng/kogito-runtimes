/*
 * Copyright 2019 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jbpm.compiler.canonical;

import java.util.Map;
import java.util.Map.Entry;

import com.github.javaparser.ast.expr.BooleanLiteralExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.IntegerLiteralExpr;
import com.github.javaparser.ast.expr.LongLiteralExpr;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.expr.StringLiteralExpr;
import com.github.javaparser.ast.stmt.BlockStmt;

public abstract class AbstractVisitor {

    protected static final String FACTORY_FIELD_NAME = "factory";
    protected static final String KCONTEXT_VAR = "kcontext";

    protected MethodCallExpr addFactoryMethodWithArgs(String factoryField, BlockStmt body, String methodName, Expression... args) {
        return addFactoryMethodWithArgs(body, factoryField, methodName, args);
    }

    protected MethodCallExpr addFactoryMethodWithArgs(BlockStmt body, String object, String methodName, Expression... args) {
        MethodCallExpr variableMethod = new MethodCallExpr(new NameExpr(object), methodName);

        for (Expression arg : args) {
            variableMethod.addArgument(arg);
        }
        body.addStatement(variableMethod);

        return variableMethod;
    }

    protected String getOrDefault(String value, String defaultValue) {
        if (value == null) {
            return defaultValue;
        }

        return value;
    }

    protected void visitMetaData(Map<String, Object> metadata, BlockStmt body, String variableName) {
        for (Entry<String, Object> entry : metadata.entrySet()) {
            Expression value = null;

            if (entry.getValue() instanceof Boolean) {
                value = new BooleanLiteralExpr((Boolean) entry.getValue());
            } else if (entry.getValue() instanceof Integer) {
                value = new IntegerLiteralExpr((Integer) entry.getValue());
            } else if (entry.getValue() instanceof Long) {
                value = new LongLiteralExpr((Long) entry.getValue());
            } else if (entry.getValue() instanceof String) {
                value = new StringLiteralExpr(entry.getValue().toString());
            }
            if (value != null) {
                addFactoryMethodWithArgs(body, variableName, "metaData", new StringLiteralExpr(entry.getKey()), value);
            }
        }
    }
}
