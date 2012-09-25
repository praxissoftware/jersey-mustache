# Jersey-Mustache
## Jersey-Mustache Integration

I like to use [mustache](https://github.com/mustache) as a templating language.  I also like to use [jersey](http://jersey.java.net) as a server-side controller.  Unfortunately, jersey doesn't come integrated with support for view rendering via mustache.

### How to Integrate Using Guice
If you're using Guice, then you have flexibility in how the MustacheViewProcessor works.

In the simplest fashion, just bind the processor as such:

```
bind(MustacheViewProcessor.class);
```

You can configure how the processor works, too, by overriding the defaults using more Guice bindings:

```
bind(MustacheViewProcessor.class);
bind(Pattern.class).annotatedWith(Names.named(MustacheViewProcessor.GUICE_BINDING_TEMPLATE_PATTERN)).to(Pattern.compile("mustache/.*");
bind(String.class).annotatedWith(Names.named(MustacheViewProcessor.GUICE_BINDING_RESOURCE_BASE_PATH)).to("templates");
```

### How to Integrate Without Guice
Jersey should just pick up the MustacheViewProcessor when class scanning for its context.
