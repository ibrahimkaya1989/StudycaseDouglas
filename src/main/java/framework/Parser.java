package framework;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Parser {
    private final Logger logger = LogManager.getLogger(this.getClass().getName());
    JSONParser parser = new JSONParser();
    static String json = Paths.get("").toAbsolutePath() + File.separator + "src" +
            File.separator + "test" + File.separator + "resources" + File.separator + "elements";

    public List<String> isPageExist(String myPage) {
        List<String> returnValue = new ArrayList<>();
        //index 0 da pageName index 1 de page waitElement
        JSONObject object = null;
        try {
            object = (JSONObject) parser.parse(new FileReader(json + File.separator + myPage + ".json"));
            logger.debug(myPage + ".json file found.");
        } catch (IOException | ParseException e) {
            e.printStackTrace();
            logger.error(myPage + ".json file NOT found.");
        }
        JSONArray array;
        try {
            array = (JSONArray) object.get("pages");
        } catch (Exception e) {
            array = new JSONArray();
        }

        for (Object o : array) {
            JSONObject page = (JSONObject) o;

            JSONObject pageInfo = (JSONObject) page.get("pageInfo");
            String pagename = (String) pageInfo.get("pageName");
            String waitelement = (String) pageInfo.get("waitelement");
            if (pagename.equalsIgnoreCase(myPage)) {
                logger.debug(pagename + " parameter is found in " + myPage + ".json file!");
                returnValue.add(pagename);
                returnValue.add(waitelement);
                return returnValue;
            }
        }
        logger.error(myPage + " parameter is NOT found in the JSON file!");
        return returnValue;
    }

    public String getElement(String myPage, String myElement) {
        JSONObject object = null;
        try {
            object = (JSONObject) parser.parse(new FileReader(json + File.separator + myPage + ".json"));
        } catch (IOException | ParseException e) {
            e.printStackTrace();
            logger.error(myPage + ".json file NOT found.");
        }

        JSONArray array = (JSONArray) object.get("pages");
        String value = null;
        String parentName = "";
        for (Object o : array) {
            JSONObject page = (JSONObject) o;

            JSONObject pageInfo = (JSONObject) page.get("pageInfo");
            String pagename = (String) pageInfo.get("pageName");
            parentName = (String) pageInfo.get("parent");
            if (pagename.equalsIgnoreCase(myPage)) {

                JSONArray elements = (JSONArray) page.get("elements");
                for (Object element : elements) {
                    JSONObject elem = (JSONObject) element;
                    value = (String) elem.get(myElement);

                    if (value != null) {
                        break;
                    }
                }

                // in case of element not found in page it controls parent page
                if (value == null) {
                    logger.debug(myElement + " is NOT found in " + myPage + ".json file!");
                    try {
                        object = (JSONObject) parser.parse(new FileReader(json + File.separator + parentName + ".json"));
                        array = (JSONArray) object.get("pages");
                        logger.debug(parentName + ".json file found.");
                    } catch (IOException | ParseException e) {
                        e.printStackTrace();
                        logger.error(parentName + ".json file NOT found.");
                    }
                    for (Object obj : array) {
                        JSONObject parentPage = (JSONObject) obj;
                        pageInfo = (JSONObject) parentPage.get("pageInfo");
                        pagename = (String) pageInfo.get("pageName");

                        if (pagename.equalsIgnoreCase(parentName)) {
                            JSONArray parenEelements = (JSONArray) parentPage.get("elements");
                            for (Object element : parenEelements) {
                                JSONObject elem = (JSONObject) element;
                                value = (String) elem.get(myElement);

                                if (value != null) {
                                    break;
                                }
                            }
                            if (value != null) {
                                break;
                            }
                        }
                    }
                }
            }
            if (value != null) {
                break;
            }
        }

        return value;
    }
}
