/*
 * This code was generated by AWS Flow Framework Annotation Processor.
 * Refer to Amazon Simple Workflow Service documentation at http://aws.amazon.com/documentation/swf 
 *
 * Any changes made directly to this file will be lost when 
 * the code is regenerated.
 */
 package com.eucalyptus.loadbalancing.workflow;

import com.amazonaws.services.simpleworkflow.flow.core.Promise;
import com.amazonaws.services.simpleworkflow.flow.StartWorkflowOptions;
import com.amazonaws.services.simpleworkflow.flow.WorkflowClient;

/**
 * Generated from {@link com.eucalyptus.loadbalancing.workflow.ModifyServicePropertiesWorkflow}. 
 * Used to invoke child workflows asynchronously from parent workflow code.
 * Created through {@link ModifyServicePropertiesWorkflowClientFactory#getClient}.
 * <p>
 * When running outside of the scope of a workflow use {@link ModifyServicePropertiesWorkflowClientExternal} instead.
 */
public interface ModifyServicePropertiesWorkflowClient extends WorkflowClient
{

    /**
     * Generated from {@link com.eucalyptus.loadbalancing.workflow.ModifyServicePropertiesWorkflow#modifyServiceProperties}
     */
    Promise<Void> modifyServiceProperties(String emi, String instanceType, String keyname, String initScript);

    /**
     * Generated from {@link com.eucalyptus.loadbalancing.workflow.ModifyServicePropertiesWorkflow#modifyServiceProperties}
     */
    Promise<Void> modifyServiceProperties(String emi, String instanceType, String keyname, String initScript, Promise<?>... waitFor);

    /**
     * Generated from {@link com.eucalyptus.loadbalancing.workflow.ModifyServicePropertiesWorkflow#modifyServiceProperties}
     */
    Promise<Void> modifyServiceProperties(String emi, String instanceType, String keyname, String initScript, StartWorkflowOptions optionsOverride, Promise<?>... waitFor);

    /**
     * Generated from {@link com.eucalyptus.loadbalancing.workflow.ModifyServicePropertiesWorkflow#modifyServiceProperties}
     */
    Promise<Void> modifyServiceProperties(Promise<String> emi, Promise<String> instanceType, Promise<String> keyname, Promise<String> initScript);

    /**
     * Generated from {@link com.eucalyptus.loadbalancing.workflow.ModifyServicePropertiesWorkflow#modifyServiceProperties}
     */
    Promise<Void> modifyServiceProperties(Promise<String> emi, Promise<String> instanceType, Promise<String> keyname, Promise<String> initScript, Promise<?>... waitFor);

    /**
     * Generated from {@link com.eucalyptus.loadbalancing.workflow.ModifyServicePropertiesWorkflow#modifyServiceProperties}
     */
    Promise<Void> modifyServiceProperties(Promise<String> emi, Promise<String> instanceType, Promise<String> keyname, Promise<String> initScript, StartWorkflowOptions optionsOverride, Promise<?>... waitFor);

}