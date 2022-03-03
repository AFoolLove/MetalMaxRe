package me.afoolslove.metalmaxre.excel;

import me.afoolslove.metalmaxre.editor.EditorManager;
import me.afoolslove.metalmaxre.editor.items.ItemsEditor;
import me.afoolslove.metalmaxre.editor.text.TextEditor;
import me.afoolslove.metalmaxre.editor.text.TextParagraphs;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class Excel {
    private Excel() {
    }

    /**
     * 导出数据到Excel文件
     */
    public static byte[] exportExcel() {
        TextEditor textEditor = EditorManager.getEditor(TextEditor.class);
        ItemsEditor itemsEditor = EditorManager.getEditor(ItemsEditor.class);

        Workbook workbook = new HSSFWorkbook();
        // 创建文本表
        Sheet textSheet = workbook.createSheet("Text");
        do {
            Row textSheetRow = textSheet.createRow(0);
            int i = 0;
            // 创建 文本地址 作为首数据
            for (Map.Entry<Integer, Integer> entry : TextEditor.POINTS.entrySet()) {
                textSheetRow.createCell(i).setCellValue(String.format("%05X-%05X", entry.getKey(), entry.getValue()));
                i++;
            }

            i = 0;
            for (Map.Entry<Integer, TextParagraphs> entry : textEditor.getParagraphsMap().entrySet()) {
                for (int j = 0; j < entry.getValue().size(); j++) {
                    textSheetRow = textSheet.getRow(j + 1);
                    if (textSheetRow == null) {
                        textSheetRow = textSheet.createRow(j + 1);
                    }
                    textSheetRow.createCell(i).setCellValue(entry.getValue().get(j));
                }
                i++;
            }
        } while (false);

        // 坦克装备
        Sheet tankEquipmentSheet = workbook.createSheet("TankEquipments");
        do {
            Row tankEquipmentSheetRow = tankEquipmentSheet.createRow(0);
            int i = 0;
            // 设置坦克装备表标题
            String[] titles = {"名称", "重量", "攻击力/载重", "防御力"};
            for (i = 0; i < titles.length; i++) {
                tankEquipmentSheetRow.createCell(i).setCellValue(titles[i]);
            }

            i = 0;

        } while (false);


        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            workbook.write(byteArrayOutputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return byteArrayOutputStream.toByteArray();
    }

    /**
     * 导入Excel的数据
     */
    public static void importExcel(@NotNull InputStream inputStream) throws IOException {
        TextEditor textEditor = EditorManager.getEditor(TextEditor.class);

        HSSFWorkbook sheets = new HSSFWorkbook(inputStream);
        HSSFSheet textSheet = sheets.getSheet("Text");
        HSSFRow textSheetRow = textSheet.getRow(0);

        Map<Integer, Integer> textIndexes = new LinkedHashMap<>();

        Iterator<Cell> iterator = textSheetRow.iterator();
        for (int i = 0; iterator.hasNext(); i++) {
            Cell cell = iterator.next();
            String[] split = cell.getStringCellValue().split("-");
            textIndexes.put(Integer.parseInt(split[0], 16), i);
        }
        for (int i = 1; true; i++) {
            HSSFRow row = textSheet.getRow(i);
            if (row != null) {
                for (Map.Entry<Integer, Integer> entry : textIndexes.entrySet()) {
                    TextParagraphs textParagraphs = textEditor.getParagraphsMap().get(entry.getKey());

                    HSSFCell cell = row.getCell(entry.getValue());
                    textParagraphs.set(i, cell.getStringCellValue());
                }
                continue;
            }
            break;
        }
    }
}
