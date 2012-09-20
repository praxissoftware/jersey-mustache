/**
 * Copyright (C) 2012 Jason Rose <jasoncrose@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.praxissoftware.jersey.mustache;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.regex.Pattern;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.github.mustachejava.Mustache;
import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableMap;
import com.sun.jersey.api.view.Viewable;

public class MustacheViewProcessorTest {

  private MustacheViewProcessor service;

  @Test
  public void render() throws IOException {
    service = new MustacheViewProcessor("templates", Pattern.compile(".*"));
    final Mustache mustache = service.resolve("exists.mustache.txt");
    final ByteArrayOutputStream out = new ByteArrayOutputStream();
    final Viewable viewable = new Viewable("exists", ImmutableMap.of("msg", "world"));
    service.writeTo(mustache, viewable, out);
    Assert.assertEquals("Hello, world!", out.toString(Charsets.UTF_8.name()));
  }

  @Before
  public void setUp() {
    service = new MustacheViewProcessor();
  }

  @Test
  public void testDoesntResolves() {
    service = new MustacheViewProcessor("templates", Pattern.compile(".*"));
    Assert.assertNull(service.resolve("ponies.mustache.txt"));
  }

  @Test
  public void testRegexMatches() {
    service = new MustacheViewProcessor("templates", Pattern.compile("^existss?[.]mustache[.]txt$"));
    Assert.assertNotNull(service.resolve("exists.mustache.txt"));

    service = new MustacheViewProcessor("templates", Pattern.compile("^existsssss?[.]mustache[.]txt$"));
    Assert.assertNull(service.resolve("exists.mustache.txt"));
  }

  @Test
  public void testResolves() {
    service = new MustacheViewProcessor("templates", Pattern.compile(".*"));
    Assert.assertNotNull(service.resolve("exists.mustache.txt"));
  }
}
