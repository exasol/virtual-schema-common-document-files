<!-- @formatter:off -->
# Dependencies

## Compile Dependencies

| Dependency                                   | License                                                                                                      |
| -------------------------------------------- | ------------------------------------------------------------------------------------------------------------ |
| [Common Virtual Schema for document data][0] | [MIT][1]                                                                                                     |
| [Parquet for Java][2]                        | [MIT License][3]                                                                                             |
| [error-reporting-java][4]                    | [MIT License][5]                                                                                             |
| [Jakarta JSON Processing API][6]             | [Eclipse Public License 2.0][7]; [GNU General Public License, version 2 with the GNU Classpath Exception][8] |
| [FastCSV][9]                                 | [MIT License][1]                                                                                             |

## Test Dependencies

| Dependency                                   | License                           |
| -------------------------------------------- | --------------------------------- |
| [Hamcrest][10]                               | [BSD License 3][11]               |
| [JUnit Jupiter Engine][12]                   | [Eclipse Public License v2.0][13] |
| [JUnit Jupiter Params][12]                   | [Eclipse Public License v2.0][13] |
| [EqualsVerifier | release normal jar][14]    | [Apache License, Version 2.0][15] |
| [mockito-core][16]                           | [The MIT License][17]             |
| [mockito-junit-jupiter][16]                  | [The MIT License][17]             |
| [Common Virtual Schema for document data][0] | [MIT][1]                          |
| [Matcher for SQL Result Sets][18]            | [MIT License][19]                 |
| [udf-debugging-java][20]                     | [MIT][1]                          |
| [Performance Test Recorder Java][21]         | [MIT License][22]                 |
| [Apache Commons Text][23]                    | [Apache License, Version 2.0][15] |

## Runtime Dependencies

| Dependency                   | License                                                                                                      |
| ---------------------------- | ------------------------------------------------------------------------------------------------------------ |
| [JSON-P Default Provider][6] | [Eclipse Public License 2.0][7]; [GNU General Public License, version 2 with the GNU Classpath Exception][8] |

## Plugin Dependencies

| Dependency                                              | License                                        |
| ------------------------------------------------------- | ---------------------------------------------- |
| [SonarQube Scanner for Maven][24]                       | [GNU LGPL 3][25]                               |
| [Apache Maven Compiler Plugin][26]                      | [Apache License, Version 2.0][15]              |
| [Apache Maven Enforcer Plugin][27]                      | [Apache License, Version 2.0][15]              |
| [Maven Flatten Plugin][28]                              | [Apache Software Licenese][29]                 |
| [org.sonatype.ossindex.maven:ossindex-maven-plugin][30] | [ASL2][29]                                     |
| [Maven Surefire Plugin][31]                             | [Apache License, Version 2.0][15]              |
| [Versions Maven Plugin][32]                             | [Apache License, Version 2.0][15]              |
| [Apache Maven Deploy Plugin][33]                        | [Apache License, Version 2.0][15]              |
| [Apache Maven GPG Plugin][34]                           | [Apache License, Version 2.0][15]              |
| [Apache Maven Source Plugin][35]                        | [Apache License, Version 2.0][15]              |
| [Apache Maven Javadoc Plugin][36]                       | [Apache License, Version 2.0][15]              |
| [Nexus Staging Maven Plugin][37]                        | [Eclipse Public License][38]                   |
| [JaCoCo :: Maven Plugin][39]                            | [Eclipse Public License 2.0][40]               |
| [error-code-crawler-maven-plugin][41]                   | [MIT License][42]                              |
| [Reproducible Build Maven Plugin][43]                   | [Apache 2.0][29]                               |
| [Project keeper maven plugin][44]                       | [The MIT License][45]                          |
| [Apache Maven JAR Plugin][46]                           | [Apache License, Version 2.0][15]              |
| [Maven Clean Plugin][47]                                | [The Apache Software License, Version 2.0][29] |
| [Maven Resources Plugin][48]                            | [The Apache Software License, Version 2.0][29] |
| [Maven Install Plugin][49]                              | [The Apache Software License, Version 2.0][29] |
| [Maven Site Plugin 3][50]                               | [The Apache Software License, Version 2.0][29] |

