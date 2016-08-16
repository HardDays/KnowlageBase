package ru.knowledgebase.convertermodule;

import org.apache.poi.xwpf.converter.core.FileImageExtractor;
import org.apache.poi.xwpf.converter.core.FileURIResolver;
import org.apache.poi.xwpf.converter.xhtml.XHTMLConverter;
import org.apache.poi.xwpf.converter.xhtml.XHTMLOptions;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFTable;

import java.io.*;
import java.math.BigInteger;
import java.util.List;

import org.openxmlformats.schemas.wordprocessingml.x2006.main.*;


/**
 * Created by vova on 13.08.16.
 */
public class ArticleConverter {

    private static volatile ArticleConverter instance;

    public static ArticleConverter getInstance() {
        ArticleConverter localInstance = instance;
        if (localInstance == null) {
            synchronized (ArticleConverter.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new ArticleConverter();
                }
            }
        }
        return localInstance;
    }

    public void convert(InputStream input) throws Exception{
        try {
            XWPFDocument document = new XWPFDocument(input);
            File outFile = new File("/home/vova/Project BZ/trash/docx/target/d.html");
            outFile.getParentFile().mkdirs();
            List<XWPFTable> tables = document.getTables();
            //add borders to tables
            for(XWPFTable t : tables){
                CTTblPr tblpro = t.getCTTbl().addNewTblPr();

                CTTblBorders borders = tblpro.addNewTblBorders();

                borders.addNewBottom().setVal(STBorder.SINGLE);
                CTBorder b = borders.getBottom();
                b.setSz(BigInteger.valueOf(8));

                borders.addNewLeft().setVal(STBorder.SINGLE);
                b = borders.getLeft();
                b.setSz(BigInteger.valueOf(8));

                borders.addNewRight().setVal(STBorder.SINGLE);
                b = borders.getRight();
                b.setSz(BigInteger.valueOf(8));

                borders.addNewTop().setVal(STBorder.SINGLE);
                b = borders.getTop();
                b.setSz(BigInteger.valueOf(8));

                borders.addNewInsideV().setVal(STBorder.SINGLE);

                tblpro.setTblBorders(borders);
                t.getCTTbl().setTblPr(tblpro);
            }
            //output
            OutputStream out = new FileOutputStream(outFile);
            XHTMLOptions options = XHTMLOptions.create().URIResolver(new FileURIResolver(new File("/home/vova/Project BZ/trash/docx/target")));
            options.setExtractor(new FileImageExtractor(new File("/home/vova/Project BZ/trash/docx/target")));

            XHTMLConverter.getInstance().convert(document,out,options);
        }
        catch (Throwable e) {
            throw new Exception();
        }

    }
}
