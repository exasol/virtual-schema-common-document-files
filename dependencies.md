<!-- @formatter:off -->
# Dependencies

## Compile Dependencies

| Dependency                                   | License                                                                                                      |
| -------------------------------------------- | ------------------------------------------------------------------------------------------------------------ |
| [Common Virtual Schema for document data][0] | [MIT License][1]                                                                                             |
| parquet-io-java                              |                                                                                                              |
| [error-reporting-java][2]                    | [MIT License][3]                                                                                             |
| [Jakarta JSON Processing API][4]             | [Eclipse Public License 2.0][5]; [GNU General Public License, version 2 with the GNU Classpath Exception][6] |
| [FastCSV][7]                                 | [MIT License][8]                                                                                             |
| [deephaven-csv][9]                           | [The Apache License, Version 2.0][10]                                                                        |

## Test Dependencies

| Dependency                                   | License                           |
| -------------------------------------------- | --------------------------------- |
| [Hamcrest][11]                               | [BSD License 3][12]               |
| [JUnit Jupiter Params][13]                   | [Eclipse Public License v2.0][14] |
| [EqualsVerifier \| release normal jar][15]   | [Apache License, Version 2.0][10] |
| [mockito-junit-jupiter][16]                  | [The MIT License][17]             |
| [Common Virtual Schema for document data][0] | [MIT License][1]                  |
| [Matcher for SQL Result Sets][18]            | [MIT License][19]                 |
| [udf-debugging-java][20]                     | [MIT License][21]                 |
| [Performance Test Recorder Java][22]         | [MIT License][23]                 |
| [Apache Commons Text][24]                    | [Apache License, Version 2.0][10] |

## Runtime Dependencies

| Dependency                   | License                                                                                                      |
| ---------------------------- | ------------------------------------------------------------------------------------------------------------ |
| [JSON-P Default Provider][4] | [Eclipse Public License 2.0][5]; [GNU General Public License, version 2 with the GNU Classpath Exception][6] |

## Plugin Dependencies

| Dependency                                              | License                                        |
| ------------------------------------------------------- | ---------------------------------------------- |
| [SonarQube Scanner for Maven][25]                       | [GNU LGPL 3][26]                               |
| [Apache Maven Compiler Plugin][27]                      | [Apache-2.0][10]                               |
| [Apache Maven Enforcer Plugin][28]                      | [Apache-2.0][10]                               |
| [Maven Flatten Plugin][29]                              | [Apache Software Licenese][10]                 |
| [org.sonatype.ossindex.maven:ossindex-maven-plugin][30] | [ASL2][31]                                     |
| [Maven Surefire Plugin][32]                             | [Apache-2.0][10]                               |
| [Versions Maven Plugin][33]                             | [Apache License, Version 2.0][10]              |
| [duplicate-finder-maven-plugin Maven Mojo][34]          | [Apache License 2.0][35]                       |
| [Apache Maven Deploy Plugin][36]                        | [Apache-2.0][10]                               |
| [Apache Maven GPG Plugin][37]                           | [Apache-2.0][10]                               |
| [Apache Maven Source Plugin][38]                        | [Apache License, Version 2.0][10]              |
| [Apache Maven Javadoc Plugin][39]                       | [Apache-2.0][10]                               |
| [Nexus Staging Maven Plugin][40]                        | [Eclipse Public License][41]                   |
| [JaCoCo :: Maven Plugin][42]                            | [Eclipse Public License 2.0][43]               |
| [error-code-crawler-maven-plugin][44]                   | [MIT License][45]                              |
| [Reproducible Build Maven Plugin][46]                   | [Apache 2.0][31]                               |
| [Project keeper maven plugin][47]                       | [The MIT License][48]                          |
| [Apache Maven JAR Plugin][49]                           | [Apache License, Version 2.0][10]              |
| [Maven Clean Plugin][50]                                | [The Apache Software License, Version 2.0][31] |
| [Maven Resources Plugin][51]                            | [The Apache Software License, Version 2.0][31] |
| [Maven Install Plugin][52]                              | [The Apache Software License, Version 2.0][31] |
| [Maven Site Plugin 3][53]                               | [The Apache Software License, Version 2.0][31] |

