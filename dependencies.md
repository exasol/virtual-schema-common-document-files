<!-- @formatter:off -->
# Dependencies

## Compile Dependencies

| Dependency                                   | License                                                                                                      |
| -------------------------------------------- | ------------------------------------------------------------------------------------------------------------ |
| [Common Virtual Schema for document data][0] | [MIT][1]                                                                                                     |
| [Parquet for Java][2]                        | [MIT][1]                                                                                                     |
| [error-reporting-java][4]                    | [MIT][1]                                                                                                     |
| [Jakarta JSON Processing API][6]             | [Eclipse Public License 2.0][7]; [GNU General Public License, version 2 with the GNU Classpath Exception][8] |
| [Performance Test Recorder Java][9]          | [MIT][1]                                                                                                     |
| [Project Lombok][11]                         | [The MIT License][12]                                                                                        |

## Test Dependencies

| Dependency                                   | License                           |
| -------------------------------------------- | --------------------------------- |
| [Hamcrest][13]                               | [BSD License 3][14]               |
| [JUnit Jupiter Engine][15]                   | [Eclipse Public License v2.0][16] |
| [JUnit Jupiter Params][15]                   | [Eclipse Public License v2.0][16] |
| [mockito-core][19]                           | [The MIT License][20]             |
| [mockito-junit-jupiter][19]                  | [The MIT License][20]             |
| [Common Virtual Schema for document data][0] | [MIT][1]                          |
| [JaCoCo :: Agent][25]                        | [Eclipse Public License 2.0][26]  |
| [JaCoCo :: Core][25]                         | [Eclipse Public License 2.0][26]  |
| [Matcher for SQL Result Sets][29]            | [MIT][1]                          |
| [udf-debugging-java][31]                     | [MIT][1]                          |

## Runtime Dependencies

| Dependency                   | License                                                                                                      |
| ---------------------------- | ------------------------------------------------------------------------------------------------------------ |
| [JSON-P Default Provider][6] | [Eclipse Public License 2.0][7]; [GNU General Public License, version 2 with the GNU Classpath Exception][8] |

## Plugin Dependencies

| Dependency                                              | License                                        |
| ------------------------------------------------------- | ---------------------------------------------- |
| [Maven Surefire Plugin][36]                             | [Apache License, Version 2.0][37]              |
| [Maven Failsafe Plugin][38]                             | [Apache License, Version 2.0][37]              |
| [JaCoCo :: Maven Plugin][40]                            | [Eclipse Public License 2.0][26]               |
| [Maven Jar Plugin][42]                                  | [The Apache Software License, Version 2.0][43] |
| [Apache Maven Source Plugin][44]                        | [Apache License, Version 2.0][37]              |
| [Apache Maven Compiler Plugin][46]                      | [Apache License, Version 2.0][37]              |
| [Maven Dependency Plugin][48]                           | [The Apache Software License, Version 2.0][43] |
| [Versions Maven Plugin][50]                             | [Apache License, Version 2.0][37]              |
| [org.sonatype.ossindex.maven:ossindex-maven-plugin][52] | [ASL2][43]                                     |
| [Apache Maven Enforcer Plugin][54]                      | [Apache License, Version 2.0][37]              |
| [Project keeper maven plugin][56]                       | [MIT][1]                                       |
| [Apache Maven Javadoc Plugin][58]                       | [Apache License, Version 2.0][37]              |
| [Apache Maven GPG Plugin][60]                           | [Apache License, Version 2.0][43]              |
| [Maven Deploy Plugin][62]                               | [The Apache Software License, Version 2.0][43] |
| [Nexus Staging Maven Plugin][64]                        | [Eclipse Public License][65]                   |
| [error-code-crawler-maven-plugin][66]                   | [MIT][1]                                       |
| [Reproducible Build Maven Plugin][68]                   | [Apache 2.0][43]                               |
| [Lombok Maven Plugin][70]                               | [The MIT License][1]                           |
| [Maven Clean Plugin][72]                                | [The Apache Software License, Version 2.0][43] |
| [Maven Resources Plugin][74]                            | [The Apache Software License, Version 2.0][43] |
| [Maven Install Plugin][76]                              | [The Apache Software License, Version 2.0][43] |
| [Maven Site Plugin 3][78]                               | [The Apache Software License, Version 2.0][43] |

[0]: https://github.com/exasol/virtual-schema-common-document
[25]: https://www.eclemma.org/jacoco/index.html
[56]: https://github.com/exasol/project-keeper-maven-plugin
[4]: https://github.com/exasol/error-reporting-java
[2]: https://github.com/exasol/parquet-io-java
[43]: http://www.apache.org/licenses/LICENSE-2.0.txt
[11]: https://projectlombok.org
[36]: https://maven.apache.org/surefire/maven-surefire-plugin/
[64]: http://www.sonatype.com/public-parent/nexus-maven-plugins/nexus-staging/nexus-staging-maven-plugin/
[72]: http://maven.apache.org/plugins/maven-clean-plugin/
[1]: https://opensource.org/licenses/MIT
[19]: https://github.com/mockito/mockito
[38]: https://maven.apache.org/surefire/maven-failsafe-plugin/
[9]: https://github.com/exasol/performance-test-recorder-java
[48]: http://maven.apache.org/plugins/maven-dependency-plugin/
[50]: http://www.mojohaus.org/versions-maven-plugin/
[70]: http://anthonywhitford.com/lombok.maven/lombok-maven-plugin/
[14]: http://opensource.org/licenses/BSD-3-Clause
[46]: https://maven.apache.org/plugins/maven-compiler-plugin/
[60]: http://maven.apache.org/plugins/maven-gpg-plugin/
[26]: https://www.eclipse.org/legal/epl-2.0/
[65]: http://www.eclipse.org/legal/epl-v10.html
[40]: https://www.jacoco.org/jacoco/trunk/doc/maven.html
[20]: https://github.com/mockito/mockito/blob/main/LICENSE
[12]: https://projectlombok.org/LICENSE
[29]: https://github.com/exasol/hamcrest-resultset-matcher
[68]: http://zlika.github.io/reproducible-build-maven-plugin
[42]: http://maven.apache.org/plugins/maven-jar-plugin/
[7]: https://projects.eclipse.org/license/epl-2.0
[37]: https://www.apache.org/licenses/LICENSE-2.0.txt
[54]: https://maven.apache.org/enforcer/maven-enforcer-plugin/
[16]: https://www.eclipse.org/legal/epl-v20.html
[76]: http://maven.apache.org/plugins/maven-install-plugin/
[15]: https://junit.org/junit5/
[52]: https://sonatype.github.io/ossindex-maven/maven-plugin/
[6]: https://github.com/eclipse-ee4j/jsonp
[31]: https://github.com/exasol/udf-debugging-java
[44]: https://maven.apache.org/plugins/maven-source-plugin/
[8]: https://projects.eclipse.org/license/secondary-gpl-2.0-cp
[13]: http://hamcrest.org/JavaHamcrest/
[62]: http://maven.apache.org/plugins/maven-deploy-plugin/
[78]: http://maven.apache.org/plugins/maven-site-plugin/
[74]: http://maven.apache.org/plugins/maven-resources-plugin/
[58]: https://maven.apache.org/plugins/maven-javadoc-plugin/
[66]: https://github.com/exasol/error-code-crawler-maven-plugin
