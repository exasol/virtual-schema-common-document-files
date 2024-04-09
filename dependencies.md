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
| [mockito-junit-jupiter][18]                  | [MIT][10]                         |
| [Common Virtual Schema for document data][0] | [MIT License][1]                  |
| [Matcher for SQL Result Sets][19]            | [MIT License][20]                 |
| [udf-debugging-java][21]                     | [MIT License][22]                 |
| [Performance Test Recorder Java][23]         | [MIT License][24]                 |
| [Apache Commons Text][25]                    | [Apache-2.0][12]                  |

## Runtime Dependencies

| Dependency                   | License                                                                                                      |
| ---------------------------- | ------------------------------------------------------------------------------------------------------------ |
| [JSON-P Default Provider][6] | [Eclipse Public License 2.0][7]; [GNU General Public License, version 2 with the GNU Classpath Exception][8] |

## Plugin Dependencies

| Dependency                                              | License                           |
| ------------------------------------------------------- | --------------------------------- |
| [SonarQube Scanner for Maven][26]                       | [GNU LGPL 3][27]                  |
| [Apache Maven Toolchains Plugin][28]                    | [Apache License, Version 2.0][12] |
| [Apache Maven Compiler Plugin][29]                      | [Apache-2.0][12]                  |
| [Apache Maven Enforcer Plugin][30]                      | [Apache-2.0][12]                  |
| [Maven Flatten Plugin][31]                              | [Apache Software Licenese][12]    |
| [org.sonatype.ossindex.maven:ossindex-maven-plugin][32] | [ASL2][33]                        |
| [Maven Surefire Plugin][34]                             | [Apache-2.0][12]                  |
| [Versions Maven Plugin][35]                             | [Apache License, Version 2.0][12] |
| [duplicate-finder-maven-plugin Maven Mojo][36]          | [Apache License 2.0][37]          |
| [Apache Maven Deploy Plugin][38]                        | [Apache-2.0][12]                  |
| [Apache Maven GPG Plugin][39]                           | [Apache-2.0][12]                  |
| [Apache Maven Source Plugin][40]                        | [Apache License, Version 2.0][12] |
| [Apache Maven Javadoc Plugin][41]                       | [Apache-2.0][12]                  |
| [Nexus Staging Maven Plugin][42]                        | [Eclipse Public License][43]      |
| [JaCoCo :: Maven Plugin][44]                            | [EPL-2.0][45]                     |
| [error-code-crawler-maven-plugin][46]                   | [MIT License][47]                 |
| [Reproducible Build Maven Plugin][48]                   | [Apache 2.0][33]                  |
| [Project Keeper Maven plugin][49]                       | [The MIT License][50]             |
| [Apache Maven JAR Plugin][51]                           | [Apache License, Version 2.0][12] |

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
[19]: https://github.com/exasol/hamcrest-resultset-matcher/
[20]: https://github.com/exasol/hamcrest-resultset-matcher/blob/main/LICENSE
[21]: https://github.com/exasol/udf-debugging-java/
[22]: https://github.com/exasol/udf-debugging-java/blob/main/LICENSE
[23]: https://github.com/exasol/performance-test-recorder-java/
[24]: https://github.com/exasol/performance-test-recorder-java/blob/main/LICENSE
[25]: https://commons.apache.org/proper/commons-text
[26]: http://sonarsource.github.io/sonar-scanner-maven/
[27]: http://www.gnu.org/licenses/lgpl.txt
[28]: https://maven.apache.org/plugins/maven-toolchains-plugin/
[29]: https://maven.apache.org/plugins/maven-compiler-plugin/
[30]: https://maven.apache.org/enforcer/maven-enforcer-plugin/
[31]: https://www.mojohaus.org/flatten-maven-plugin/
[32]: https://sonatype.github.io/ossindex-maven/maven-plugin/
[33]: http://www.apache.org/licenses/LICENSE-2.0.txt
[34]: https://maven.apache.org/surefire/maven-surefire-plugin/
[35]: https://www.mojohaus.org/versions/versions-maven-plugin/
[36]: https://basepom.github.io/duplicate-finder-maven-plugin
[37]: http://www.apache.org/licenses/LICENSE-2.0.html
[38]: https://maven.apache.org/plugins/maven-deploy-plugin/
[39]: https://maven.apache.org/plugins/maven-gpg-plugin/
[40]: https://maven.apache.org/plugins/maven-source-plugin/
[41]: https://maven.apache.org/plugins/maven-javadoc-plugin/
[42]: http://www.sonatype.com/public-parent/nexus-maven-plugins/nexus-staging/nexus-staging-maven-plugin/
[43]: http://www.eclipse.org/legal/epl-v10.html
[44]: https://www.jacoco.org/jacoco/trunk/doc/maven.html
[45]: https://www.eclipse.org/legal/epl-2.0/
[46]: https://github.com/exasol/error-code-crawler-maven-plugin/
[47]: https://github.com/exasol/error-code-crawler-maven-plugin/blob/main/LICENSE
[48]: http://zlika.github.io/reproducible-build-maven-plugin
[49]: https://github.com/exasol/project-keeper/
[50]: https://github.com/exasol/project-keeper/blob/main/LICENSE
[51]: https://maven.apache.org/plugins/maven-jar-plugin/
