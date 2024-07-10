package org.opengis.cite.trainingdmlai10part2.trainingdataset;

import java.io.IOException;
import java.net.URL;
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
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Iterator;
import java.util.Set;

import org.opengis.cite.validation.RelaxNGValidator;
import org.opengis.cite.validation.ValidationErrorHandler;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.SkipException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.SpecVersion;
import com.networknt.schema.SpecVersionDetector;
import com.networknt.schema.ValidationMessage;

/**
 * Includes various tests of capability 1.
 */
public class TrainingDatasetClassTests extends CommonFixture {

	private File testSubject;

    /**
     * Obtains the test subject from the ISuite context. The suite attribute
     * {@link org.opengis.cite.trainingdmlai10part2.SuiteAttribute#TEST_SUBJECT} should
     * evaluate to a DOM Document node.
     * 
     * @param testContext
     *            The test (group) context.
     */
    @BeforeClass
    public void obtainTestSubject(ITestContext testContext) {

        Object obj = testContext.getSuite().getAttribute(
        		SuiteAttribute.TEST_SUBJECT.getName());
  
        this.testSubject = (File) obj;        
        
    }

    /**
     * Sets the test subject. This method is intended to facilitate unit
     * testing.
     *
     * @param testSubject A Document node representing the test subject or
     * metadata about it.
     */
    public void setTestSubject(File testSubject) {
        this.testSubject = testSubject;
    }


    /**
     * Checks the behavior of the trim function.
     */
    @Test(description = "Implements AI TrainingDataset - TBA")
    public void validateByTrainingDatasetSchema() {
    
    	
    	if(!testSubject.isFile()) {
    		Assert.assertTrue(testSubject.isFile(),"No file selected. ");
    	}
    	
    	BaseJsonSchemaValidatorTest tester = new BaseJsonSchemaValidatorTest();
	      String schemaToApply = "/org/opengis/cite/trainingdmlai10part2/jsonschema/ai_eoTrainingDataset.json";
	  	
	        boolean valid = false;
	        StringBuffer sb = new StringBuffer();

	        InputStream inputStream = tester.getClass()
	                .getResourceAsStream(schemaToApply);
		
	        try {
		      JsonNode schemaNode = tester.getJsonNodeFromStringContent(tester.otherConvertInputStreamToString(inputStream));
		          JsonSchema schema = tester.getJsonSchemaFromJsonNodeAutomaticVersion(schemaNode);

		          schema.initializeValidators(); 
		          
		          JsonNode node = tester.getJsonNodeFromStringContent(tester.otherConvertInputStreamToString(new FileInputStream(testSubject)));
		          Set<ValidationMessage> errors = schema.validate(node);
		        
		
		        
				Iterator it = errors.iterator();
				while(it.hasNext())
				{
					sb.append(" "+it.next()+".\n");
	
				}
				
		} catch (IOException e) {
			System.out.println(e.getMessage());
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	        
	        System.out.println("CHK "+this.getClass().getName()+" RESULT "+sb.toString()+" = "+(sb.toString().length()==0));

	        Assert.assertTrue(sb.toString().length()==0,sb.toString());
    }


}
