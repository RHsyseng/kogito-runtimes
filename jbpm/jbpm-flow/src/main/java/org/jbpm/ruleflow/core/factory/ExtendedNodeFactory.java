package org.jbpm.ruleflow.core.factory;

import java.util.ArrayList;
import java.util.List;

import org.jbpm.ruleflow.core.RuleFlowNodeContainerFactory;
import org.jbpm.workflow.core.DroolsAction;
import org.jbpm.workflow.core.NodeContainer;
import org.jbpm.workflow.core.impl.DroolsConsequenceAction;
import org.jbpm.workflow.core.impl.ExtendedNodeImpl;

public abstract class ExtendedNodeFactory extends NodeFactory {

    protected ExtendedNodeFactory(RuleFlowNodeContainerFactory nodeContainerFactory, NodeContainer nodeContainer, long id) {
        super(nodeContainerFactory, nodeContainer, id);
    }

    protected ExtendedNodeImpl getExtendedNode() {
        return (ExtendedNodeImpl) super.getNode();
    }

    public ExtendedNodeFactory onEntryAction(String dialect, String action) {
        if (getExtendedNode().getActions(dialect) != null) {
            getExtendedNode().getActions(dialect).add(new DroolsConsequenceAction(dialect, action));
        } else {
            List<DroolsAction> actions = new ArrayList<>();
            actions.add(new DroolsConsequenceAction(dialect, action));
            getExtendedNode().setActions(ExtendedNodeImpl.EVENT_NODE_ENTER, actions);
        }
        return this;
    }

    public ExtendedNodeFactory onExitAction(String dialect, String action) {
        if (getExtendedNode().getActions(dialect) != null) {
            getExtendedNode().getActions(dialect).add(new DroolsConsequenceAction(dialect, action));
        } else {
            List<DroolsAction> actions = new ArrayList<>();
            actions.add(new DroolsConsequenceAction(dialect, action));
            getExtendedNode().setActions(ExtendedNodeImpl.EVENT_NODE_EXIT, actions);
        }
        return this;
    }
}
