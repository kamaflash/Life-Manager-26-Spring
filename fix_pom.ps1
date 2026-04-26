$path = 'pom.xml'
$text = Get-Content $path -Raw
$start = $text.IndexOf('<!-- SPRING BOOT -->')
$end = $text.IndexOf('<!-- JIB', $start)
if ($start -lt 0 -or $end -lt 0) {
    throw 'Markers not found'
}
$prefix = $text.Substring(0, $start)
$suffix = $text.Substring($end)
$newBlock = @'
            <!-- SPRING BOOT -->
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

            <!-- TESTS -->
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-surefire-plugin</artifactId>
                <version>3.2.5</version>
            </plugin>

'@
$newText = $prefix + $newBlock + $suffix
Set-Content -Path $path -Value $newText -Encoding utf8
Write-Output 'Replaced block successfully'
