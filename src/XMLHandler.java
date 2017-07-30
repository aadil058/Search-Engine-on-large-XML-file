import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.Observable;

/**
 * @author Abhishek Vashisht 2015006
 * @author Md. Aadil 2015058
 */
public class XMLHandler extends DefaultHandler {
    private DocObject docObject;
    private DocElement docElement;
    private StringBuilder builder = new StringBuilder();

    public XMLHandler(DocObject docObject){
        super();
        this.docObject = docObject;
    }

    @Override
    public void startElement(String uri,String localName, String qName, Attributes attributes) throws SAXException{
        if(qName.equalsIgnoreCase("article") || qName.equalsIgnoreCase("inproceedings") || qName.equalsIgnoreCase("proceedings")
                || qName.equalsIgnoreCase("book") || qName.equalsIgnoreCase("incollection") || qName.equalsIgnoreCase("phdthesis")
                || qName.equalsIgnoreCase("mastersthesis") || qName.equalsIgnoreCase("www")){

            docObject.firstTag = qName;
            docObject.docElements = new ArrayList<>();

        }
        if(!qName.equalsIgnoreCase("ref") && !qName.equalsIgnoreCase("sub") && !qName.equalsIgnoreCase("sup")
                && !qName.equalsIgnoreCase("i") && !qName.equalsIgnoreCase("tt")){

            docElement = new DocElement(null,null);
            docElement.tag = qName;
            builder.setLength(0);

        }

    }

    @Override
    public void endElement(String uri,String localName, String qName) throws  SAXException{
        if(qName.equalsIgnoreCase("article") || qName.equalsIgnoreCase("inproceedings") || qName.equalsIgnoreCase("proceedings")
                || qName.equalsIgnoreCase("book") || qName.equalsIgnoreCase("incollection") || qName.equalsIgnoreCase("phdthesis")
                || qName.equalsIgnoreCase("mastersthesis") || qName.equalsIgnoreCase("www")){

            docObject.informChange();
            docObject.notifyObservers();

        }
        if(!qName.equalsIgnoreCase("ref") && !qName.equalsIgnoreCase("sub") && !qName.equalsIgnoreCase("sup")
                && !qName.equalsIgnoreCase("i") && !qName.equalsIgnoreCase("tt")){
            if(docElement!=null){
                docElement.data = builder.toString();
                docObject.docElements.add(docElement);
                docElement = null;
            }
        }


    }

    @Override
    public void characters(char ch[], int start, int length) throws SAXException{
        //if(docElement!=null){
            //docElement.data = String.valueOf(ch,start,length);
            builder.append(ch,start,length);

        //}

    }

    @Override
    public void endDocument() throws SAXException{
        docObject.informChange();
        docObject.notifyObservers(new Integer(1));
    }
}