[0]: https://github.com/exasol/virtual-schema-common-document/
[1]: https://opensource.org/licenses/MIT
[2]: https://github.com/exasol/parquet-io-java/
[3]: https://github.com/exasol/parquet-io-java/blob/main/LICENSE
[4]: https://github.com/exasol/error-reporting-java/
[5]: https://github.com/exasol/error-reporting-java/blob/main/LICENSE
[6]: https://github.com/eclipse-ee4j/jsonp
[7]: https://projects.eclipse.org/license/epl-2.0
[8]: https://projects.eclipse.org/license/secondary-gpl-2.0-cp
[9]: https://github.com/osiegmar/FastCSV
[10]: http://hamcrest.org/JavaHamcrest/
[11]: http://opensource.org/licenses/BSD-3-Clause
[12]: https://junit.org/junit5/
[13]: https://www.eclipse.org/legal/epl-v20.html
[14]: https://www.jqno.nl/equalsverifier
[15]: https://www.apache.org/licenses/LICENSE-2.0.txt
[16]: https://github.com/mockito/mockito
[17]: https://github.com/mockito/mockito/blob/main/LICENSE
[18]: https://github.com/exasol/hamcrest-resultset-matcher/
[19]: https://github.com/exasol/hamcrest-resultset-matcher/blob/main/LICENSE
[20]: https://github.com/exasol/udf-debugging-java/
[21]: https://github.com/exasol/performance-test-recorder-java/
[22]: https://github.com/exasol/performance-test-recorder-java/blob/main/LICENSE
[23]: https://commons.apache.org/proper/commons-text
[24]: http://sonarsource.github.io/sonar-scanner-maven/
[25]: http://www.gnu.org/licenses/lgpl.txt
[26]: https://maven.apache.org/plugins/maven-compiler-plugin/
[27]: https://maven.apache.org/enforcer/maven-enforcer-plugin/
[28]: https://www.mojohaus.org/flatten-maven-plugin/
[29]: http://www.apache.org/licenses/LICENSE-2.0.txt
[30]: https://sonatype.github.io/ossindex-maven/maven-plugin/
[31]: https://maven.apache.org/surefire/maven-surefire-plugin/
[32]: http://www.mojohaus.org/versions-maven-plugin/
[33]: https://maven.apache.org/plugins/maven-deploy-plugin/
[34]: https://maven.apache.org/plugins/maven-gpg-plugin/
[35]: https://maven.apache.org/plugins/maven-source-plugin/
[36]: https://maven.apache.org/plugins/maven-javadoc-plugin/
[37]: http://www.sonatype.com/public-parent/nexus-maven-plugins/nexus-staging/nexus-staging-maven-plugin/
[38]: http://www.eclipse.org/legal/epl-v10.html
[39]: https://www.jacoco.org/jacoco/trunk/doc/maven.html
[40]: https://www.eclipse.org/legal/epl-2.0/
[41]: https://github.com/exasol/error-code-crawler-maven-plugin/
[42]: https://github.com/exasol/error-code-crawler-maven-plugin/blob/main/LICENSE
[43]: http://zlika.github.io/reproducible-build-maven-plugin
[44]: https://github.com/exasol/project-keeper/
[45]: https://github.com/exasol/project-keeper/blob/main/LICENSE
[46]: https://maven.apache.org/plugins/maven-jar-plugin/
[47]: http://maven.apache.org/plugins/maven-clean-plugin/
[48]: http://maven.apache.org/plugins/maven-resources-plugin/
[49]: http://maven.apache.org/plugins/maven-install-plugin/
[50]: http://maven.apache.org/plugins/maven-site-plugin/
