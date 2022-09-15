package com.ydw.oa.wkflow.util.watermark.word;


import com.spire.doc.*;
import com.spire.doc.documents.WatermarkLayout;

import com.spire.doc.FileFormat;
import com.spire.doc.TextWatermark;
import com.spire.doc.documents.WatermarkLayout;

import java.awt.*;

public class WordTextWatermark {
    public static void main(String[] args) {
        addWaterMark("D:test.doc","BGYP-2021-0001");
    }
    public static void addWaterMark(String filePath,String text) {
        Document document = new Document();
        document.loadFromFile(filePath);
        insertTextWatermark(document.getSections().get(0),text);
        document.saveToFile(filePath, FileFormat.Docx);
    }
    private static void insertTextWatermark(Section section,String text) {
        TextWatermark txtWatermark = new TextWatermark();
        txtWatermark.setText(text);
        txtWatermark.setFontSize(40);
        txtWatermark.setColor(Color.GRAY);
        txtWatermark.setLayout(WatermarkLayout.Diagonal);
        section.getDocument().setWatermark(txtWatermark);
    }
}
