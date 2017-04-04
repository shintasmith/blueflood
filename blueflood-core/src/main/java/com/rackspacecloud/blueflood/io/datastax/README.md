# NOTE

## Datastax driver and Netty Compatibility

The Datastax driver uses Netty for communicating with the Cassandra nodes. Blueflood also uses Netty for the Http layer (reading/writing requests/responses). 

In the past, to resolve conflict in versions, we chose to use Datastax driver with classifier 'shaded'. This essentially shades the Netty classes used by Datastax driver to have a different package name. Datastax doc clearly indicated that if the 'shaded' jar is used, then Netty can not be configured to use native Epoll, which has some performance gains, according to documentation.

Therefore, in order for us leverage native Epoll, we deliberately choose the non-shaded jar of Datastax driver. But we must do so with care. We have to ensure that the Datastax driver for Cassandra is compatible with ours, i.e.: has the same major.minor version. This is the compatiblity matrix:

Cassandra Driver | Built w/ Netty | Blueflood Netty
Version          | Version        | Version
-----------------|----------------|----------------
3.1.2            | 4.0.37.FINAL   | 4.0.40.FINAL
3.2.0            | 4.0.44.FINAL   | 4.0.44.FINAL

To see which Netty version the Cassandra Driver is built with, you will have to checkout the github repo:
   git@github.com:datastax/java-driver

Then checkout the branch for that version and examine the pom.xml. For example, to see which Netty is used with the Cassandra driver version 3.2.0, clone the above github repo, issue "git checkout 3.2.0", then edit the pom.xml file.

