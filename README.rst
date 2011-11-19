sbtsrc
======

The missing plugin for sbt. Adds the *src* command for creating source directories.

Installing sbtsrc
-----------------

- To install this plugin, either use a local or the global plugin definition (for details about sbt plugins see the `sbt documentation`_):

  - Local plugins are defined in a *plugins.sbt* file in the *project/* folder of your project
  - Global plugin are defined in a *plugins.sbt* file in the *~/.sbt/plugins/* directory

- Just add the below lines to your plugin definition, paying attention to the blank line:

::
  
  resolvers += Resolver.url("heikoseeberger", new java.net.URL("http://hseeberger.github.com/releases"))(Resolver.ivyStylePatterns)
  
  addSbtPlugin("name.heikoseeberger.sbtsrc" % "sbtsrc" % "1.0.0")

Using sbtsrc
------------

In order to create source directories for your project, just execute the *src* command which is provided by this plugin. Don't worry, if some or all source directories already exist. sbtsrc will not touch these.

::

  > src
  [info] About to create source directories for you.
  [info] Successfully created source directories for 1 project(s):
  [info] /Users/heiko/tmp/test/src/main/scala was created.
  [info] /Users/heiko/tmp/test/src/main/java was created.
  [info] /Users/heiko/tmp/test/src/test/scala was created.
  [info] /Users/heiko/tmp/test/src/test/java was created.
  </code></pre>

Contribution policy
-------------------

Contributions via GitHub pull requests are gladly accepted from their original author. Along with any pull requests, please state that the contribution is your original work and that you license the work to the groll project under the project's open source license.

License
-------

This code is open source software licensed under the `Apache 2.0 License`_. Feel free to use it accordingly.

.. _`sbt documentation`: https://github.com/harrah/xsbt/wiki/Plugins
.. _`Apache 2.0 License`: http://www.apache.org/licenses/LICENSE-2.0.html
