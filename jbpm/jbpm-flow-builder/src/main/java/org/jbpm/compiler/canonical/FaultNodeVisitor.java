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

import org.jbpm.process.core.context.variable.VariableScope;
import org.jbpm.ruleflow.core.factory.FaultNodeFactory;
import org.jbpm.workflow.core.node.FaultNode;
import org.kie.api.definition.process.Node;

import com.github.javaparser.ast.expr.LongLiteralExpr;
import com.github.javaparser.ast.expr.StringLiteralExpr;
import com.github.javaparser.ast.stmt.BlockStmt;

public class FaultNodeVisitor extends AbstractNodeVisitor {
    
    private static final String NODE_KEY = "faultNode";

    @Override
    public String getNodeKey() {
        return NODE_KEY;
    }
    
    @Override
    public void visitNode(String factoryField, Node node, BlockStmt body, VariableScope variableScope, ProcessMetaData metadata) {
        FaultNode faultNode = (FaultNode) node;
        
        addFactoryMethodWithArgsWithAssignment(factoryField, body, FaultNodeFactory.class, getNodeId(node), "faultNode", new LongLiteralExpr(faultNode.getId()));
        addFactoryMethodWithArgs(body, getNodeId(node), "name", new StringLiteralExpr(getOrDefault(faultNode.getName(), "Error")));
        if (faultNode.getFaultVariable() != null) {
            addFactoryMethodWithArgs(body, getNodeId(node), "setFaultVariable", new StringLiteralExpr(faultNode.getFaultVariable()));
        }
        if (faultNode.getFaultName() != null) {
            addFactoryMethodWithArgs(body, getNodeId(node), "setFaultName", new StringLiteralExpr(faultNode.getFaultName()));
        }

        visitMetaData(faultNode.getMetaData(), body, getNodeId(node));
        
        addFactoryDoneMethod(body, getNodeId(node));
        
    }
}
