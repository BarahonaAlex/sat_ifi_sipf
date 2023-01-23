/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.gob.sat.sat_ifi_sipf.utils;

import java.awt.Color;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;

/**
 *
 * @author crramosl
 */
@Builder
@Getter
@Setter
public class CellProperty {

    private String title;
    private XSSFColor color;
    private boolean autoSizeColumn;
    private boolean bold;
    private Short fontSize;
    private BorderStyle borders[];
    private boolean allBorders;
    private boolean wrapText;
    private Short width;
    private VerticalAlignment verticalAlignment;
    private HorizontalAlignment horizontalAlignment;
    private boolean centered;

    public Cell stylizeCell(final Sheet sheet, final Row row, int rowCount) {
        initRequiredFields();
        XSSFCellStyle cellStyle = (XSSFCellStyle) sheet.getWorkbook().createCellStyle();
        Font font = sheet.getWorkbook().createFont();
        font.setFontHeightInPoints(fontSize);
        font.setBold(bold);
        cellStyle.setFont(font);
        cellStyle.setFillForegroundColor(color);
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cellStyle.setWrapText(wrapText);
        cellStyle.setVerticalAlignment(verticalAlignment);
        cellStyle.setAlignment(horizontalAlignment);

        applyBoders(cellStyle);

        Cell cell = row.createCell(rowCount);
        cell.setCellStyle(cellStyle);
        cell.setCellValue(title);

        sheet.setColumnWidth(rowCount, (width == null ? 11 : width) * 256);
        if (autoSizeColumn) {
            sheet.autoSizeColumn(rowCount);
        }
        return cell;
    }

    private void initRequiredFields() {
        verticalAlignment = VerticalAlignment.BOTTOM;
        horizontalAlignment = HorizontalAlignment.LEFT;
        wrapText = true;
        
        if (color == null) {
            color = new XSSFColor(Color.WHITE, null);
        }
        if (fontSize == null) {
            fontSize = 11;
        }
        if (borders == null) {
            borders = new BorderStyle[]{BorderStyle.NONE, BorderStyle.NONE, BorderStyle.NONE, BorderStyle.NONE};
        }
        if (allBorders) {
            borders = new BorderStyle[]{BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN, BorderStyle.THIN};
        }
        if (centered) {
            verticalAlignment = VerticalAlignment.CENTER;
            horizontalAlignment = HorizontalAlignment.CENTER;
        }
    }

    private void applyBoders(XSSFCellStyle style) {
        style.setBorderTop(borders[0]);
        style.setBorderBottom(borders[1]);
        style.setBorderLeft(borders[2]);
        style.setBorderRight(borders[3]);
    }
}
