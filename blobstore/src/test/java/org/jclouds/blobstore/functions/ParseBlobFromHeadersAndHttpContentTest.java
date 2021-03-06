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

package org.jclouds.blobstore.functions;

import static org.easymock.EasyMock.expect;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.testng.Assert.assertEquals;

import java.util.Collections;

import javax.inject.Provider;
import javax.ws.rs.core.MediaType;

import org.jclouds.blobstore.config.BlobStoreObjectModule;
import org.jclouds.blobstore.domain.Blob;
import org.jclouds.blobstore.domain.MutableBlobMetadata;
import org.jclouds.blobstore.domain.internal.MutableBlobMetadataImpl;
import org.jclouds.http.HttpException;
import org.jclouds.http.HttpResponse;
import org.jclouds.io.Payloads;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.google.common.collect.ImmutableMultimap;
import com.google.inject.Guice;

/**
 * @author Adrian Cole
 */
public class ParseBlobFromHeadersAndHttpContentTest {

   @BeforeTest
   void setUp() {
      blobProvider = Guice.createInjector(new BlobStoreObjectModule()).getInstance(Blob.Factory.class);
   }

   @Test(expectedExceptions = NullPointerException.class)
   public void testCall() throws HttpException {
      ParseSystemAndUserMetadataFromHeaders metadataParser = createMock(ParseSystemAndUserMetadataFromHeaders.class);
      ParseBlobFromHeadersAndHttpContent callable = new ParseBlobFromHeadersAndHttpContent(metadataParser, blobProvider);
      HttpResponse response = new HttpResponse(200, null, null, ImmutableMultimap.of("Content-Range", (String) null));
      callable.apply(response);
   }

   private Blob.Factory blobProvider;
   private Provider<MutableBlobMetadata> blobMetadataProvider = new Provider<MutableBlobMetadata>() {

      public MutableBlobMetadata get() {
         return new MutableBlobMetadataImpl();
      }

   };

   @Test
   public void testParseContentLengthWhenContentRangeSet() throws HttpException {
      ParseSystemAndUserMetadataFromHeaders metadataParser = createMock(ParseSystemAndUserMetadataFromHeaders.class);
      ParseBlobFromHeadersAndHttpContent callable = new ParseBlobFromHeadersAndHttpContent(metadataParser, blobProvider);

      HttpResponse response = new HttpResponse(200, "ok", Payloads.newStringPayload(""), ImmutableMultimap.of(
            "Content-Range", "0-10485759/20232760"));
      response.getPayload().getContentMetadata().setContentType(MediaType.APPLICATION_JSON);
      response.getPayload().getContentMetadata().setContentLength(10485760l);

      MutableBlobMetadata meta = blobMetadataProvider.get();
      expect(metadataParser.apply(response)).andReturn(meta);
      replay(metadataParser);

      Blob object = callable.apply(response);
      assertEquals(object.getPayload().getContentMetadata().getContentLength(), new Long(10485760));
      assertEquals(object.getAllHeaders().get("Content-Range"), Collections.singletonList("0-10485759/20232760"));

   }

}
