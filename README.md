> :warning: **This project is no longer maintained.**  
> For tools related to RODA, please look at https://market.roda-community.org

keeps-characterization-ai
==========================

Characterization tool for ai files, made by KEEP SOLUTIONS.


## Build & Use

To build the application simply clone the project and execute the following Maven command: `mvn clean package`  
The binary will appear at `target/ai-characterization-tool-1.0-SNAPSHOT-jar-with-dependencies.jar`

To see the usage options execute the command:

```bash
$ java -jar target/ai-characterization-tool-1.0-SNAPSHOT-jar-with-dependencies.jar -h
usage: java -jar [jarFile]
 -f <arg> file to analyze
 -h       print this message
 -v       print this tool version
```

## Tool Output Example
```bash
<aiCharacterizationResult>
    <features>
        <item>
            <key>Artifacts.verbose</key>
            <value>true</value>
        </item>
        <item>
            <key>Background_color</key>
            <value>white</value>
        </item>
        <item>
            <key>Base_type</key>
            <value>ColorSeparation</value>
        </item>
        <item>
            <key>Border_color</key>
            <value>cmyk(223,223,223,0)</value>
        </item>
        ( .... )
    </features>
    <valid>true</valid>
</aiCharacterizationResult>


```

## License

This software is available under the [LGPL version 3 license](LICENSE).

