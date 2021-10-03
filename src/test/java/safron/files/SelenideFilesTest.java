package safron.files;

import com.codeborne.pdftest.PDF;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.xlstest.XLS;
import com.opencsv.CSVReader;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.URL;
import java.util.List;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static org.assertj.core.api.Assertions.assertThat;


public class SelenideFilesTest {

    @Test
    void downloadFileTest() throws Exception {
        Selenide.open("https://github.com/selenide/selenide/blob/master/README.md");
        File download = $("#raw-url").download();
        String result;
        try (InputStream is = new FileInputStream(download)) {
           result = new String(is.readAllBytes(), "UTF-8");
       }
       assertThat(result).contains("Selenide = UI Testing Framework powered by Selenium WebDriver");

    }

    @Test
    void uploadFilesTest() {
        Selenide.open("https://the-internet.herokuapp.com/upload");
        $("input[type='file']").uploadFromClasspath("example.txt");
        $("#file-submit").click();
        $("#uploaded-files").shouldHave(Condition.text("example.txt"));

    }

    @Test
    void downloadPdfTest() throws Exception {
        Selenide.open("https://junit.org/junit5/docs/current/user-guide/");
        File download = $(byText("PDF download")).download();
        PDF parsed = new PDF(download);
        assertThat(parsed.author).contains("Marc Philipp");
    }

    @Test
    void downloadExcelTest() throws Exception {
        try (InputStream stream = getClass().getClassLoader().getResourceAsStream("TORG-12.xlsx")) {
            XLS parsed = new XLS(stream);
            assertThat(parsed.excel.getSheetAt(0).getRow(7).getCell(1).getStringCellValue()).isEqualTo("Грузополучатель");
        }

    }

    @Test
    void parseCsvTest() throws Exception {
        URL url = getClass().getClassLoader().getResource("file.csv");
        CSVReader reader = new CSVReader(new FileReader(new File(url.toURI())));

        List<String[]> strings = reader.readAll();

        assertThat(strings).contains(
               new String[] {"lector", "lecture"},
               new String[] {"Safronova", "Yepta"},
               new String[] {"Kto-to", "Chto-to"}
        );
    }

    @Test
    void parseZipFileTest() throws Exception {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("171.zip")) {

            ZipInputStream zis = new ZipInputStream(is);
            ZipEntry entry;

            while((entry = zis.getNextEntry()) != null) {
                System.out.println(entry.getName());
            }

           // Scanner sc = new Scanner(zis);
           // while (sc.hasNext()) {
           //     System.out.println(sc.nextLine());
           // }
        }

    }

}
