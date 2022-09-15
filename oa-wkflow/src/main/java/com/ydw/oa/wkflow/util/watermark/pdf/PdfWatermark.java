package com.ydw.oa.wkflow.util.watermark.pdf;

import com.spire.ms.System.Collections.IEnumerator;
import com.spire.pdf.PdfPageBase;
import com.spire.pdf.graphics.*;
import com.spire.pdf.*;
import com.spire.pdf.widget.PdfPageCollection;

import java.awt.*;
import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 * @Title Textwatermark
 * @Description pdf添加水印
 * @Author ZhangKai
 * @Date 2021/06/21 22:47
 * @Version 1.0
 * @Email 410618538@qq.com
 */
public class PdfWatermark {
    public static void main(String[] args) {
//        //创建PdfDocument对象
//        PdfDocument pdf = new PdfDocument();
//
//        //加载示例文档
//        pdf.loadFromFile("D:/test.pdf");
//        //获取第一页
//        PdfPageCollection pages = pdf.getPages();
//        IEnumerator<PdfPageBase> iterator = pages.iterator();
//        while (iterator.hasNext()) {
//            PdfPageBase page = iterator.next();
//            //调用insertWatermark方法插入文本水印
//            insertWatermark(page, "BGYP-2021-0001");
//        }
//
//        //保存文档
//        pdf.saveToFile("D:/test.pdf");
        addWaterMark("D:\\test.pdf","BGYP-2021-0001");
    }

    public static void addWaterMark(String filePath,String text) {
        //创建PdfDocument对象
        PdfDocument pdf = new PdfDocument();

        //加载示例文档
        pdf.loadFromFile(filePath);
        //获取第一页
        PdfPageCollection pages = pdf.getPages();
        IEnumerator<PdfPageBase> iterator = pages.iterator();
        while (iterator.hasNext()) {
            PdfPageBase page = iterator.next();
            //调用insertWatermark方法插入文本水印
            insertWatermark(page, text);
        }
        
        // PdfPageBase pb = pdf.getPages().add(); //新增一页
        // pdf.getPages().remove(pb); //去除第一页水印

        //保存文档
        pdf.saveToFile(filePath);
    }

    private static void insertWatermark(PdfPageBase page, String watermark) {
        Dimension2D dimension2D = new Dimension();
        dimension2D.setSize(page.getCanvas().getClientSize().getWidth(), page.getCanvas().getClientSize().getHeight());
        PdfTilingBrush brush = new PdfTilingBrush(dimension2D);
        brush.getGraphics().setTransparency(0.3F);
        brush.getGraphics().save();
        brush.getGraphics().translateTransform((float) brush.getSize().getWidth() / 2, (float) brush.getSize().getHeight() / 2);
        brush.getGraphics().rotateTransform(-45);
        brush.getGraphics().drawString(watermark, new PdfFont(PdfFontFamily.Helvetica, 36), PdfBrushes.getViolet(), 0, 0, new PdfStringFormat(PdfTextAlignment.Center));
        brush.getGraphics().restore();
        brush.getGraphics().setTransparency(1);
        Rectangle2D loRect = new Rectangle2D.Float();
        loRect.setFrame(new Point2D.Float(0, 0), page.getCanvas().getClientSize());
        page.getCanvas().drawRectangle(brush, loRect);
    }
}
