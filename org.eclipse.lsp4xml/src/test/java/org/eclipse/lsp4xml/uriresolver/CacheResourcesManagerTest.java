/*******************************************************************************
* Copyright (c) 2018 Red Hat Inc. and others.
* All rights reserved. This program and the accompanying materials
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v20.html
*
* Contributors:
*     Red Hat Inc. - initial API and implementation
*******************************************************************************/

package org.eclipse.lsp4xml.uriresolver;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import com.google.common.cache.CacheBuilder;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class CacheResourcesManagerTest {

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Test
	public void testCanUseCache() {
		testCanUseCache(true);
		testCanUseCache(false);
	}

	private void testCanUseCache(boolean useCacheEnabled) {
		CacheResourcesManager cacheResourcesManager = new CacheResourcesManager();
		cacheResourcesManager.setUseCache(useCacheEnabled);
		assertEquals(useCacheEnabled, cacheResourcesManager.canUseCache("http://foo"));
		assertEquals(useCacheEnabled, cacheResourcesManager.canUseCache("ftp://foo"));
		assertEquals(useCacheEnabled, cacheResourcesManager.canUseCache("https://foo"));
		assertFalse(cacheResourcesManager.canUseCache("file:///foo"));
	}

	@Test
	public void testUnavailableCache() {
		CacheResourcesManager cacheResourcesManager = new CacheResourcesManager();
		cacheResourcesManager.setUseCache(true);
		try {
			thrown.expect(CacheResourceDownloadingException.class);
			cacheResourcesManager.getResource("http://bad");
		} catch (IOException e) {
			
		}

		try {
			thrown.expect(CacheResourceDownloadedException.class);
			assertNull(cacheResourcesManager.getResource("http://bad"));
		} catch (IOException e1) {

		}
	}

	@Test
	
	public void testAvailableCache() {
		CacheResourcesManager cacheResourcesManager = new CacheResourcesManager(CacheBuilder.newBuilder()
														.expireAfterWrite(10, TimeUnit.SECONDS)
														.maximumSize(100)
														.build());
		cacheResourcesManager.setUseCache(true);
		try {
			//thrown.expect(CacheResourceDownloadedException.class);
			//cacheResourcesManager.getResource("http://bad");
		
			//thrown.expect(CacheResourceDownloadedException.class);
			assertNull(cacheResourcesManager.getResource("http://bad"));
			
			TimeUnit.SECONDS.sleep(10);
			assertNull(cacheResourcesManager.getResource("http://bad"));
		} catch (Exception IOException) {
			fail();
		}
			
		
		
	}

}