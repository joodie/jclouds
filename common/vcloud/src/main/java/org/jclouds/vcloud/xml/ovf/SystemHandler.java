/**
 *
 * Copyright (C) 2010 Cloud Conscious, LLC. <info@cloudconscious.com>
 *
 * ====================================================================
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
 * ====================================================================
 */

package org.jclouds.vcloud.xml.ovf;

import java.util.Set;

import org.jclouds.http.functions.ParseSax;
import org.jclouds.vcloud.domain.ovf.System;
import org.xml.sax.Attributes;

import com.google.common.collect.Sets;

/**
 * @author Adrian Cole
 */
public class SystemHandler extends ParseSax.HandlerWithResult<System> {
   private StringBuilder currentText = new StringBuilder();

   private String elementName;
   private int instanceID;
   private String virtualSystemIdentifier;
   private Set<String> virtualSystemTypes = Sets.newLinkedHashSet();

   public System getResult() {
      System system = new org.jclouds.vcloud.domain.ovf.System(instanceID, elementName, virtualSystemIdentifier,
               virtualSystemTypes);
      this.elementName = null;
      this.instanceID = -1;
      this.virtualSystemIdentifier = null;
      this.virtualSystemTypes = null;
      return system;
   }

   public void startElement(String uri, String localName, String qName, Attributes attributes) {
      // no op
   }

   @Override
   public void endElement(String uri, String localName, String qName) {
      if (qName.endsWith("ElementName")) {
         this.elementName = currentText.toString().trim();
      } else if (qName.endsWith("InstanceID")) {
         this.instanceID = Integer.parseInt(currentText.toString().trim());
      } else if (qName.endsWith("VirtualSystemIdentifier")) {
         this.virtualSystemIdentifier = currentText.toString().trim();
      } else if (qName.endsWith("VirtualSystemType")) {
         this.virtualSystemTypes.add(currentText.toString().trim());
      }
      currentText = new StringBuilder();
   }

   public void characters(char ch[], int start, int length) {
      currentText.append(ch, start, length);
   }
}
