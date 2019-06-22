package com.plugin.hellojava;

import com.dtolabs.rundeck.core.execution.ExecutionContext;
import com.dtolabs.rundeck.core.execution.workflow.steps.FailureReason;
import com.dtolabs.rundeck.core.execution.workflow.steps.StepException;
import com.dtolabs.rundeck.core.plugins.Plugin;
import com.dtolabs.rundeck.core.plugins.configuration.Describable;
import com.dtolabs.rundeck.core.plugins.configuration.Description;
import com.dtolabs.rundeck.plugins.ServiceNameConstants;
import com.dtolabs.rundeck.plugins.descriptions.PluginDescription;
import com.dtolabs.rundeck.plugins.step.PluginStepContext;
import com.dtolabs.rundeck.plugins.step.StepPlugin;
import com.dtolabs.rundeck.plugins.util.DescriptionBuilder;
import com.dtolabs.rundeck.plugins.util.PropertyBuilder;

import org.rundeck.storage.api.Path;
import org.rundeck.storage.api.PathUtil;

import com.dtolabs.rundeck.plugins.PluginLogger;
import com.dtolabs.rundeck.core.plugins.configuration.StringRenderingConstants;
import com.dtolabs.rundeck.core.storage.ResourceMeta;
import com.dtolabs.rundeck.core.storage.StorageTree;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

@Plugin(service = ServiceNameConstants.WorkflowStep, name = "hellojava")
@PluginDescription(title = "hellojava", description = "My WorkflowStep plugin description")
public class Hellojava implements StepPlugin, Describable {

  public static final String SERVICE_PROVIDER_NAME = "hellojava";

  /**
   * Overriding this method gives the plugin a chance to take part in building the
   * {@link com.dtolabs.rundeck.core.plugins.configuration.Description} presented
   * by this plugin. This subclass can use the {@link DescriptionBuilder} to
   * modify all aspects of the description, add or remove properties, etc.
   */
  @Override
  public Description getDescription() {
    return DescriptionBuilder.builder().name(SERVICE_PROVIDER_NAME).title("hellojava")
        .description("Example Workflow Step")
        .property(PropertyBuilder.builder().string("example").title("Example String").description("Example description")
            .required(true).build())
        .property(PropertyBuilder.builder().booleanType("exampleBoolean").title("Example Boolean")
            .description("Example Boolean?").required(false).defaultValue("false").build())
        .property(PropertyBuilder.builder().freeSelect("ExampleFreeSelect").title("Example Free Select")
            .description("Example Free Select").required(false).defaultValue("Blue").values("Blue", "Beige", "Black")
            .build())
        .property(
            PropertyBuilder.builder().string("acl_token").title("Select Nomad ACL token")
                .description("Authentication token for cluster access.").required(true)
                .renderingOption(StringRenderingConstants.SELECTION_ACCESSOR_KEY,
                    StringRenderingConstants.SelectionAccessor.STORAGE_PATH)
                .renderingOption(StringRenderingConstants.STORAGE_PATH_ROOT_KEY, "keys")
                .renderingOption(StringRenderingConstants.STORAGE_FILE_META_FILTER_KEY, "Rundeck-data-type=password")
                .renderingOption(StringRenderingConstants.VALUE_CONVERSION_KEY,
                    StringRenderingConstants.ValueConversion.STORAGE_PATH_AUTOMATIC_READ)
                .defaultValue("").build())
        .build();
  }

  /**
   * This enum lists the known reasons this plugin might fail
   */
  static enum Reason implements FailureReason {
    ExampleReason
  }

  /**
   * Here is the meat of the plugin implementation, which should perform the
   * appropriate logic for your plugin.
   * <p/>
   * The {@link PluginStepContext} provides access to the appropriate Nodes, the
   * configuration of the plugin, and details about the step number and context.
   */
  @Override
  public void executeStep(final PluginStepContext context, final Map<String, Object> configuration)
      throws StepException {
    PluginLogger logger = context.getLogger();
    logger.log(2, "Example step configuration: " + configuration);
    logger.log(2, "Example step num: " + context.getStepNumber());
    logger.log(2, "Example step context: " + context.getStepContext());

    if ("true".equals(configuration.get("exampleBoolean"))) {
      throw new StepException("exampleBoolean was true", Reason.ExampleReason);
    }

    StorageTree st = context.getExecutionContext().getStorageTree();
    ResourceMeta contents = context.getExecutionContext().getStorageTree()
        .getResource(PathUtil.asPath(configuration.get("acl_token").toString())).getContents();
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    try {
      contents.writeContent(byteArrayOutputStream);
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    String password = new String(byteArrayOutputStream.toByteArray());
    logger.log(2, "ACL TOKEN: " + password );
  }

}