[0]: https://github.com/exasol/virtual-schema-common-document/
[1]: https://github.com/exasol/virtual-schema-common-document/blob/main/LICENSE
[2]: https://github.com/exasol/error-reporting-java/
[3]: https://github.com/exasol/error-reporting-java/blob/main/LICENSE
[4]: https://github.com/eclipse-ee4j/jsonp
[5]: https://projects.eclipse.org/license/epl-2.0
[6]: https://projects.eclipse.org/license/secondary-gpl-2.0-cp
[7]: https://github.com/osiegmar/FastCSV
[8]: https://opensource.org/licenses/MIT
[9]: https://github.com/deephaven/deephaven-csv
[10]: https://www.apache.org/licenses/LICENSE-2.0.txt
[11]: http://hamcrest.org/JavaHamcrest/
[12]: http://opensource.org/licenses/BSD-3-Clause
[13]: https://junit.org/junit5/
[14]: https://www.eclipse.org/legal/epl-v20.html
[15]: https://www.jqno.nl/equalsverifier
[16]: https://github.com/mockito/mockito
[17]: https://github.com/mockito/mockito/blob/main/LICENSE
[18]: https://github.com/exasol/hamcrest-resultset-matcher/
[19]: https://github.com/exasol/hamcrest-resultset-matcher/blob/main/LICENSE
[20]: https://github.com/exasol/udf-debugging-java/
[21]: https://github.com/exasol/udf-debugging-java/blob/main/LICENSE
[22]: https://github.com/exasol/performance-test-recorder-java/
[23]: https://github.com/exasol/performance-test-recorder-java/blob/main/LICENSE
[24]: https://commons.apache.org/proper/commons-text
[25]: http://sonarsource.github.io/sonar-scanner-maven/
[26]: http://www.gnu.org/licenses/lgpl.txt
[27]: https://maven.apache.org/plugins/maven-compiler-plugin/
[28]: https://maven.apache.org/enforcer/maven-enforcer-plugin/
[29]: https://www.mojohaus.org/flatten-maven-plugin/
[30]: https://sonatype.github.io/ossindex-maven/maven-plugin/
[31]: http://www.apache.org/licenses/LICENSE-2.0.txt
[32]: https://maven.apache.org/surefire/maven-surefire-plugin/
[33]: https://www.mojohaus.org/versions/versions-maven-plugin/
[34]: https://basepom.github.io/duplicate-finder-maven-plugin
[35]: http://www.apache.org/licenses/LICENSE-2.0.html
[36]: https://maven.apache.org/plugins/maven-deploy-plugin/
[37]: https://maven.apache.org/plugins/maven-gpg-plugin/
[38]: https://maven.apache.org/plugins/maven-source-plugin/
[39]: https://maven.apache.org/plugins/maven-javadoc-plugin/
[40]: http://www.sonatype.com/public-parent/nexus-maven-plugins/nexus-staging/nexus-staging-maven-plugin/
[41]: http://www.eclipse.org/legal/epl-v10.html
[42]: https://www.jacoco.org/jacoco/trunk/doc/maven.html
[43]: https://www.eclipse.org/legal/epl-2.0/
[44]: https://github.com/exasol/error-code-crawler-maven-plugin/
[45]: https://github.com/exasol/error-code-crawler-maven-plugin/blob/main/LICENSE
[46]: http://zlika.github.io/reproducible-build-maven-plugin
[47]: https://github.com/exasol/project-keeper/
[48]: https://github.com/exasol/project-keeper/blob/main/LICENSE
[49]: https://maven.apache.org/plugins/maven-jar-plugin/
[50]: http://maven.apache.org/plugins/maven-clean-plugin/
[51]: http://maven.apache.org/plugins/maven-resources-plugin/
[52]: http://maven.apache.org/plugins/maven-install-plugin/
[53]: http://maven.apache.org/plugins/maven-site-plugin/
