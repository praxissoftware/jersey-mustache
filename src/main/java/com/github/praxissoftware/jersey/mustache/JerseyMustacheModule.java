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

import java.util.regex.Pattern;

import javax.inject.Singleton;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.MustacheFactory;
import com.google.inject.AbstractModule;
import com.google.inject.name.Names;

/**
 * This module provides the defaults for configurable classes in the Jersey-Mustache package.
 * @author Jason Rose
 * 
 */
public class JerseyMustacheModule extends AbstractModule {
  private final MustacheFactory factory;
  private final Pattern regex;

  private JerseyMustacheModule(final MustacheFactory factory, final Pattern regex) {
    this.factory = factory;
    this.regex = regex;
  }

  @Override
  protected void configure() {
    bind(MustacheFactory.class).annotatedWith(Names.named("praxis.mustache.factory")).toInstance(factory);
    bind(Pattern.class).annotatedWith(Names.named("praxis.mustache.regex")).toInstance(regex);
    bind(MustacheViewProcessor.class).in(Singleton.class);
  }

  public static class Builder {
    private MustacheFactory factory;
    private Pattern regex;

    public Builder() {
      this.factory = new DefaultMustacheFactory("templates");
      this.regex = Pattern.compile(".*[.]mustache.*");
    }

    public JerseyMustacheModule build() {
      return new JerseyMustacheModule(factory, regex);
    }
    
    public Builder factory(final MustacheFactory factory) {
      this.factory = factory;
      return this;
    }
    
    public Builder regex(final Pattern regex) {
      this.regex = regex;
      return this;
    }
  }
}
