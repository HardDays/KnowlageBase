package ru.knowledgebase.convertermodule;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.HWPFDocumentCore;
import org.apache.poi.hwpf.converter.WordToHtmlConverter;
import org.apache.poi.hwpf.converter.WordToHtmlUtils;
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

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;
import java.util.Base64;

import org.apache.poi.hwpf.converter.WordToHtmlConverter;
import org.apache.poi.hwpf.usermodel.Picture;
import org.w3c.dom.Document;
import org.w3c.dom.Element;


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


    public class InlineImageWordToHtmlConverter extends WordToHtmlConverter {

        public InlineImageWordToHtmlConverter(Document document) {
            super(document);
        }

        @Override
        protected void processImageWithoutPicturesManager(Element currentBlock,
                                                          boolean inlined, Picture picture)
        {
            Element imgNode = currentBlock.getOwnerDocument().createElement("img");
            StringBuilder sb = new StringBuilder();
            sb.append(Base64.getMimeEncoder().encodeToString(picture.getRawContent()));
            sb.insert(0, "data:"+picture.getMimeType()+";base64,");
            imgNode.setAttribute("src", sb.toString());
            currentBlock.appendChild(imgNode);
        }

    }

    public void convertDoc(InputStream input, String outputFile) throws Exception{
        try {
            HWPFDocumentCore wordDocument = WordToHtmlUtils.loadDoc(input);
                WordToHtmlConverter wordToHtmlConverter = new InlineImageWordToHtmlConverter(
                    DocumentBuilderFactory.newInstance().newDocumentBuilder()
                            .newDocument());
            wordToHtmlConverter.processDocument(wordDocument);

            StringWriter stringWriter = new StringWriter();
            Transformer transformer = TransformerFactory.newInstance().newTransformer();

            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.ENCODING, "utf-8");
            transformer.setOutputProperty(OutputKeys.METHOD, "html");
            transformer.transform(new DOMSource( wordToHtmlConverter.getDocument()), new StreamResult( stringWriter ) );

            String html = stringWriter.toString();

            FileOutputStream fos=new FileOutputStream(new File(outputFile));
            DataOutputStream dos;
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(fos,"UTF-8"));
            out.write(html);
            out.close();
        }
        catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public void convert(InputStream input, String outputFile) throws Exception{
        try {
            XWPFDocument document = new XWPFDocument(input);
            File outFile = new File(outputFile);
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
            e.printStackTrace();
            throw new Exception();
        }

    }
}
