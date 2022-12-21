<!-- @formatter:off -->
# Dependencies

## Compile Dependencies

| Dependency                                   | License                                                                                                        |
| -------------------------------------------- | -------------------------------------------------------------------------------------------------------------- |
| [Common Virtual Schema for document data][0] | [MIT][1]                                                                                                       |
| [Parquet for Java][2]                        | [MIT License][3]                                                                                               |
| [Woodstox][4]                                | [The Apache License, Version 2.0][5]                                                                           |
| [jackson-databind][6]                        | [The Apache Software License, Version 2.0][7]                                                                  |
| [error-reporting-java][8]                    | [MIT License][9]                                                                                               |
| [Jakarta JSON Processing API][10]            | [Eclipse Public License 2.0][11]; [GNU General Public License, version 2 with the GNU Classpath Exception][12] |
| [FastCSV][13]                                | [MIT License][1]                                                                                               |

## Test Dependencies

| Dependency                                   | License                           |
| -------------------------------------------- | --------------------------------- |
| [Hamcrest][14]                               | [BSD License 3][15]               |
| [JUnit Jupiter Engine][16]                   | [Eclipse Public License v2.0][17] |
| [JUnit Jupiter Params][16]                   | [Eclipse Public License v2.0][17] |
| [EqualsVerifier | release normal jar][18]    | [Apache License, Version 2.0][7]  |
| [mockito-core][19]                           | [The MIT License][20]             |
| [mockito-junit-jupiter][19]                  | [The MIT License][20]             |
| [Common Virtual Schema for document data][0] | [MIT][1]                          |
| [Matcher for SQL Result Sets][21]            | [MIT License][22]                 |
| [udf-debugging-java][23]                     | [MIT][1]                          |
| [Performance Test Recorder Java][24]         | [MIT License][25]                 |
| [Apache Commons Text][26]                    | [Apache License, Version 2.0][7]  |

## Runtime Dependencies

| Dependency                    | License                                                                                                        |
| ----------------------------- | -------------------------------------------------------------------------------------------------------------- |
| [JSON-P Default Provider][10] | [Eclipse Public License 2.0][11]; [GNU General Public License, version 2 with the GNU Classpath Exception][12] |

## Plugin Dependencies

| Dependency                                              | License                                       |
| ------------------------------------------------------- | --------------------------------------------- |
| [SonarQube Scanner for Maven][27]                       | [GNU LGPL 3][28]                              |
| [Apache Maven Compiler Plugin][29]                      | [Apache License, Version 2.0][7]              |
| [Apache Maven Enforcer Plugin][30]                      | [Apache License, Version 2.0][7]              |
| [Maven Flatten Plugin][31]                              | [Apache Software Licenese][7]                 |
| [org.sonatype.ossindex.maven:ossindex-maven-plugin][32] | [ASL2][5]                                     |
| [Maven Surefire Plugin][33]                             | [Apache License, Version 2.0][7]              |
| [Versions Maven Plugin][34]                             | [Apache License, Version 2.0][7]              |
| [Apache Maven Deploy Plugin][35]                        | [Apache License, Version 2.0][7]              |
| [Apache Maven GPG Plugin][36]                           | [Apache License, Version 2.0][7]              |
| [Apache Maven Source Plugin][37]                        | [Apache License, Version 2.0][7]              |
| [Apache Maven Javadoc Plugin][38]                       | [Apache License, Version 2.0][7]              |
| [Nexus Staging Maven Plugin][39]                        | [Eclipse Public License][40]                  |
| [JaCoCo :: Maven Plugin][41]                            | [Eclipse Public License 2.0][42]              |
| [error-code-crawler-maven-plugin][43]                   | [MIT License][44]                             |
| [Reproducible Build Maven Plugin][45]                   | [Apache 2.0][5]                               |
| [Project keeper maven plugin][46]                       | [The MIT License][47]                         |
| [Apache Maven JAR Plugin][48]                           | [Apache License, Version 2.0][7]              |
| [Maven Clean Plugin][49]                                | [The Apache Software License, Version 2.0][5] |
| [Maven Resources Plugin][50]                            | [The Apache Software License, Version 2.0][5] |
| [Maven Install Plugin][51]                              | [The Apache Software License, Version 2.0][5] |
| [Maven Site Plugin 3][52]                               | [The Apache Software License, Version 2.0][5] |

