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

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.ws.rs.ext.Provider;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import com.google.common.util.concurrent.UncheckedExecutionException;
import com.sun.jersey.api.view.Viewable;
import com.sun.jersey.spi.template.ViewProcessor;

/**
 * This class is an extension of the {@link ViewProcessor} API that provides support for Mustache templates.
 * @author Jason Rose
 */
@Provider
@Singleton
public class MustacheViewProcessor implements ViewProcessor<Mustache> {

  /** The Guice {@link Named} property to bind for the regex that any Mustache template must match. */
  public static final String GUICE_BINDING_TEMPLATE_PATTERN = "mustacheViewProcessor.templatePattern";
  /** The Guice {@link Named} property to bind for the root classloader resource path for template lookups. */
  public static final String GUICE_BINDING_RESOURCE_BASE_PATH = "mustacheViewProcessor.resourceBasePath";
  private final MustacheFactory factory;
  private final Pattern templatePattern;

  public MustacheViewProcessor() {
    this("templates", Pattern.compile(".*[.]mustache.*"));
  }

  /**
   * Generate a processor rooted at the specified path and matching the specified regex.
   * @param resourceBasePath The root of the classpath to begin searching at.
   * @param templatePattern The regex that any template must match.
   */
  @Inject
  public MustacheViewProcessor(@Named(GUICE_BINDING_RESOURCE_BASE_PATH) final String resourceBasePath, @Named(GUICE_BINDING_TEMPLATE_PATTERN) final Pattern templatePattern) {
    factory = new DefaultMustacheFactory(resourceBasePath);
    this.templatePattern = templatePattern;
  }

  @Override
  public Mustache resolve(final String name) {
    // Filter based on specified regex, so we don't try to send non-mustache templates.
    final Matcher matcher = templatePattern.matcher(name);
    if( !matcher.matches() ) {
      return null;
    }

    // Try to load from the classloader.
    try {
      return factory.compile(name);
    } catch( final UncheckedExecutionException uee ) {
      return null;
    }
  }

  @Override
  public void writeTo(final Mustache template, final Viewable viewable, final OutputStream out) throws IOException {
    template.execute(new OutputStreamWriter(out), viewable.getModel()).flush();
  }

}