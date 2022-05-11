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
| [SonarQube Scanner for Maven][36]                       | [GNU LGPL 3][37]                               |
| [Apache Maven Compiler Plugin][38]                      | [Apache License, Version 2.0][39]              |
| [Apache Maven Enforcer Plugin][40]                      | [Apache License, Version 2.0][39]              |
| [Maven Flatten Plugin][42]                              | [Apache Software Licenese][43]                 |
| [org.sonatype.ossindex.maven:ossindex-maven-plugin][44] | [ASL2][43]                                     |
| [Reproducible Build Maven Plugin][46]                   | [Apache 2.0][43]                               |
| [Maven Surefire Plugin][48]                             | [Apache License, Version 2.0][39]              |
| [Maven Dependency Plugin][50]                           | [The Apache Software License, Version 2.0][43] |
| [Versions Maven Plugin][52]                             | [Apache License, Version 2.0][39]              |
| [Apache Maven Deploy Plugin][54]                        | [Apache License, Version 2.0][39]              |
| [Apache Maven GPG Plugin][56]                           | [Apache License, Version 2.0][39]              |
| [Apache Maven JAR Plugin][58]                           | [Apache License, Version 2.0][39]              |
| [Apache Maven Source Plugin][60]                        | [Apache License, Version 2.0][39]              |
| [Project keeper maven plugin][62]                       | [The MIT License][63]                          |
| [Apache Maven Javadoc Plugin][64]                       | [Apache License, Version 2.0][39]              |
| [Nexus Staging Maven Plugin][66]                        | [Eclipse Public License][67]                   |
| [Lombok Maven Plugin][68]                               | [The MIT License][1]                           |
| [Maven Failsafe Plugin][70]                             | [Apache License, Version 2.0][39]              |
| [JaCoCo :: Maven Plugin][72]                            | [Eclipse Public License 2.0][26]               |
| [error-code-crawler-maven-plugin][74]                   | [MIT][1]                                       |
| [Maven Clean Plugin][76]                                | [The Apache Software License, Version 2.0][43] |
| [Maven Resources Plugin][78]                            | [The Apache Software License, Version 2.0][43] |
| [Maven Install Plugin][80]                              | [The Apache Software License, Version 2.0][43] |
| [Maven Site Plugin 3][82]                               | [The Apache Software License, Version 2.0][43] |

[0]: https://github.com/exasol/virtual-schema-common-document
[25]: https://www.eclemma.org/jacoco/index.html
[4]: https://github.com/exasol/error-reporting-java
[2]: https://github.com/exasol/parquet-io-java
[43]: http://www.apache.org/licenses/LICENSE-2.0.txt
[11]: https://projectlombok.org
[48]: https://maven.apache.org/surefire/maven-surefire-plugin/
[76]: http://maven.apache.org/plugins/maven-clean-plugin/
[1]: https://opensource.org/licenses/MIT
[19]: https://github.com/mockito/mockito
[52]: http://www.mojohaus.org/versions-maven-plugin/
[62]: https://github.com/exasol/project-keeper/
[14]: http://opensource.org/licenses/BSD-3-Clause
[38]: https://maven.apache.org/plugins/maven-compiler-plugin/
[26]: https://www.eclipse.org/legal/epl-2.0/
[54]: https://maven.apache.org/plugins/maven-deploy-plugin/
[37]: http://www.gnu.org/licenses/lgpl.txt
[72]: https://www.jacoco.org/jacoco/trunk/doc/maven.html
[20]: https://github.com/mockito/mockito/blob/main/LICENSE
[12]: https://projectlombok.org/LICENSE
[29]: https://github.com/exasol/hamcrest-resultset-matcher
[46]: http://zlika.github.io/reproducible-build-maven-plugin
[36]: http://sonarsource.github.io/sonar-scanner-maven/
[31]: https://github.com/exasol/udf-debugging-java/
[15]: https://junit.org/junit5/
[42]: https://www.mojohaus.org/flatten-maven-plugin/flatten-maven-plugin
[6]: https://github.com/eclipse-ee4j/jsonp
[60]: https://maven.apache.org/plugins/maven-source-plugin/
[8]: https://projects.eclipse.org/license/secondary-gpl-2.0-cp
[13]: http://hamcrest.org/JavaHamcrest/
[78]: http://maven.apache.org/plugins/maven-resources-plugin/
[58]: https://maven.apache.org/plugins/maven-jar-plugin/
[66]: http://www.sonatype.com/public-parent/nexus-maven-plugins/nexus-staging/nexus-staging-maven-plugin/
[70]: https://maven.apache.org/surefire/maven-failsafe-plugin/
[9]: https://github.com/exasol/performance-test-recorder-java
[50]: http://maven.apache.org/plugins/maven-dependency-plugin/
[67]: http://www.eclipse.org/legal/epl-v10.html
[63]: https://github.com/exasol/project-keeper/blob/main/LICENSE
[7]: https://projects.eclipse.org/license/epl-2.0
[39]: https://www.apache.org/licenses/LICENSE-2.0.txt
[40]: https://maven.apache.org/enforcer/maven-enforcer-plugin/
[68]: https://awhitford.github.com/lombok.maven/lombok-maven-plugin/
[16]: https://www.eclipse.org/legal/epl-v20.html
[80]: http://maven.apache.org/plugins/maven-install-plugin/
[44]: https://sonatype.github.io/ossindex-maven/maven-plugin/
[56]: https://maven.apache.org/plugins/maven-gpg-plugin/
[82]: http://maven.apache.org/plugins/maven-site-plugin/
[64]: https://maven.apache.org/plugins/maven-javadoc-plugin/
[74]: https://github.com/exasol/error-code-crawler-maven-plugin