[0]: https://github.com/exasol/virtual-schema-common-document/
[1]: https://opensource.org/licenses/MIT
[2]: https://github.com/exasol/parquet-io-java/
[3]: https://github.com/exasol/parquet-io-java/blob/main/LICENSE
[4]: https://github.com/FasterXML/woodstox
[5]: http://www.apache.org/licenses/LICENSE-2.0.txt
[6]: https://github.com/FasterXML/jackson
[7]: https://www.apache.org/licenses/LICENSE-2.0.txt
[8]: https://github.com/exasol/error-reporting-java/
[9]: https://github.com/exasol/error-reporting-java/blob/main/LICENSE
[10]: https://github.com/eclipse-ee4j/jsonp
[11]: https://projects.eclipse.org/license/epl-2.0
[12]: https://projects.eclipse.org/license/secondary-gpl-2.0-cp
[13]: https://github.com/osiegmar/FastCSV
[14]: http://hamcrest.org/JavaHamcrest/
[15]: http://opensource.org/licenses/BSD-3-Clause
[16]: https://junit.org/junit5/
[17]: https://www.eclipse.org/legal/epl-v20.html
[18]: https://www.jqno.nl/equalsverifier
[19]: https://github.com/mockito/mockito
[20]: https://github.com/mockito/mockito/blob/main/LICENSE
[21]: https://github.com/exasol/hamcrest-resultset-matcher/
[22]: https://github.com/exasol/hamcrest-resultset-matcher/blob/main/LICENSE
[23]: https://github.com/exasol/udf-debugging-java/
[24]: https://github.com/exasol/performance-test-recorder-java/
[25]: https://github.com/exasol/performance-test-recorder-java/blob/main/LICENSE
[26]: https://commons.apache.org/proper/commons-text
[27]: http://sonarsource.github.io/sonar-scanner-maven/
[28]: http://www.gnu.org/licenses/lgpl.txt
[29]: https://maven.apache.org/plugins/maven-compiler-plugin/
[30]: https://maven.apache.org/enforcer/maven-enforcer-plugin/
[31]: https://www.mojohaus.org/flatten-maven-plugin/
[32]: https://sonatype.github.io/ossindex-maven/maven-plugin/
[33]: https://maven.apache.org/surefire/maven-surefire-plugin/
[34]: https://www.mojohaus.org/versions-maven-plugin/
[35]: https://maven.apache.org/plugins/maven-deploy-plugin/
[36]: https://maven.apache.org/plugins/maven-gpg-plugin/
[37]: https://maven.apache.org/plugins/maven-source-plugin/
[38]: https://maven.apache.org/plugins/maven-javadoc-plugin/
[39]: http://www.sonatype.com/public-parent/nexus-maven-plugins/nexus-staging/nexus-staging-maven-plugin/
[40]: http://www.eclipse.org/legal/epl-v10.html
[41]: https://www.jacoco.org/jacoco/trunk/doc/maven.html
[42]: https://www.eclipse.org/legal/epl-2.0/
[43]: https://github.com/exasol/error-code-crawler-maven-plugin/
[44]: https://github.com/exasol/error-code-crawler-maven-plugin/blob/main/LICENSE
[45]: http://zlika.github.io/reproducible-build-maven-plugin
[46]: https://github.com/exasol/project-keeper/
[47]: https://github.com/exasol/project-keeper/blob/main/LICENSE
[48]: https://maven.apache.org/plugins/maven-jar-plugin/
[49]: http://maven.apache.org/plugins/maven-clean-plugin/
[50]: http://maven.apache.org/plugins/maven-resources-plugin/
[51]: http://maven.apache.org/plugins/maven-install-plugin/
[52]: http://maven.apache.org/plugins/maven-site-plugin/
