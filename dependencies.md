<!-- @formatter:off -->
# Dependencies

## Compile Dependencies

| Dependency                                   | License                                                                                                       |
| -------------------------------------------- | ------------------------------------------------------------------------------------------------------------- |
| [Common Virtual Schema for document data][0] | [MIT License][1]                                                                                              |
| [Parquet for Java][2]                        | [MIT License][3]                                                                                              |
| [FastCSV][4]                                 | [MIT License][5]                                                                                              |
| [deephaven-csv][6]                           | [The Apache License, Version 2.0][7]                                                                          |
| [Jakarta JSON Processing API][8]             | [Eclipse Public License 2.0][9]; [GNU General Public License, version 2 with the GNU Classpath Exception][10] |
| [error-reporting-java][11]                   | [MIT License][12]                                                                                             |

## Test Dependencies

| Dependency                                   | License                           |
| -------------------------------------------- | --------------------------------- |
| [Hamcrest][13]                               | [BSD-3-Clause][14]                |
| [JUnit Jupiter Params][15]                   | [Eclipse Public License v2.0][16] |
| [EqualsVerifier \| release normal jar][17]   | [Apache License, Version 2.0][7]  |
| [to-string-verifier][18]                     | [MIT License][19]                 |
| [mockito-junit-jupiter][20]                  | [MIT][5]                          |
| [Common Virtual Schema for document data][0] | [MIT License][1]                  |
| [Matcher for SQL Result Sets][21]            | [MIT License][22]                 |
| [udf-debugging-java][23]                     | [MIT License][24]                 |
| [Performance Test Recorder Java][25]         | [MIT License][26]                 |

## Runtime Dependencies

| Dependency                   | License                                                                                                       |
| ---------------------------- | ------------------------------------------------------------------------------------------------------------- |
| [JSON-P Default Provider][8] | [Eclipse Public License 2.0][9]; [GNU General Public License, version 2 with the GNU Classpath Exception][10] |

## Plugin Dependencies

| Dependency                                              | License                                     |
| ------------------------------------------------------- | ------------------------------------------- |
| [Apache Maven Clean Plugin][27]                         | [Apache-2.0][7]                             |
| [Apache Maven Install Plugin][28]                       | [Apache-2.0][7]                             |
| [Apache Maven Resources Plugin][29]                     | [Apache-2.0][7]                             |
| [Apache Maven Site Plugin][30]                          | [Apache-2.0][7]                             |
| [SonarQube Scanner for Maven][31]                       | [GNU LGPL 3][32]                            |
| [Apache Maven Toolchains Plugin][33]                    | [Apache-2.0][7]                             |
| [Apache Maven Compiler Plugin][34]                      | [Apache-2.0][7]                             |
| [Apache Maven Enforcer Plugin][35]                      | [Apache-2.0][7]                             |
| [Maven Flatten Plugin][36]                              | [Apache Software License][7]                |
| [Project Keeper Maven plugin][37]                       | [The MIT License][38]                       |
| [Apache Maven JAR Plugin][39]                           | [Apache-2.0][7]                             |
| [org.sonatype.ossindex.maven:ossindex-maven-plugin][40] | [ASL2][41]                                  |
| [Maven Surefire Plugin][42]                             | [Apache-2.0][7]                             |
| [Versions Maven Plugin][43]                             | [Apache License, Version 2.0][7]            |
| [duplicate-finder-maven-plugin Maven Mojo][44]          | [Apache License 2.0][45]                    |
| [Apache Maven Artifact Plugin][46]                      | [Apache-2.0][7]                             |
| [Apache Maven Deploy Plugin][47]                        | [Apache-2.0][7]                             |
| [Apache Maven GPG Plugin][48]                           | [Apache-2.0][7]                             |
| [Apache Maven Source Plugin][49]                        | [Apache-2.0][7]                             |
| [Apache Maven Javadoc Plugin][50]                       | [Apache-2.0][7]                             |
| [Central Publishing Maven Plugin][51]                   | [The Apache License, Version 2.0][7]        |
| [JaCoCo :: Maven Plugin][52]                            | [EPL-2.0][53]                               |
| [Quality Summarizer Maven Plugin][54]                   | [MIT License][55]                           |
| [error-code-crawler-maven-plugin][56]                   | [MIT License][57]                           |
| [Git Commit Id Maven Plugin][58]                        | [GNU Lesser General Public License 3.0][59] |

