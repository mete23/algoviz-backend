# algoviz-backend
## Jasypt
Für die Anbindung der Datenbank müssen die Zugangsdaten in application.properties Datei stehen. 
Da unser Repository öffentlich ist, müssen die verschlüsselt werden. Dafür benutzen wir das Tool Jasypt.

Für die Verschlüsselungen benutzt Jasypt einen Private Key. Diesen kann man in Environment Variablen speichern.
Diese kann man über IntelliJ konfigurieren:

1. Geht in die Klasse AlgovizApplication. Wenn ihr auf das Startsymbol rechtsklickt, öffnet sich ein Auswahlmenü, wo ihr <kbd>Edit Run Configarations..</kbd> auswählen müsst.
    
2. Dann öffnet sich folgende Seite.

  ![grafik](https://user-images.githubusercontent.com/100144468/210433710-62113852-478b-4da8-8558-5fcdf3fa18a2.png)

  Fügt in das Feld Environment Variables folgenden Asudruck ein:
    
    
```console
JASYPT_ENCRYPTOR_PASSWORD=secretKey
```  
   Den Secret Key schicke ich euch.
  
3. Drückt auf <kbd>Apply</kbd> und danach auf <kbd>Ok</kbd>

Damit habt ihr nur die Run Configuration in AlgovizApplication geändert. Für bereits bestehenden Tests müsst ihr diese auch ändern, falls ihr diese ausführen wollt. Für alle zukünftigen Tests ist im nächsten Abschnitt erklärt, wie das automatisiert passiert.


## JASYPT_ENCRYPTOR_PASSWORD für zukünftige Run Configurations einrichten

Damit ihr in Zukunft nicht bei jedem neuen Test die Run Configurations ändern müsst, müsst ihr noch das Configuration Template ändern:

1. Wählt in der horinzontalen Menüleiste Run | Edit Confihurations.. aus.

2. Dann öffnet sich folgende Seite

![grafik](https://user-images.githubusercontent.com/100144468/210436632-3f6c7f76-bc71-4f23-b4a5-ffefaf62f048.png)

Dort klickt ihr links oben auf <kbd>Gradle</kbd> und links unten auf <kbd>Edit configaration templates..</kbd>

3. Dann öffnet sich folgende Seite

![grafik](https://user-images.githubusercontent.com/100144468/210437021-13a9e5f7-395e-4e72-a24e-2e959855646f.png)

Achtet zuerst darauf, dass das Gradle Template ausgewählt ist.
Dann wählt ihr im Feld Gradle Projekt algoviz-backend aus.

Dann fügt ihr in das Feld Environment Variables folgenden Asudruck ein:
    
```console
JASYPT_ENCRYPTOR_PASSWORD=secretKey
```  
   Den Secret Key schicke ich euch.
   
4. Drückt auf <kbd>Apply</kbd> und danach auf <kbd>Ok</kbd>

## Testing with Postman

To run the tests with Postman, you need to import the collection and environment files. You can find them in the `postman` folder.
The automatic execution of the tests via the Script is only possible with the Postman CLI tool in linux.
If the exit code is 0, the tests were successful. If the exit code is n > 0, n tests failed.
You can see the results in the terminal.

1. Install Postman CLI tool

```console
curl -o- "https://dl-cli.pstmn.io/install/linux64.sh" | sh
```

2. Run the tests

Choose one of the test collections out of the text-data in the 'postman' folder.
The data musst start with the link to our postman account. In each following line is the
link to the collection and the environment file. The collection file must be the first link and the environment file the second link.
Run the following command in the terminal in the 'postman' directory to execute the tests.

```console
bash postmanScript.sh <collectionName.txt> 
```

Further information about the Postman CLI tool can be found [here](https://learning.postman.com/docs/running-collections/using-newman-cli/command-line-integration-with-newman/).

