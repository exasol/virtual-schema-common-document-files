<!-- @formatter:off -->
# Dependencies

## Compile Dependencies

| Dependency                                   | License                                                                                                      |
| -------------------------------------------- | ------------------------------------------------------------------------------------------------------------ |
| [Common Virtual Schema for document data][0] | [MIT License][1]                                                                                             |
| parquet-io-java                              |                                                                                                              |
| [FastCSV][2]                                 | [MIT License][3]                                                                                             |
| [deephaven-csv][4]                           | [The Apache License, Version 2.0][5]                                                                         |
| [Jakarta JSON Processing API][6]             | [Eclipse Public License 2.0][7]; [GNU General Public License, version 2 with the GNU Classpath Exception][8] |
| [error-reporting-java][9]                    | [MIT License][10]                                                                                            |

## Test Dependencies

| Dependency                                   | License                           |
| -------------------------------------------- | --------------------------------- |
| [Hamcrest][11]                               | [BSD-3-Clause][12]                |
| [JUnit Jupiter Params][13]                   | [Eclipse Public License v2.0][14] |
| [EqualsVerifier \| release normal jar][15]   | [Apache License, Version 2.0][5]  |
| [to-string-verifier][16]                     | [MIT License][17]                 |
| [mockito-junit-jupiter][18]                  | [MIT][3]                          |
| [Common Virtual Schema for document data][0] | [MIT License][1]                  |
| [Matcher for SQL Result Sets][19]            | [MIT License][20]                 |
| [udf-debugging-java][21]                     | [MIT License][22]                 |
| [Performance Test Recorder Java][23]         | [MIT License][24]                 |
| [SLF4J JDK14 Provider][25]                   | [MIT][26]                         |

## Runtime Dependencies

| Dependency                   | License                                                                                                      |
| ---------------------------- | ------------------------------------------------------------------------------------------------------------ |
| [JSON-P Default Provider][6] | [Eclipse Public License 2.0][7]; [GNU General Public License, version 2 with the GNU Classpath Exception][8] |

## Plugin Dependencies

| Dependency                                              | License                                        |
| ------------------------------------------------------- | ---------------------------------------------- |
| [SonarQube Scanner for Maven][27]                       | [GNU LGPL 3][28]                               |
| [Apache Maven Toolchains Plugin][29]                    | [Apache-2.0][5]                                |
| [Apache Maven Compiler Plugin][30]                      | [Apache-2.0][5]                                |
| [Apache Maven Enforcer Plugin][31]                      | [Apache-2.0][5]                                |
| [Maven Flatten Plugin][32]                              | [Apache Software License][5]                   |
| [Project Keeper Maven plugin][33]                       | [The MIT License][34]                          |
| [Apache Maven JAR Plugin][35]                           | [Apache-2.0][5]                                |
| [org.sonatype.ossindex.maven:ossindex-maven-plugin][36] | [ASL2][37]                                     |
| [Maven Surefire Plugin][38]                             | [Apache-2.0][5]                                |
| [Versions Maven Plugin][39]                             | [Apache License, Version 2.0][5]               |
| [duplicate-finder-maven-plugin Maven Mojo][40]          | [Apache License 2.0][41]                       |
| [Apache Maven Artifact Plugin][42]                      | [Apache-2.0][5]                                |
| [Apache Maven Deploy Plugin][43]                        | [Apache-2.0][5]                                |
| [Apache Maven Source Plugin][44]                        | [Apache-2.0][5]                                |
| [Apache Maven Javadoc Plugin][45]                       | [Apache-2.0][5]                                |
| [spdx-maven-plugin Maven Plugin][46]                    | [The Apache Software License, Version 2.0][37] |
| [Build Helper Maven Plugin][47]                         | [The MIT License][48]                          |
| [Apache Maven GPG Plugin][49]                           | [Apache-2.0][5]                                |
| [Central Publishing Maven Plugin][50]                   | [The Apache License, Version 2.0][5]           |
| [Maven Failsafe Plugin][51]                             | [Apache-2.0][5]                                |
| [JaCoCo :: Maven Plugin][52]                            | [EPL-2.0][53]                                  |
| [error-code-crawler-maven-plugin][54]                   | [MIT License][55]                              |
| [Git Commit Id Maven Plugin][56]                        | [GNU Lesser General Public License 3.0][57]    |
| [Apache Maven Clean Plugin][58]                         | [Apache-2.0][5]                                |
| [Apache Maven Resources Plugin][59]                     | [Apache-2.0][5]                                |
| [Apache Maven Install Plugin][60]                       | [Apache-2.0][5]                                |
| [Apache Maven Site Plugin][61]                          | [Apache-2.0][5]                                |

