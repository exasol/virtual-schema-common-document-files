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
| [Project Lombok][10]                         | [The MIT License][11]                                                                                        |

## Test Dependencies

| Dependency                                   | License                           |
| -------------------------------------------- | --------------------------------- |
| [Hamcrest][12]                               | [BSD License 3][13]               |
| [JUnit Jupiter Engine][14]                   | [Eclipse Public License v2.0][15] |
| [JUnit Jupiter Params][14]                   | [Eclipse Public License v2.0][15] |
| [mockito-core][16]                           | [The MIT License][17]             |
| [mockito-junit-jupiter][16]                  | [The MIT License][17]             |
| [Common Virtual Schema for document data][0] | [MIT][1]                          |
| [Matcher for SQL Result Sets][18]            | [MIT License][19]                 |
| [udf-debugging-java][20]                     | [MIT][1]                          |
| [exasol-test-setup-abstraction-java][21]     | [MIT License][22]                 |
| [Performance Test Recorder Java][23]         | [MIT License][24]                 |

## Runtime Dependencies

| Dependency                   | License                                                                                                      |
| ---------------------------- | ------------------------------------------------------------------------------------------------------------ |
| [JSON-P Default Provider][6] | [Eclipse Public License 2.0][7]; [GNU General Public License, version 2 with the GNU Classpath Exception][8] |

## Plugin Dependencies

| Dependency                                              | License                                        |
| ------------------------------------------------------- | ---------------------------------------------- |
| [SonarQube Scanner for Maven][25]                       | [GNU LGPL 3][26]                               |
| [Apache Maven Compiler Plugin][27]                      | [Apache License, Version 2.0][28]              |
| [Apache Maven Enforcer Plugin][29]                      | [Apache License, Version 2.0][28]              |
| [Maven Flatten Plugin][30]                              | [Apache Software Licenese][31]                 |
| [org.sonatype.ossindex.maven:ossindex-maven-plugin][32] | [ASL2][31]                                     |
| [Maven Surefire Plugin][33]                             | [Apache License, Version 2.0][28]              |
| [Versions Maven Plugin][34]                             | [Apache License, Version 2.0][28]              |
| [Apache Maven Deploy Plugin][35]                        | [Apache License, Version 2.0][28]              |
| [Apache Maven GPG Plugin][36]                           | [Apache License, Version 2.0][28]              |
| [Apache Maven Source Plugin][37]                        | [Apache License, Version 2.0][28]              |
| [Apache Maven Javadoc Plugin][38]                       | [Apache License, Version 2.0][28]              |
| [Nexus Staging Maven Plugin][39]                        | [Eclipse Public License][40]                   |
| [Lombok Maven Plugin][41]                               | [The MIT License][1]                           |
| [JaCoCo :: Maven Plugin][42]                            | [Eclipse Public License 2.0][43]               |
| [error-code-crawler-maven-plugin][44]                   | [MIT License][45]                              |
| [Reproducible Build Maven Plugin][46]                   | [Apache 2.0][31]                               |
| [Project keeper maven plugin][47]                       | [The MIT License][48]                          |
| [Apache Maven JAR Plugin][49]                           | [Apache License, Version 2.0][28]              |
| [Maven Clean Plugin][50]                                | [The Apache Software License, Version 2.0][31] |
| [Maven Resources Plugin][51]                            | [The Apache Software License, Version 2.0][31] |
| [Maven Install Plugin][52]                              | [The Apache Software License, Version 2.0][31] |
| [Maven Site Plugin 3][53]                               | [The Apache Software License, Version 2.0][31] |

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
[10]: https://projectlombok.org
[11]: https://projectlombok.org/LICENSE
[12]: http://hamcrest.org/JavaHamcrest/
[13]: http://opensource.org/licenses/BSD-3-Clause
[14]: https://junit.org/junit5/
[15]: https://www.eclipse.org/legal/epl-v20.html
[16]: https://github.com/mockito/mockito
[17]: https://github.com/mockito/mockito/blob/main/LICENSE
[18]: https://github.com/exasol/hamcrest-resultset-matcher/
[19]: https://github.com/exasol/hamcrest-resultset-matcher/blob/main/LICENSE
[20]: https://github.com/exasol/udf-debugging-java/
[21]: https://github.com/exasol/exasol-test-setup-abstraction-java/
[22]: https://github.com/exasol/exasol-test-setup-abstraction-java/blob/main/LICENSE
[23]: https://github.com/exasol/performance-test-recorder-java/
[24]: https://github.com/exasol/performance-test-recorder-java/blob/main/LICENSE
[25]: http://sonarsource.github.io/sonar-scanner-maven/
[26]: http://www.gnu.org/licenses/lgpl.txt
[27]: https://maven.apache.org/plugins/maven-compiler-plugin/
[28]: https://www.apache.org/licenses/LICENSE-2.0.txt
[29]: https://maven.apache.org/enforcer/maven-enforcer-plugin/
[30]: https://www.mojohaus.org/flatten-maven-plugin/
[31]: http://www.apache.org/licenses/LICENSE-2.0.txt
[32]: https://sonatype.github.io/ossindex-maven/maven-plugin/
[33]: https://maven.apache.org/surefire/maven-surefire-plugin/
[34]: http://www.mojohaus.org/versions-maven-plugin/
[35]: https://maven.apache.org/plugins/maven-deploy-plugin/
[36]: https://maven.apache.org/plugins/maven-gpg-plugin/
[37]: https://maven.apache.org/plugins/maven-source-plugin/
[38]: https://maven.apache.org/plugins/maven-javadoc-plugin/
[39]: http://www.sonatype.com/public-parent/nexus-maven-plugins/nexus-staging/nexus-staging-maven-plugin/
[40]: http://www.eclipse.org/legal/epl-v10.html
[41]: https://anthonywhitford.com/lombok.maven/lombok-maven-plugin/
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
