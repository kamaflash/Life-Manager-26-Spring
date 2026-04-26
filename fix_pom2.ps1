$path = 'pom.xml'
$text = Get-Content $path -Raw
$buildStart = $text.IndexOf('<build>')
if ($buildStart -lt 0) { throw 'No <build> found'; }
$marker = '<!-- ===================== -->'
$firstMarker = $text.IndexOf($marker, $buildStart)
$secondMarker = $text.IndexOf($marker, $firstMarker + $marker.Length)
if ($secondMarker -lt 0) { throw 'Second marker not found'; }
$prefix = $text.Substring(0, $buildStart)
$suffix = $text.Substring($secondMarker)
$newBuild = @'
    <!-- ===================== -->
    <!-- ===== BUILD ========= -->
    <!-- ===================== -->
    <build>
        <pluginManagement>
            <plugins>
                <plugin>
                    <groupId>org.springframework.boot</groupId>
                    <artifactId>spring-boot-maven-plugin</artifactId>
                    <version>${spring-boot.version}</version>
                    <executions>
                        <execution>
                            <goals>
                                <goal>repackage</goal>
                            </goals>
                        </execution>
                    </executions>
                </plugin>
            </plugins>
        </pluginManagement>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>3.11.0</version>
                <configuration>
                    <source>${maven.compiler.source}</source>
                    <target>${maven.compiler.target}</target>
                    <annotationProcessorPaths>
                        <path>
                            <groupId>org.projectlombok</groupId>
                            <artifactId>lombok</artifactId>
                            <version>${projectlombok.version}</version>
                        </path>
                        <path>
                            <groupId>org.mapstruct</groupId>
                            <artifactId>mapstruct-processor</artifactId>
                            <version>${mapstruct.version}</version>
                        </path>
                    </annotationProcessorPaths>
                </configuration>
            </plugin>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-surefire-plugin</artifactId>
                <version>3.2.5</version>
            </plugin>
            <plugin>
                <groupId>com.google.cloud.tools</groupId>
                <artifactId>jib-maven-plugin</artifactId>
                <version>3.4.1</version>
            </plugin>
        </plugins>
    </build>
'@
Set-Content -Path $path -Value ($prefix + $newBuild + $suffix) -Encoding utf8
Write-Output 'Replaced entire build section successfully'
