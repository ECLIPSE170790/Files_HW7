package safron.files;

import com.codeborne.pdftest.PDF;
import com.codeborne.xlstest.XLS;
import net.lingala.zip4j.ZipFile;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static com.codeborne.pdftest.assertj.Assertions.assertThat;

public class Home_Work {

    @Test
    void parseTxtFileTest() throws Exception {
        String result;
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("Txt.txt")) {
            result = new String(is.readAllBytes(), StandardCharsets.UTF_8);
        }
        assertThat(result).contains("dskfklsdjfksdjkfjsdkfjsdkfsdfsdvcxvbdfhgdhdfh");
    }

    @Test
    void parsePdfFileTest() throws Exception {
        PDF parsed = new PDF(getClass().getClassLoader().getResourceAsStream("Pdf.pdf"));
        assertThat(parsed.creator).contains("Microsoft® Word 2010");
        assertThat(parsed.numberOfPages).isEqualTo(1);
        assertThat(parsed.text).contains("XPath");
        //assertThat(parsed.creationDate).isEqualTo("2021-04-22"); спросила в чате, как проверить дату
    }

    @Test
    void parseExcelFileTest() throws Exception {
        XLS parsed = new XLS (getClass().getClassLoader().getResourceAsStream("Excel.xlsx"));
        assertThat(parsed.excel.getSheetAt(0).getRow(2).getCell(0).getStringCellValue()).isEqualTo("ttt");
        assertThat(parsed.excel.getSheetAt(0).getRow(3).getCell(0).getStringCellValue()).isNotNull();
        //спросить как быть с числовым значением, Idea орёт, что там текст, а не 0.
    }

    @Test
    void parseZipFilePassTest() throws Exception {
        ZipFile zipFile = new ZipFile("./src/test/resources/Files_pass.zip");

        if (zipFile.isEncrypted())
            zipFile.setPassword("pas".toCharArray());

        zipFile.extractAll("./src/test/resources/");

        try (FileInputStream stream = new FileInputStream("./src/test/resources/Txt.txt")) {
            String result = new String(stream.readAllBytes(), "UTF-8");
            assertThat(result).contains("dskfklsdjfksdjkfjsdkfjsdkfsdfsdvcxvbdfhgdhdfh");
        }
    }

    @Test
    void parseWordFileTest() throws Exception {
        String text;

        try (InputStream stream = getClass().getClassLoader().getResourceAsStream("Word.docx")) {
            WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.load(stream);
            text = wordMLPackage.getMainDocumentPart().getContent().toString();
        }
        assertThat(text).contains("dsfsdfsdfsdfsdfsdfkljdskjfkdsjkfds");
    }

}
