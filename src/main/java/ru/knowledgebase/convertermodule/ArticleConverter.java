package ru.knowledgebase.convertermodule;


import org.apache.poi.hwpf.HWPFDocumentCore;
import org.apache.poi.hwpf.converter.WordToHtmlConverter;
import org.apache.poi.hwpf.converter.WordToHtmlUtils;
import org.apache.poi.xwpf.converter.core.FileImageExtractor;
import org.apache.poi.xwpf.converter.xhtml.XHTMLConverter;
import org.apache.poi.xwpf.converter.xhtml.XHTMLOptions;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFTable;

import java.io.*;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;

import org.openxmlformats.schemas.wordprocessingml.x2006.main.*;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;

import javax.xml.transform.stream.StreamResult;
import java.util.Base64;
import org.apache.poi.hwpf.usermodel.Picture;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import ru.knowledgebase.articlemodule.ArticleController;
import ru.knowledgebase.configmodule.Configurations;
import ru.knowledgebase.exceptionmodule.converterexceptions.ConvertException;
import ru.knowledgebase.imagemodule.ImageController;
import ru.knowledgebase.modelsmodule.imagemodels.Image;


/**
 * Created by vova on 13.08.16.
 */
public class ArticleConverter {

    private ArticleController articleController = ArticleController.getInstance();
    private ImageController imageController = ImageController.getInstance();

    private String uploadPath = Configurations.getUploadPath();
    private String imageFolder = Configurations.getImageFolderPath();

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

    //kind of magic, need to get images
    private class InlineImageWordToHtmlConverter extends WordToHtmlConverter {

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
            sb.insert(0, "data:" + picture.getMimeType() + ";base64,");
            imgNode.setAttribute("src", sb.toString());
            currentBlock.appendChild(imgNode);
        }

    }
    /**
     * Convert doc to html
     * @param input - document
     * other params - need for article creation
     */
    public void convertDoc(InputStream input, String title,
                           int authorId, int parentArticle,
                           boolean isSection, Timestamp archiveTime,
                           Timestamp createDate, Timestamp updateDate) throws Exception{
        String body = null;
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

            body = stringWriter.toString();
        }
        catch (Throwable e) {
            throw new ConvertException();
        }

        articleController.addArticle(title, body, authorId, parentArticle, createDate, updateDate, archiveTime, isSection);
    }
    /**
     * Convert docx to html
     * @param input - document
     * other params - need for article creation
     */
    public void convertDocx(InputStream input, String title, int authorId, int parentArticle, Timestamp archiveTime,
                            Timestamp createDate, Timestamp updateDate, boolean isSection) throws Exception{
        String body = null;
        String curPath = uploadPath + title;
        List <String> images = new LinkedList<String>();
        try {
            XWPFDocument document = new XWPFDocument(input);

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
            //create folder for images
            File outFolder = new File(curPath);
            outFolder.getParentFile().mkdirs();
            //out images and body
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            XHTMLOptions options = XHTMLOptions.create();//.URIResolver(new FileURIResolver(new File("/home/vova/Project BZ/trash/docx/target")));
            options.setExtractor(new FileImageExtractor(new File(curPath)));
            XHTMLConverter.getInstance().convert(document, out, options);
            outFolder = new File(curPath + imageFolder);
            //upload images to db
            for (File file : outFolder.listFiles()) {
                if (file.isFile()) {
                    Image image = imageController.addImage(new Image(file.getAbsolutePath(), file.getName()));
                    images.add(image.getId());
                }
            }
            body = new String(out.toByteArray(), "UTF-8");
        } catch (Exception e) {
            throw new ConvertException();
        }

        articleController.addArticle(title, body, authorId, parentArticle, createDate, updateDate, archiveTime, isSection);
    }
}
