<!-- @formatter:off -->
# Dependencies

## Compile Dependencies

| Dependency                                   | License                                                                                                      |
| -------------------------------------------- | ------------------------------------------------------------------------------------------------------------ |
| [Common Virtual Schema for document data][0] | [MIT License][1]                                                                                             |
| [Parquet for Java][2]                        | [MIT License][3]                                                                                             |
| [error-reporting-java][4]                    | [MIT License][5]                                                                                             |
| [Jakarta JSON Processing API][6]             | [Eclipse Public License 2.0][7]; [GNU General Public License, version 2 with the GNU Classpath Exception][8] |
| [FastCSV][9]                                 | [MIT License][10]                                                                                            |

## Test Dependencies

| Dependency                                   | License                           |
| -------------------------------------------- | --------------------------------- |
| [Hamcrest][11]                               | [BSD License 3][12]               |
| [JUnit Jupiter Engine][13]                   | [Eclipse Public License v2.0][14] |
| [JUnit Jupiter Params][13]                   | [Eclipse Public License v2.0][14] |
| [EqualsVerifier | release normal jar][15]    | [Apache License, Version 2.0][16] |
| [mockito-junit-jupiter][17]                  | [The MIT License][18]             |
| [Common Virtual Schema for document data][0] | [MIT License][1]                  |
| [Matcher for SQL Result Sets][19]            | [MIT License][20]                 |
| [udf-debugging-java][21]                     | [MIT License][22]                 |
| [Performance Test Recorder Java][23]         | [MIT License][24]                 |
| [Apache Commons Text][25]                    | [Apache License, Version 2.0][16] |

## Runtime Dependencies

| Dependency                   | License                                                                                                      |
| ---------------------------- | ------------------------------------------------------------------------------------------------------------ |
| [JSON-P Default Provider][6] | [Eclipse Public License 2.0][7]; [GNU General Public License, version 2 with the GNU Classpath Exception][8] |

## Plugin Dependencies

| Dependency                                              | License                           |
| ------------------------------------------------------- | --------------------------------- |
| [SonarQube Scanner for Maven][26]                       | [GNU LGPL 3][27]                  |
| [Apache Maven Compiler Plugin][28]                      | [Apache License, Version 2.0][16] |
| [Apache Maven Enforcer Plugin][29]                      | [Apache-2.0][16]                  |
| [Maven Flatten Plugin][30]                              | [Apache Software Licenese][16]    |
| [org.sonatype.ossindex.maven:ossindex-maven-plugin][31] | [ASL2][32]                        |
| [Maven Surefire Plugin][33]                             | [Apache License, Version 2.0][16] |
| [Versions Maven Plugin][34]                             | [Apache License, Version 2.0][16] |
| [Apache Maven Deploy Plugin][35]                        | [Apache-2.0][16]                  |
| [Apache Maven GPG Plugin][36]                           | [Apache License, Version 2.0][16] |
| [Apache Maven Source Plugin][37]                        | [Apache License, Version 2.0][16] |
| [Apache Maven Javadoc Plugin][38]                       | [Apache License, Version 2.0][16] |
| [Nexus Staging Maven Plugin][39]                        | [Eclipse Public License][40]      |
| [JaCoCo :: Maven Plugin][41]                            | [Eclipse Public License 2.0][42]  |
| [error-code-crawler-maven-plugin][43]                   | [MIT License][44]                 |
| [Reproducible Build Maven Plugin][45]                   | [Apache 2.0][32]                  |
| [Project keeper maven plugin][46]                       | [The MIT License][47]             |
| [Apache Maven JAR Plugin][48]                           | [Apache License, Version 2.0][16] |
| [Apache Maven Clean Plugin][49]                         | [Apache License, Version 2.0][16] |
| [Apache Maven Resources Plugin][50]                     | [Apache License, Version 2.0][16] |
| [Apache Maven Install Plugin][51]                       | [Apache License, Version 2.0][16] |
| [Apache Maven Site Plugin][52]                          | [Apache License, Version 2.0][16] |

[0]: https://github.com/exasol/virtual-schema-common-document/
[1]: https://github.com/exasol/virtual-schema-common-document/blob/main/LICENSE
[2]: https://github.com/exasol/parquet-io-java/
[3]: https://github.com/exasol/parquet-io-java/blob/main/LICENSE
[4]: https://github.com/exasol/error-reporting-java/
[5]: https://github.com/exasol/error-reporting-java/blob/main/LICENSE
[6]: https://github.com/eclipse-ee4j/jsonp
[7]: https://projects.eclipse.org/license/epl-2.0
[8]: https://projects.eclipse.org/license/secondary-gpl-2.0-cp
[9]: https://github.com/osiegmar/FastCSV
[10]: https://opensource.org/licenses/MIT
[11]: http://hamcrest.org/JavaHamcrest/
[12]: http://opensource.org/licenses/BSD-3-Clause
[13]: https://junit.org/junit5/
[14]: https://www.eclipse.org/legal/epl-v20.html
[15]: https://www.jqno.nl/equalsverifier
[16]: https://www.apache.org/licenses/LICENSE-2.0.txt
[17]: https://github.com/mockito/mockito
[18]: https://github.com/mockito/mockito/blob/main/LICENSE
[19]: https://github.com/exasol/hamcrest-resultset-matcher/
[20]: https://github.com/exasol/hamcrest-resultset-matcher/blob/main/LICENSE
[21]: https://github.com/exasol/udf-debugging-java/
[22]: https://github.com/exasol/udf-debugging-java/blob/main/LICENSE
[23]: https://github.com/exasol/performance-test-recorder-java/
[24]: https://github.com/exasol/performance-test-recorder-java/blob/main/LICENSE
[25]: https://commons.apache.org/proper/commons-text
[26]: http://sonarsource.github.io/sonar-scanner-maven/
[27]: http://www.gnu.org/licenses/lgpl.txt
[28]: https://maven.apache.org/plugins/maven-compiler-plugin/
[29]: https://maven.apache.org/enforcer/maven-enforcer-plugin/
[30]: https://www.mojohaus.org/flatten-maven-plugin/
[31]: https://sonatype.github.io/ossindex-maven/maven-plugin/
[32]: http://www.apache.org/licenses/LICENSE-2.0.txt
[33]: https://maven.apache.org/surefire/maven-surefire-plugin/
[34]: https://www.mojohaus.org/versions/versions-maven-plugin/
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
[49]: https://maven.apache.org/plugins/maven-clean-plugin/
[50]: https://maven.apache.org/plugins/maven-resources-plugin/
[51]: https://maven.apache.org/plugins/maven-install-plugin/
[52]: https://maven.apache.org/plugins/maven-site-plugin/
