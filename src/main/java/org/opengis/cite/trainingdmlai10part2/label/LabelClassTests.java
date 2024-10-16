package org.opengis.cite.trainingdmlai10part2.label;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Iterator;
import java.util.Set;

import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;

import org.opengis.cite.trainingdmlai10part2.BaseJsonSchemaValidatorTest;
import org.opengis.cite.trainingdmlai10part2.CommonFixture;
import org.opengis.cite.trainingdmlai10part2.ErrorMessage;
import org.opengis.cite.trainingdmlai10part2.ErrorMessageKeys;
import org.opengis.cite.trainingdmlai10part2.SuiteAttribute;
import org.opengis.cite.validation.RelaxNGValidator;
import org.opengis.cite.validation.ValidationErrorHandler;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.SkipException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.fasterxml.jackson.databind.JsonNode;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.ValidationMessage;

/**
 * Includes various tests of capability 1.
 */
public class LabelClassTests extends CommonFixture {

    private File testSubject;

    /**
     * Obtains the test subject from the ISuite context. The suite attribute
     * {@link org.opengis.cite.trainingdmlai10part2.SuiteAttribute#TEST_SUBJECT}
     * should evaluate to a DOM Document node.
     *
     * @param testContext The test (group) context.
     */
    @BeforeClass
    public void obtainTestSubject(ITestContext testContext) {
        Object obj = testContext.getSuite().getAttribute(SuiteAttribute.TEST_SUBJECT.getName());
        this.testSubject = (File) obj;
    }

    /**
     * Sets the test subject. This method is intended to facilitate unit testing.
     *
     * @param testSubject A Document node representing the test subject or metadata
     *                    about it.
     */
    public void setTestSubject(File testSubject) {
        this.testSubject = testSubject;
    }

    /**
     * Abstract Test 19: Verify that instance documents using the AI_Label JSON objects listed in Table 9 validate against the JSON schema specified in <a href="http://schemas.opengis.net/trainingdml-ai/1.0/ai_label.json">ai_label.json</a>.
     * <br/><br/>
     * Abstract Test 20: Verify that instance documents using the AI_SceneLabel JSON objects listed in Table 10 validate against the JSON schema specified in <a href="http://schemas.opengis.net/trainingdml-ai/1.0/ai_sceneLabel.json">ai_sceneLabel.json</a>.
     * <br/><br/>
     * Abstract Test 21: Verify that instance documents using the AI_ObjectLabel JSON objects listed in Table 11 validate against the JSON schema specified in <a href="http://schemas.opengis.net/trainingdml-ai/1.0/ai_objectLabel.json">ai_objectLabel.json</a>.
     * <br/><br/>
     * Abstract Test 22: Verify that instance documents using the AI_PixelLabel JSON objects listed in Table 12 validate against the JSON schema specified in <a href="http://schemas.opengis.net/trainingdml-ai/1.0/ai_pixelLabel.json">ai_pixelLabel.json</a>.
     */
    @Test(description = "Implements AI Label - TBA")
    public void validateByLabelSchema() {

        if (!testSubject.isFile()) {
            Assert.assertTrue(testSubject.isFile(), "No file selected. ");
        }

        StringBuffer sb = new StringBuffer();
        boolean foundAtLeastOneLabel = false;

        try {
            boolean valid = false;
            BaseJsonSchemaValidatorTest tester = new BaseJsonSchemaValidatorTest();
            JsonNode node = tester.getJsonNodeFromStringContent(
                    tester.otherConvertInputStreamToString(new FileInputStream(testSubject)));

            String arrayToFetch = "data";

            //-----Confirm that there is at least one label-----------

            if (node.get(arrayToFetch).size() > 0) {
                for (int i = 0; i < node.get(arrayToFetch).size(); i++) {
                    if (node.get(arrayToFetch).get(i).getClass().toString()
                            .endsWith("com.fasterxml.jackson.databind.node.ObjectNode")) {
                        if (node.get(arrayToFetch).get(i).get("labels").size() > 0) {
                            foundAtLeastOneLabel = true;
                        }
                    }
                }
            }
            //----------------

            if (node.get(arrayToFetch).size() > 0) {
                for (int i = 0; i < node.get(arrayToFetch).size(); i++) {
                    if (!(node.get(arrayToFetch).get(i).getClass().toString()
                            .endsWith("com.fasterxml.jackson.databind.node.ObjectNode"))) {
                        continue;
                    }
                    for (int indexToFetch = 0; indexToFetch < node.get(arrayToFetch).get(i).get("labels")
                            .size(); indexToFetch++) {
                        String labelType = node.get(arrayToFetch).get(i).get("labels").get(indexToFetch).get("type").asText();
                        String schemaToApply = null;

                        if (labelType.equals("AI_SceneLabel")) {
                            schemaToApply = "/org/opengis/cite/trainingdmlai10part2/jsonschema/ai_sceneLabel.json";
                        } else if (labelType.equals("AI_ObjectLabel")) {
                            schemaToApply = "/org/opengis/cite/trainingdmlai10part2/jsonschema/ai_objectLabel.json";
                        } else if (labelType.equals("AI_PixelLabel")) {
                            schemaToApply = "/org/opengis/cite/trainingdmlai10part2/jsonschema/ai_pixelLabel.json";
                        } else if (labelType.equals("AI_AbstractLabel")) {
                            schemaToApply = "/org/opengis/cite/trainingdmlai10part2/jsonschema/ai_label.json";
                        } else {
                            Assert.fail("Label at data item " + i + " did not have a recognized type.");
                        }

                        InputStream inputStream = tester.getClass().getResourceAsStream(schemaToApply);

                        JsonNode schemaNode = tester
                                .getJsonNodeFromStringContent(tester.otherConvertInputStreamToString(inputStream));
                        JsonSchema schema = tester.getJsonSchemaFromJsonNodeAutomaticVersion(schemaNode);

                        schema.initializeValidators();
                        Set<ValidationMessage> errors =
                                schema.validate(node.get(arrayToFetch).get(i).get("labels").get(indexToFetch));

                        Iterator it = errors.iterator();
                        while (it.hasNext()) {
                            sb.append("Item " + indexToFetch + " has error " + it.next() + ".\n");
                        }
                    }
                }
            }
        } catch (Exception e) {
            sb.append(e.getMessage());
            e.printStackTrace();
        }

        if (foundAtLeastOneLabel) {
            Assert.assertTrue(sb.toString().length() == 0, sb.toString());
        } else {
            Assert.fail("None of the data items contained labels.");
        }
    }

    /**
     * Verify that instance documents using the AI_ImageFormatCode JSON objects conform to the requirements specified by the Multipurpose Internet Mail Extensions (MIME) Part Two: Media Types <a href="https://www.ietf.org/rfc/rfc2046.txt">[RFC 2046]</a>.
     **/
    @Test(description = "Implements Abstract Test 23 (/req/ailabel/imageformatcode)")
    public void validateByImageFormatCode() {
        throw new SkipException("Not implemented yet.");
    }
}
