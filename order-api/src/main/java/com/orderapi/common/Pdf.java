package com.orderapi.common;

import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: xueyalin
 * @description:
 * @date: 2023/05/12
 */
public class Pdf {
    private  static final String TEMP_PATH="C:\\Users\\Dell\\Desktop\\电子保单.pdf";
    private  static final String DEST="C:\\Users\\Dell\\Desktop\\dest.pdf";
    private  static final String FONT_PATH="C:\\Windows\\Fonts\\simsun.ttc,0";
    public static void main(String[] args) throws IOException {
        PdfDocument pdfDocument = new PdfDocument(new PdfReader(TEMP_PATH), new PdfWriter(DEST));

        PdfAcroForm form = PdfAcroForm.getAcroForm(pdfDocument, false);
        PdfFont font = PdfFontFactory.createFont(FONT_PATH);
        DeviceRgb color = new DeviceRgb(0, 0, 0);
        //文本填充
        HashMap<String, String> map = new HashMap<>();
        map.put("userName","xx");
        map.put("startDate","2023-5-12");
        map.put("type","身份证");
        map.put("number","xxxxx");
        map.put("price","190");
        for (Map.Entry<String, String> entry : map.entrySet()) {
            form.getFormFields().get(entry.getKey()).setValue(entry.getValue()).setColor(color).setFont(font);
        }
        //清除表单域
        form.flattenFields();
        pdfDocument.close();
    }
}
