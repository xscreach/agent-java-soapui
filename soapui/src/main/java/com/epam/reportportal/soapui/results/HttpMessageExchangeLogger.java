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

import static rp.com.google.common.base.Strings.isNullOrEmpty;

import java.util.LinkedList;
import java.util.List;

import com.epam.ta.reportportal.ws.model.log.SaveLogRQ;
import com.eviware.soapui.impl.wsdl.submit.HttpMessageExchange;
import com.eviware.soapui.support.types.StringToStringsMap;

/**
 * @author Andrei Varabyeu
 */
public class HttpMessageExchangeLogger extends ResultLogger<HttpMessageExchange> {

  public HttpMessageExchangeLogger() {
    super(HttpMessageExchange.class);
  }

  @Override
  protected List<SaveLogRQ> prepareLogs(HttpMessageExchange result) {
    List<SaveLogRQ> retVal = new LinkedList<>();
    if (!result.isDiscarded()) {
      retVal.add(prepareEntity("REQUEST", result.getRequestHeaders(), result.getRequestContent()));
      if (result.hasResponse()) {
        retVal.add(prepareEntity("RESPONSE", result.getResponseHeaders(), result.getResponseContent()));
      }
    }
    return retVal;
  }

  private SaveLogRQ prepareEntity(String prefix, StringToStringsMap headers, String body) {
    StringBuilder rqLog = new StringBuilder();
    if (headers != null) {
      rqLog
          .append(prefix).append("\n")
          .append("HEADERS:\n")
          .append(headers);
    }

    if (!isNullOrEmpty(body)) {
      rqLog.append("BODY:\n")
          .append(body);
    }
    return prepareEntity("INFO", rqLog.toString());
  }
}