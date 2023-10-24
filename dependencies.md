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
| [deephaven-csv][11]                          | [The Apache License, Version 2.0][12]                                                                        |

## Test Dependencies

| Dependency                                   | License                           |
| -------------------------------------------- | --------------------------------- |
| [Hamcrest][13]                               | [BSD License 3][14]               |
| [JUnit Jupiter Params][15]                   | [Eclipse Public License v2.0][16] |
| [EqualsVerifier \| release normal jar][17]   | [Apache License, Version 2.0][12] |
| [mockito-junit-jupiter][18]                  | [MIT][19]                         |
| [Common Virtual Schema for document data][0] | [MIT License][1]                  |
| [Matcher for SQL Result Sets][20]            | [MIT License][21]                 |
| [udf-debugging-java][22]                     | [MIT License][23]                 |
| [Performance Test Recorder Java][24]         | [MIT License][25]                 |
| [Apache Commons Text][26]                    | [Apache License, Version 2.0][12] |

## Runtime Dependencies

| Dependency                   | License                                                                                                      |
| ---------------------------- | ------------------------------------------------------------------------------------------------------------ |
| [JSON-P Default Provider][6] | [Eclipse Public License 2.0][7]; [GNU General Public License, version 2 with the GNU Classpath Exception][8] |

## Plugin Dependencies

| Dependency                                              | License                           |
| ------------------------------------------------------- | --------------------------------- |
| [SonarQube Scanner for Maven][27]                       | [GNU LGPL 3][28]                  |
| [Apache Maven Compiler Plugin][29]                      | [Apache-2.0][12]                  |
| [Maven Flatten Plugin][30]                              | [Apache Software Licenese][12]    |
| [org.sonatype.ossindex.maven:ossindex-maven-plugin][31] | [ASL2][32]                        |
| [Maven Surefire Plugin][33]                             | [Apache-2.0][12]                  |
| [Versions Maven Plugin][34]                             | [Apache License, Version 2.0][12] |
| [duplicate-finder-maven-plugin Maven Mojo][35]          | [Apache License 2.0][36]          |
| [Apache Maven Deploy Plugin][37]                        | [Apache-2.0][12]                  |
| [Apache Maven GPG Plugin][38]                           | [Apache-2.0][12]                  |
| [Apache Maven Source Plugin][39]                        | [Apache License, Version 2.0][12] |
| [Apache Maven Javadoc Plugin][40]                       | [Apache-2.0][12]                  |
| [Nexus Staging Maven Plugin][41]                        | [Eclipse Public License][42]      |
| [JaCoCo :: Maven Plugin][43]                            | [Eclipse Public License 2.0][44]  |
| [error-code-crawler-maven-plugin][45]                   | [MIT License][46]                 |
| [Reproducible Build Maven Plugin][47]                   | [Apache 2.0][32]                  |
| [Project keeper maven plugin][48]                       | [The MIT License][49]             |
| [Apache Maven JAR Plugin][50]                           | [Apache License, Version 2.0][12] |

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
[11]: https://github.com/deephaven/deephaven-csv
[12]: https://www.apache.org/licenses/LICENSE-2.0.txt
[13]: http://hamcrest.org/JavaHamcrest/
[14]: http://opensource.org/licenses/BSD-3-Clause
[15]: https://junit.org/junit5/
[16]: https://www.eclipse.org/legal/epl-v20.html
[17]: https://www.jqno.nl/equalsverifier
[18]: https://github.com/mockito/mockito
[19]: https://github.com/mockito/mockito/blob/main/LICENSE
[20]: https://github.com/exasol/hamcrest-resultset-matcher/
[21]: https://github.com/exasol/hamcrest-resultset-matcher/blob/main/LICENSE
[22]: https://github.com/exasol/udf-debugging-java/
[23]: https://github.com/exasol/udf-debugging-java/blob/main/LICENSE
[24]: https://github.com/exasol/performance-test-recorder-java/
[25]: https://github.com/exasol/performance-test-recorder-java/blob/main/LICENSE
[26]: https://commons.apache.org/proper/commons-text
[27]: http://sonarsource.github.io/sonar-scanner-maven/
[28]: http://www.gnu.org/licenses/lgpl.txt
[29]: https://maven.apache.org/plugins/maven-compiler-plugin/
[30]: https://www.mojohaus.org/flatten-maven-plugin/
[31]: https://sonatype.github.io/ossindex-maven/maven-plugin/
[32]: http://www.apache.org/licenses/LICENSE-2.0.txt
[33]: https://maven.apache.org/surefire/maven-surefire-plugin/
[34]: https://www.mojohaus.org/versions/versions-maven-plugin/
[35]: https://basepom.github.io/duplicate-finder-maven-plugin
[36]: http://www.apache.org/licenses/LICENSE-2.0.html
[37]: https://maven.apache.org/plugins/maven-deploy-plugin/
[38]: https://maven.apache.org/plugins/maven-gpg-plugin/
[39]: https://maven.apache.org/plugins/maven-source-plugin/
[40]: https://maven.apache.org/plugins/maven-javadoc-plugin/
[41]: http://www.sonatype.com/public-parent/nexus-maven-plugins/nexus-staging/nexus-staging-maven-plugin/
[42]: http://www.eclipse.org/legal/epl-v10.html
[43]: https://www.jacoco.org/jacoco/trunk/doc/maven.html
[44]: https://www.eclipse.org/legal/epl-2.0/
[45]: https://github.com/exasol/error-code-crawler-maven-plugin/
[46]: https://github.com/exasol/error-code-crawler-maven-plugin/blob/main/LICENSE
[47]: http://zlika.github.io/reproducible-build-maven-plugin
[48]: https://github.com/exasol/project-keeper/
[49]: https://github.com/exasol/project-keeper/blob/main/LICENSE
[50]: https://maven.apache.org/plugins/maven-jar-plugin/
