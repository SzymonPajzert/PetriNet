# PetriNet [![Build Status](https://travis-ci.com/SzymonPajzert/PetriNet.svg?token=sifZqAUGMfhp9sseTbjy&branch=master)](https://travis-ci.com/SzymonPajzert/PetriNet)

## Resources to read
* Od prof. Sroki:
  * http://sysbio3.fhms.surrey.ac.uk/qsspn/
  * http://bioinformatics.oxfordjournals.org/content/suppl/2013/09/23/btt552.DC1/QSSPN_SupplementaryText.pdf
* Found on my own:
  * http://edoc.sub.uni-hamburg.de/haw/volltexte/2013/1921/pdf/bsc_uhlig_m_web.pdf
  * https://github.com/matiasvinjevoll/cpnscalasimulator
  * https://github.com/Primetalk/SynapseGrid
  * http://ceur-ws.org/Vol-851/paper16.pdf


## How to use

Add the plugin in project/plugins.sbt:
```scala
addSbtPlugin("org.scoverage" % "sbt-scoverage" % "1.5.0")
```

Run the tests with enabled coverage:
```
$ sbt clean coverage test
```
or if you have integration tests as well
```
$ sbt clean coverage it:test
```

To enable coverage directly in your build, use:
```
coverageEnabled := true
```

To generate the coverage reports run
```
$ sbt coverageReport
```

Coverage reports will be in `target/scoverage-report`. There are HTML and XML reports.
