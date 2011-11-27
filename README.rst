sbtsrc
======

The missing plugin for sbt. Adds the *src* command for creating source directories. This optionally includes resource directories and managed directories.


Installing sbtsrc
-----------------

- You probably want to install this plugin as a global plugin, but of course you could also go for a local one. For details about sbt plugins see the `sbt documentation`_:

  - Global plugin are defined in a *plugins.sbt* file in the *~/.sbt/plugins/* directory
  - Local plugins are defined in a *plugins.sbt* file in the *project/* folder of your project

- Just add the below lines to your plugin definition, paying attention to the blank line:

::
  
  resolvers += Resolver.url("heikoseeberger", new java.net.URL("http://hseeberger.github.com/releases"))(Resolver.ivyStylePatterns)
  
  addSbtPlugin("name.heikoseeberger.sbtsrc" % "sbtsrc" % "1.1.0")


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


Configuring sbtsrc
------------------

There are two ways to configure the creation of the source directories. First you can use command options and take advantage of sbt's excellent auto complete feature. Second you can use sbt settings to persist your configuration in *build.sbt* or the like. Please notice that there are defaults for the sbt settings and command options will override sbt settings.

Available command options are:

- *with-managed*: *true* or *false* => Create managed source directories?
- *with-resources* : *true* or *false* => Create resource directories?

Example (within sbt session):

::

  > src with-managed=true with-resources=false

Available settings are:

- *SrcKeys.withManaged*: *true* or *false* (default) => Create managed source directories?
- *SrcKeys.withResources*: *true* (default) or *false* => Create resource directories?

Example (within sbt session):

::

  > set SrcKeys.withResources := false


Mailing list
------------

Please use the `sbt mailing list`_ and prefix the subject with "[sbtsrc]".


Contribution policy
-------------------

Contributions via GitHub pull requests are gladly accepted from their original author. Along with any pull requests, please state that the contribution is your original work and that you license the work to the groll project under the project's open source license.


License
-------

This code is open source software licensed under the `Apache 2.0 License`_. Feel free to use it accordingly.

.. _`sbt documentation`: https://github.com/harrah/xsbt/wiki/Plugins
.. _`sbt mailing list`: mailto:simple-build-tool@googlegroups.com
.. _`Apache 2.0 License`: http://www.apache.org/licenses/LICENSE-2.0.html
