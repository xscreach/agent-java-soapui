/*
 * Copyright (C) 2019 EPAM Systems
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
package com.epam.reportportal.soapui.results;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.epam.reportportal.utils.markdown.MarkdownUtils;
import com.epam.ta.reportportal.ws.model.log.SaveLogRQ;
import com.eviware.soapui.impl.wsdl.teststeps.WsdlGroovyScriptTestStep;
import com.eviware.soapui.model.testsuite.TestStepResult;

/**
 * @author Andrei Varabyeu
 */
public class GroovyScriptLogger extends ResultLogger<TestStepResult> {

  public GroovyScriptLogger() {
    super(TestStepResult.class);
  }

  @Override
  public boolean supports(TestStepResult result) {
    return super.supports(result) && WsdlGroovyScriptTestStep.class.isAssignableFrom(result.getTestStep().getClass());
  }

  @Override
  protected List<SaveLogRQ> prepareLogs(TestStepResult result) {
    if (!result.isDiscarded()) {
      WsdlGroovyScriptTestStep step = ((WsdlGroovyScriptTestStep) result.getTestStep());
      String scriptResult = step.getPropertyValue("result");
      List<SaveLogRQ> logs = Arrays.asList(
          prepareEntity("INFO", "Executed script:"),
          prepareEntity("INFO", MarkdownUtils.asCode("groovy", step.getScript()))
      );

      if (scriptResult != null && !"null".equals(scriptResult)) {
        logs.add(prepareEntity("INFO", "With result:"));
        logs.add(prepareEntity("INFO", MarkdownUtils.asCode("groovy", scriptResult)));
      }
      Throwable scriptError = result.getError();
      if (scriptError != null) {
        StringWriter stackTraceWriter = new StringWriter();
        scriptError.printStackTrace(new PrintWriter(stackTraceWriter));
        logs.add(prepareEntity("ERROR", "Raised exception:"));
        logs.add(prepareEntity("INFO", MarkdownUtils.asCode("groovy", stackTraceWriter.toString())));
      }
      return logs;
    }
    return new ArrayList<>();
  }
}