package org.opengis.cite.trainingdmlai10part2.dataquality;

import java.io.IOException;
import java.net.URL;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;
import org.opengis.cite.trainingdmlai10part2.CommonFixture;
import org.opengis.cite.trainingdmlai10part2.ErrorMessage;
import org.opengis.cite.trainingdmlai10part2.ErrorMessageKeys;
import org.opengis.cite.trainingdmlai10part2.SuiteAttribute;
import org.opengis.cite.validation.RelaxNGValidator;
import org.opengis.cite.validation.ValidationErrorHandler;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * Includes various tests of capability 1.
 */
public class DataQualityClassTests extends CommonFixture {

    /**
     * Checks the behavior of the trim function.
     */
    @Test(description = "Implements ATC 1-2")
    public void trim() {
        String str = "  foo   ";
        Assert.assertTrue("foo".equals(str.trim()));
    }


}
