/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.gob.sat.sat_ifi_sipf.utils;

import gt.gob.sat.sat_ifi_sipf.constants.Constants;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.tika.config.TikaConfig;
import org.apache.tika.exception.TikaException;
import org.apache.tika.io.TikaInputStream;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MediaType;
import org.apache.tika.mime.MimeTypes;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author crramosl
 */
public class FileUtils {

    public static boolean isValidImage(InputStream is) {
        try {
            TikaConfig tika = new TikaConfig();
            MediaType type = tika.getDetector().detect(
                    TikaInputStream.get(is),
                    new Metadata()
            );

            return Constants.VALID_TYPES.contains(type.toString());
        } catch (TikaException | IOException ex) {
            return false;
        }
    }

    public static MultipartFile renameTo(MultipartFile file, String newName) throws IOException {
        return new MockMultipartFile(newName, newName, file.getContentType(), file.getResource().getInputStream());
    }

    public static String getExtension(String type) {
        try {
            return MimeTypes.getDefaultMimeTypes().forName(type).getExtension();
        } catch (TikaException ex) {
            return null;
        }
    }

    public static String getType(InputStream is) {
        try {
            TikaConfig tika = new TikaConfig();
            MediaType type = tika.getDetector().detect(
                    TikaInputStream.get(is),
                    new Metadata()
            );

            return type.toString();
        } catch (TikaException | IOException ex) {
            return null;
        }
    }

    public static Workbook getWorkbook(String type) {
        Workbook workbook = null;

        switch (type) {
            case "xlsx":
                workbook = new XSSFWorkbook();
                break;
            case "xls":
                workbook = new HSSFWorkbook();
                break;
        }

        return workbook;
    }

    public static Sheet createHeader(final Sheet sheet, List<CellProperty> cells) {
        Row row = sheet.createRow(0);

        for (int i = 0; i < cells.size(); i++) {
            cells.get(i).stylizeCell(sheet, row, i);
        }

        return sheet;
    }

    public static Cell createCell(Sheet sheet, Row row, CellProperty cell) {
        return cell.stylizeCell(sheet, row, row.getPhysicalNumberOfCells());
    }
}
