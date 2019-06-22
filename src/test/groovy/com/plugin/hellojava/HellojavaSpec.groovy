package com.plugin.hellojava

import com.dtolabs.rundeck.plugins.step.PluginStepContext
import com.dtolabs.rundeck.core.execution.workflow.steps.StepException
import com.dtolabs.rundeck.core.data.BaseDataContext
import com.dtolabs.rundeck.plugins.PluginLogger
import spock.lang.Specification

class HellojavaSpec extends Specification {

    def getContext(PluginLogger logger){
        Mock(PluginStepContext){
            getLogger()>>logger
/*            getFrameworkProject() >> "TEST"
            getDataContext() >> new BaseDataContext([:])
            getExecutionContext() >> Mock(ExecutionContext) {
                getFramework() >> framework
                getFrameworkProject() >> "TEST"
                getStorageTree() >> Mock(StorageTree)
                }*/
        }
    }

    def "check Boolean parameter"(){

        given:

        def example = new Hellojava()
        def context = getContext(Mock(PluginLogger))
        def configuration = [example:"example123",exampleBoolean:"true"]

        when:
        example.executeStep(context,configuration)

        then:
        thrown StepException
    }
/*
    def "run OK"(){

        given:

        def example = new Hellojava()

        def context = getContext(Mock(PluginLogger))

        def configuration = [
            example:"example123",
            exampleBoolean:"false",
            exampleFreeSelect:"Beige",
            acl_token:"keys/nomad/dev/acl_token"]

        when:
        example.executeStep(context,configuration)

        then:
        1 * logger.log(2, 'Example step configuration: {example=example123, exampleBoolean=false, exampleFreeSelect=Beige, acl_token=keys/nomad/dev/acl_token}')
        //1 * logger.log(2, "ACL_TOKEN: kakafuti")
    }
*/
}