[0]: https://github.com/exasol/virtual-schema-common-document/
[1]: https://github.com/exasol/virtual-schema-common-document/blob/main/LICENSE
[2]: https://github.com/exasol/parquet-io-java/
[3]: https://github.com/exasol/parquet-io-java/blob/main/LICENSE
[4]: https://fastcsv.org
[5]: https://opensource.org/licenses/MIT
[6]: https://github.com/deephaven/deephaven-csv
[7]: https://www.apache.org/licenses/LICENSE-2.0.txt
[8]: https://github.com/eclipse-ee4j/jsonp
[9]: https://projects.eclipse.org/license/epl-2.0
[10]: https://projects.eclipse.org/license/secondary-gpl-2.0-cp
[11]: https://github.com/exasol/error-reporting-java/
[12]: https://github.com/exasol/error-reporting-java/blob/main/LICENSE
[13]: http://hamcrest.org/JavaHamcrest/
[14]: https://raw.githubusercontent.com/hamcrest/JavaHamcrest/master/LICENSE
[15]: https://junit.org/junit5/
[16]: https://www.eclipse.org/legal/epl-v20.html
[17]: https://www.jqno.nl/equalsverifier
[18]: https://github.com/jparams/to-string-verifier
[19]: http://www.opensource.org/licenses/mit-license.php
[20]: https://github.com/mockito/mockito
[21]: https://github.com/exasol/hamcrest-resultset-matcher/
[22]: https://github.com/exasol/hamcrest-resultset-matcher/blob/main/LICENSE
[23]: https://github.com/exasol/udf-debugging-java/
[24]: https://github.com/exasol/udf-debugging-java/blob/main/LICENSE
[25]: https://github.com/exasol/performance-test-recorder-java/
[26]: https://github.com/exasol/performance-test-recorder-java/blob/main/LICENSE
[27]: https://maven.apache.org/plugins/maven-clean-plugin/
[28]: https://maven.apache.org/plugins/maven-install-plugin/
[29]: https://maven.apache.org/plugins/maven-resources-plugin/
[30]: https://maven.apache.org/plugins/maven-site-plugin/
[31]: https://docs.sonarsource.com/sonarqube-server/latest/extension-guide/developing-a-plugin/plugin-basics/sonar-scanner-maven/sonar-maven-plugin/
[32]: http://www.gnu.org/licenses/lgpl.txt
[33]: https://maven.apache.org/plugins/maven-toolchains-plugin/
[34]: https://maven.apache.org/plugins/maven-compiler-plugin/
[35]: https://maven.apache.org/enforcer/maven-enforcer-plugin/
[36]: https://www.mojohaus.org/flatten-maven-plugin/
[37]: https://github.com/exasol/project-keeper/
[38]: https://github.com/exasol/project-keeper/blob/main/LICENSE
[39]: https://maven.apache.org/plugins/maven-jar-plugin/
[40]: https://sonatype.github.io/ossindex-maven/maven-plugin/
[41]: http://www.apache.org/licenses/LICENSE-2.0.txt
[42]: https://maven.apache.org/surefire/maven-surefire-plugin/
[43]: https://www.mojohaus.org/versions/versions-maven-plugin/
[44]: https://basepom.github.io/duplicate-finder-maven-plugin
[45]: http://www.apache.org/licenses/LICENSE-2.0.html
[46]: https://maven.apache.org/plugins/maven-artifact-plugin/
[47]: https://maven.apache.org/plugins/maven-deploy-plugin/
[48]: https://maven.apache.org/plugins/maven-gpg-plugin/
[49]: https://maven.apache.org/plugins/maven-source-plugin/
[50]: https://maven.apache.org/plugins/maven-javadoc-plugin/
[51]: https://central.sonatype.org
[52]: https://www.jacoco.org/jacoco/trunk/doc/maven.html
[53]: https://www.eclipse.org/legal/epl-2.0/
[54]: https://github.com/exasol/quality-summarizer-maven-plugin/
[55]: https://github.com/exasol/quality-summarizer-maven-plugin/blob/main/LICENSE
[56]: https://github.com/exasol/error-code-crawler-maven-plugin/
[57]: https://github.com/exasol/error-code-crawler-maven-plugin/blob/main/LICENSE
[58]: https://github.com/git-commit-id/git-commit-id-maven-plugin
[59]: http://www.gnu.org/licenses/lgpl-3.0.txt
