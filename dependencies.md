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
| [Matcher for SQL Result Sets][25]            | [MIT][1]                          |
| [udf-debugging-java][27]                     | [MIT][1]                          |

## Runtime Dependencies

| Dependency                   | License                                                                                                      |
| ---------------------------- | ------------------------------------------------------------------------------------------------------------ |
| [JSON-P Default Provider][6] | [Eclipse Public License 2.0][7]; [GNU General Public License, version 2 with the GNU Classpath Exception][8] |

## Plugin Dependencies

| Dependency                                              | License                                        |
| ------------------------------------------------------- | ---------------------------------------------- |
| [SonarQube Scanner for Maven][32]                       | [GNU LGPL 3][33]                               |
| [Apache Maven Compiler Plugin][34]                      | [Apache License, Version 2.0][35]              |
| [Apache Maven Enforcer Plugin][36]                      | [Apache License, Version 2.0][35]              |
| [Maven Flatten Plugin][38]                              | [Apache Software Licenese][39]                 |
| [org.sonatype.ossindex.maven:ossindex-maven-plugin][40] | [ASL2][39]                                     |
| [Reproducible Build Maven Plugin][42]                   | [Apache 2.0][39]                               |
| [Maven Surefire Plugin][44]                             | [Apache License, Version 2.0][35]              |
| [Versions Maven Plugin][46]                             | [Apache License, Version 2.0][35]              |
| [Apache Maven Deploy Plugin][48]                        | [Apache License, Version 2.0][35]              |
| [Apache Maven GPG Plugin][50]                           | [Apache License, Version 2.0][35]              |
| [Apache Maven Source Plugin][52]                        | [Apache License, Version 2.0][35]              |
| [Project keeper maven plugin][54]                       | [The MIT License][55]                          |
| [Apache Maven JAR Plugin][56]                           | [Apache License, Version 2.0][35]              |
| [Apache Maven Javadoc Plugin][58]                       | [Apache License, Version 2.0][35]              |
| [Nexus Staging Maven Plugin][60]                        | [Eclipse Public License][61]                   |
| [Lombok Maven Plugin][62]                               | [The MIT License][1]                           |
| [JaCoCo :: Maven Plugin][64]                            | [Eclipse Public License 2.0][65]               |
| [error-code-crawler-maven-plugin][66]                   | [MIT][1]                                       |
| [Maven Clean Plugin][68]                                | [The Apache Software License, Version 2.0][39] |
| [Maven Resources Plugin][70]                            | [The Apache Software License, Version 2.0][39] |
| [Maven Install Plugin][72]                              | [The Apache Software License, Version 2.0][39] |
| [Maven Site Plugin 3][74]                               | [The Apache Software License, Version 2.0][39] |

[4]: https://github.com/exasol/error-reporting-java
[2]: https://github.com/exasol/parquet-io-java
[39]: http://www.apache.org/licenses/LICENSE-2.0.txt
[11]: https://projectlombok.org
[44]: https://maven.apache.org/surefire/maven-surefire-plugin/
[60]: http://www.sonatype.com/public-parent/nexus-maven-plugins/nexus-staging/nexus-staging-maven-plugin/
[68]: http://maven.apache.org/plugins/maven-clean-plugin/
[1]: https://opensource.org/licenses/MIT
[19]: https://github.com/mockito/mockito
[9]: https://github.com/exasol/performance-test-recorder-java
[46]: http://www.mojohaus.org/versions-maven-plugin/
[54]: https://github.com/exasol/project-keeper/
[14]: http://opensource.org/licenses/BSD-3-Clause
[34]: https://maven.apache.org/plugins/maven-compiler-plugin/
[65]: https://www.eclipse.org/legal/epl-2.0/
[48]: https://maven.apache.org/plugins/maven-deploy-plugin/
[61]: http://www.eclipse.org/legal/epl-v10.html
[33]: http://www.gnu.org/licenses/lgpl.txt
[64]: https://www.jacoco.org/jacoco/trunk/doc/maven.html
[20]: https://github.com/mockito/mockito/blob/main/LICENSE
[12]: https://projectlombok.org/LICENSE
[25]: https://github.com/exasol/hamcrest-resultset-matcher
[42]: http://zlika.github.io/reproducible-build-maven-plugin
[55]: https://github.com/exasol/project-keeper/blob/main/LICENSE
[7]: https://projects.eclipse.org/license/epl-2.0
[32]: http://sonarsource.github.io/sonar-scanner-maven/
[35]: https://www.apache.org/licenses/LICENSE-2.0.txt
[36]: https://maven.apache.org/enforcer/maven-enforcer-plugin/
[62]: https://awhitford.github.com/lombok.maven/lombok-maven-plugin/
[0]: https://github.com/exasol/virtual-schema-common-document/
[27]: https://github.com/exasol/udf-debugging-java/
[16]: https://www.eclipse.org/legal/epl-v20.html
[72]: http://maven.apache.org/plugins/maven-install-plugin/
[15]: https://junit.org/junit5/
[40]: https://sonatype.github.io/ossindex-maven/maven-plugin/
[50]: https://maven.apache.org/plugins/maven-gpg-plugin/
[38]: https://www.mojohaus.org/flatten-maven-plugin/flatten-maven-plugin
[6]: https://github.com/eclipse-ee4j/jsonp
[52]: https://maven.apache.org/plugins/maven-source-plugin/
[8]: https://projects.eclipse.org/license/secondary-gpl-2.0-cp
[13]: http://hamcrest.org/JavaHamcrest/
[74]: http://maven.apache.org/plugins/maven-site-plugin/
[70]: http://maven.apache.org/plugins/maven-resources-plugin/
[58]: https://maven.apache.org/plugins/maven-javadoc-plugin/
[66]: https://github.com/exasol/error-code-crawler-maven-plugin
[56]: https://maven.apache.org/plugins/maven-jar-plugin/
