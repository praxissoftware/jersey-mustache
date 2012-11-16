# Jersey-Mustache
## Jersey-Mustache Integration

I like to use [mustache](https://github.com/mustache) as a templating language.  I also like to use [jersey](http://jersey.java.net) as a server-side controller.  Unfortunately, jersey doesn't come integrated with support for view rendering via mustache.

### How to Integrate Using Guice
If you're using Guice, then you have flexibility in how the MustacheViewProcessor works.

In the simplest fashion, just install the module while creating your injector:

```
new JerseyMustacheModule.Builder().build()
```

You can configure how the processor works, too, by overriding the defaults using the builder pattern on the module:

```
new JerseyMustacheModule.Builder().factory(myFancyFactory).regex(mySuperRegex).build()
```

### How to Integrate Without Guice
Jersey should just pick up the MustacheViewProcessor when class scanning for its context.