[0]: https://github.com/exasol/virtual-schema-common-document/
[1]: https://github.com/exasol/virtual-schema-common-document/blob/main/LICENSE
[2]: https://fastcsv.org
[3]: https://opensource.org/licenses/MIT
[4]: https://github.com/deephaven/deephaven-csv
[5]: https://www.apache.org/licenses/LICENSE-2.0.txt
[6]: https://github.com/eclipse-ee4j/jsonp
[7]: https://projects.eclipse.org/license/epl-2.0
[8]: https://projects.eclipse.org/license/secondary-gpl-2.0-cp
[9]: https://github.com/exasol/error-reporting-java/
[10]: https://github.com/exasol/error-reporting-java/blob/main/LICENSE
[11]: http://hamcrest.org/JavaHamcrest/
[12]: https://raw.githubusercontent.com/hamcrest/JavaHamcrest/master/LICENSE
[13]: https://junit.org/
[14]: https://www.eclipse.org/legal/epl-v20.html
[15]: https://www.jqno.nl/equalsverifier
[16]: https://github.com/jparams/to-string-verifier
[17]: http://www.opensource.org/licenses/mit-license.php
[18]: https://github.com/mockito/mockito
[19]: https://github.com/exasol/hamcrest-resultset-matcher/
[20]: https://github.com/exasol/hamcrest-resultset-matcher/blob/main/LICENSE
[21]: https://github.com/exasol/udf-debugging-java/
[22]: https://github.com/exasol/udf-debugging-java/blob/main/LICENSE
[23]: https://github.com/exasol/performance-test-recorder-java/
[24]: https://github.com/exasol/performance-test-recorder-java/blob/main/LICENSE
[25]: http://www.slf4j.org
[26]: https://opensource.org/license/mit
[27]: https://docs.sonarsource.com/sonarqube-server/latest/extension-guide/developing-a-plugin/plugin-basics/sonar-scanner-maven/sonar-maven-plugin/
[28]: http://www.gnu.org/licenses/lgpl.txt
[29]: https://maven.apache.org/plugins/maven-toolchains-plugin/
[30]: https://maven.apache.org/plugins/maven-compiler-plugin/
[31]: https://maven.apache.org/enforcer/maven-enforcer-plugin/
[32]: https://www.mojohaus.org/flatten-maven-plugin/
[33]: https://github.com/exasol/project-keeper/
[34]: https://github.com/exasol/project-keeper/blob/main/LICENSE
[35]: https://maven.apache.org/plugins/maven-jar-plugin/
[36]: https://sonatype.github.io/ossindex-maven/maven-plugin/
[37]: http://www.apache.org/licenses/LICENSE-2.0.txt
[38]: https://maven.apache.org/surefire/maven-surefire-plugin/
[39]: https://www.mojohaus.org/versions/versions-maven-plugin/
[40]: https://basepom.github.io/duplicate-finder-maven-plugin
[41]: http://www.apache.org/licenses/LICENSE-2.0.html
[42]: https://maven.apache.org/plugins/maven-artifact-plugin/
[43]: https://maven.apache.org/plugins/maven-deploy-plugin/
[44]: https://maven.apache.org/plugins/maven-source-plugin/
[45]: https://maven.apache.org/plugins/maven-javadoc-plugin/
[46]: https://github.com/spdx/spdx-maven-plugin
[47]: https://www.mojohaus.org/build-helper-maven-plugin/
[48]: https://spdx.org/licenses/MIT.txt
[49]: https://maven.apache.org/plugins/maven-gpg-plugin/
[50]: https://central.sonatype.org
[51]: https://maven.apache.org/surefire/maven-failsafe-plugin/
[52]: https://www.jacoco.org/jacoco/trunk/doc/maven.html
[53]: https://www.eclipse.org/legal/epl-2.0/
[54]: https://github.com/exasol/error-code-crawler-maven-plugin/
[55]: https://github.com/exasol/error-code-crawler-maven-plugin/blob/main/LICENSE
[56]: https://github.com/git-commit-id/git-commit-id-maven-plugin
[57]: http://www.gnu.org/licenses/lgpl-3.0.txt
[58]: https://maven.apache.org/plugins/maven-clean-plugin/
[59]: https://maven.apache.org/plugins/maven-resources-plugin/
[60]: https://maven.apache.org/plugins/maven-install-plugin/
[61]: https://maven.apache.org/plugins/maven-site-plugin/
