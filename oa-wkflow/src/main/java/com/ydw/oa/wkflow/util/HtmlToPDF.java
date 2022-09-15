package com.ydw.oa.wkflow.util;

import com.spire.pdf.graphics.PdfMargins;
import com.spire.pdf.htmlconverter.qt.HtmlConverter;
import com.spire.pdf.htmlconverter.qt.Size;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class HtmlToPDF {

    @Value("${wkfow.domain}")
    private String domain;
    @Value("${wkfow.plugin}")
    private String plugin;

    public static String URI;
//    public static final String PLUGIN_PATH = "/data/soft/plugins_linux";
//    public static final String PLUGIN_PATH = "D:\\plugins_linux";
    public static String PLUGIN_PATH;

    public static boolean convert(String fileName,String pid,String formId){
        String url = URI.concat("?pid=").concat(pid).concat("&formId=").concat(formId);
        HtmlConverter.setPluginPath(PLUGIN_PATH);
        HtmlConverter.convert(url, fileName, true, 20000, new Size(800f, 650f), new PdfMargins(0));
        return true;
    }

    @PostConstruct
    public void getData() {
        String path = "/oa-wkflow/resource/table/pdfModel.html";
        if (this.domain == null || "".equals(this.domain)) {
            URI = "http://localhost"+path;
        }else{
            URI = this.domain + path ;
        }

        if (this.plugin != null && !"".equals(this.plugin)) {
            PLUGIN_PATH = this.plugin;
        }else{
            PLUGIN_PATH = "D:\\plugins-windows-x64";
        }
    }


    public static void main(String[] args) {
        String fileName = "20201126130401.pdf";
        String pid = "347501";
        String formId = "7d286b02c02045c45d08c8bb43b45971";
        HtmlToPDF.convert(fileName,pid,formId);
                //定义需要转换的HTML
    }
}
