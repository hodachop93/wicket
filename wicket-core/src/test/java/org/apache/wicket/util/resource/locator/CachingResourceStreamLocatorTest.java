/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.wicket.util.resource.locator;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.net.URL;

import org.apache.wicket.util.resource.FileResourceStream;
import org.apache.wicket.util.resource.UrlResourceStream;
import org.junit.Test;

/**
 * <a href="https://issues.apache.org/jira/browse/WICKET-3511">WICKET-3511</a>
 * 
 * @author mgrigorov
 */
public class CachingResourceStreamLocatorTest
{

	/**
	 * Tests NullResourceStreamReference
	 */
	@Test
	public void testNotExistingResource()
	{

		IResourceStreamLocator resourceStreamLocator = mock(IResourceStreamLocator.class);

		CachingResourceStreamLocator cachingLocator = new CachingResourceStreamLocator(
			resourceStreamLocator);

		cachingLocator.locate(String.class, "path");
		cachingLocator.locate(String.class, "path");

		// there is no resource with that Key so expect two calls to the delegate
		verify(resourceStreamLocator, times(2)).locate(String.class, "path");
	}

	/**
	 * Tests FileResourceStreamReference
	 */
	@Test
	public void testFileResource()
	{
		IResourceStreamLocator resourceStreamLocator = mock(IResourceStreamLocator.class);

		FileResourceStream frs = new FileResourceStream(new File("."));

		when(
			resourceStreamLocator.locate(String.class, "path", "style", "variation", null,
				"extension", true)).thenReturn(frs);

		CachingResourceStreamLocator cachingLocator = new CachingResourceStreamLocator(
			resourceStreamLocator);

		cachingLocator.locate(String.class, "path", "style", "variation", null, "extension", true);
		cachingLocator.locate(String.class, "path", "style", "variation", null, "extension", true);

		// there is a file resource with that Key so expect just one call to the delegate
		verify(resourceStreamLocator, times(1)).locate(String.class, "path", "style", "variation",
			null, "extension", true);
	}

	/**
	 * Tests UrlResourceStreamReference
	 * 
	 * @throws Exception
	 */
	@Test
	public void testUrlResource() throws Exception
	{
		IResourceStreamLocator resourceStreamLocator = mock(IResourceStreamLocator.class);

		UrlResourceStream urs = new UrlResourceStream(new URL("file:///"));

		when(resourceStreamLocator.locate(String.class, "path")).thenReturn(urs);

		CachingResourceStreamLocator cachingLocator = new CachingResourceStreamLocator(
			resourceStreamLocator);

		cachingLocator.locate(String.class, "path");
		cachingLocator.locate(String.class, "path");

		// there is a url resource with that Key so expect just one call to the delegate
		verify(resourceStreamLocator, times(1)).locate(String.class, "path");
	}
}
