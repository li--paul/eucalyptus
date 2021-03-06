/*************************************************************************
 * Copyright 2009-2016 Ent. Services Development Corporation LP
 *
 * Redistribution and use of this software in source and binary forms,
 * with or without modification, are permitted provided that the
 * following conditions are met:
 *
 *   Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 *   Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer
 *   in the documentation and/or other materials provided with the
 *   distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS
 * FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE
 * COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
 * BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN
 * ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 ************************************************************************/
package com.eucalyptus.cloudformation.config;

import com.eucalyptus.configurable.ConfigurableClass;
import com.eucalyptus.configurable.ConfigurableField;
import com.eucalyptus.configurable.PropertyChangeListeners;
import com.eucalyptus.simpleworkflow.common.client.Config;

/**
 *
 */
@ConfigurableClass( root = "cloudformation", description = "Parameters controlling cloud formation")
public class CloudFormationProperties {

  private static final String DEFAULT_SWF_CLIENT_CONFIG =
      "{\"ConnectionTimeout\": 10000, \"MaxConnections\": 100}";

  private static final String DEFAULT_SWF_ACTIVITY_WORKER_CONFIG =
      "{\"PollThreadCount\": 8, \"TaskExecutorThreadPoolSize\": 16, \"MaximumPollRateIntervalMilliseconds\": 50, \"MaximumPollRatePerSecond\": 20 }";

  private static final String DEFAULT_SWF_WORKFLOW_WORKER_CONFIG =
      "{ \"DomainRetentionPeriodInDays\": 1, \"PollThreadCount\": 8, \"MaximumPollRateIntervalMilliseconds\": 50, \"MaximumPollRatePerSecond\": 20 }";

  @ConfigurableField(
    initial = "true",
    description = "Set 'true' to only allow 'known' properties in Resources",
    changeListener = PropertyChangeListeners.IsBoolean.class )
  public static volatile Boolean ENFORCE_STRICT_RESOURCE_PROPERTIES = true;

  @ConfigurableField(
      initial = "CloudFormationDomain",
      description = "The simple workflow service domain for cloudformation",
      changeListener = Config.NameValidatingChangeListener.class )
  public static volatile String SWF_DOMAIN = "CloudFormationDomain";

  @ConfigurableField(
      initial = "CloudFormationTaskList",
      description = "The simple workflow service task list for cloudformation",
      changeListener = Config.NameValidatingChangeListener.class )
  public static volatile String SWF_TASKLIST = "CloudFormationTaskList";

  @ConfigurableField(
      initial = DEFAULT_SWF_CLIENT_CONFIG,
      description = "JSON configuration for the cloudformation simple workflow client",
      changeListener = Config.ClientConfigurationValidatingChangeListener.class )
  public static volatile String SWF_CLIENT_CONFIG = DEFAULT_SWF_CLIENT_CONFIG;

  @ConfigurableField(
      initial = DEFAULT_SWF_ACTIVITY_WORKER_CONFIG,
      description = "JSON configuration for the cloudformation simple workflow activity worker",
      changeListener = Config.ActivityWorkerConfigurationValidatingChangeListener.class )
  public static volatile String SWF_ACTIVITY_WORKER_CONFIG = DEFAULT_SWF_ACTIVITY_WORKER_CONFIG;

  @ConfigurableField(
      initial = DEFAULT_SWF_WORKFLOW_WORKER_CONFIG,
      description = "JSON configuration for the cloudformation simple workflow decision worker",
      changeListener = Config.WorkflowWorkerConfigurationValidatingChangeListener.class )
  public static volatile String SWF_WORKFLOW_WORKER_CONFIG = DEFAULT_SWF_WORKFLOW_WORKER_CONFIG;

  // In case we are using AWS SWF
  public static boolean USE_AWS_SWF = "true".equalsIgnoreCase(System.getProperty("cloudformation.use_aws_swf"));
  public static String AWS_ACCESS_KEY = System.getProperty("cloudformation.aws_access_key", "");
  public static String AWS_SECRET_KEY= System.getProperty("cloudformation.aws_secret_key", "");

}
