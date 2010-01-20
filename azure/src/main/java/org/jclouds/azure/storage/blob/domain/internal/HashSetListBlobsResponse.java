/**
 *
 * Copyright (C) 2009 Cloud Conscious, LLC. <info@cloudconscious.com>
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
package org.jclouds.azure.storage.blob.domain.internal;

import java.net.URI;
import java.util.Set;

import org.jclouds.azure.storage.blob.domain.BlobProperties;
import org.jclouds.azure.storage.blob.domain.ListBlobsResponse;
import org.jclouds.azure.storage.domain.internal.BoundedHashSet;

import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;

/**
 * 
 * @author Adrian Cole
 * 
 */
public class HashSetListBlobsResponse extends BoundedHashSet<BlobProperties> implements
         ListBlobsResponse {
   /** The serialVersionUID */
   private static final long serialVersionUID = -4475709781001190244L;

   protected final String delimiter;
   protected final Set<String> blobPrefixes = Sets.newHashSet();

   public HashSetListBlobsResponse(Iterable<BlobProperties> contents, URI url, String prefix,
            String marker, Integer maxResults, String nextMarker, String delimiter,
            Iterable<String> blobPrefixes) {
      super(contents, url, prefix, marker, maxResults, nextMarker);
      this.delimiter = delimiter;
      Iterables.addAll(this.blobPrefixes, blobPrefixes);
   }

   public String getDelimiter() {
      return delimiter;
   }

   public Set<String> getBlobPrefixes() {
      return blobPrefixes;
   }
}