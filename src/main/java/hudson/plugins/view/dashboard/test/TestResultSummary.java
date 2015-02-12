package hudson.plugins.view.dashboard.test;

import hudson.tasks.junit.CaseResult;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TestResultSummary extends TestResult {

   private List<TestResult> testResults = new ArrayList<TestResult>();
   private Set<String> passedTests = new HashSet<String>();
   private Set<String> failedTests = new HashSet<String>();
   private Set<String> skippedTests = new HashSet<String>();
   

   public TestResultSummary() {
      super(null, 0, 0, 0);
   }

   public TestResultSummary addTestResult(TestResult testResult) {
      testResults.add(testResult);
      
      if(testResult instanceof JUnitTestResult)
      {
    	  JUnitTestResult jUnitTestResult = (JUnitTestResult) testResult;
    	  addUniqueTestResult(jUnitTestResult);
      } else {
	      tests += testResult.getTests();
	      success += testResult.getSuccess();
	      failed += testResult.getFailed();
	      skipped += testResult.getSkipped();
      }
	      
      return this;
   }

   public List<TestResult> getTestResults() {
      return testResults;
   }

   @Override
   public int getSuccess() {
	   return super.getSuccess() + passedTests.size();
   }

   @Override
   public int getFailed() {
	   return super.getFailed() + failedTests.size();
   }

   @Override
   public int getSkipped() {
	   return super.getSkipped() + skippedTests.size();
   }
   
	private void addUniqueTestResult(JUnitTestResult testResult) {
		  for(CaseResult testCase : testResult.getAllTests()) {
			  String testName = testCase.getFullName();
			  
			  // Skipped < Passed < Failed (where '<' means 'less important than')
			  if(testCase.isSkipped()) {
				  if(!passedTests.contains(testName) && !failedTests.contains(testCase)) {
					  skippedTests.add(testName);
				  }
			  } else if(testCase.isPassed()) {
				  if(!failedTests.contains(testName)) {
					  passedTests.add(testName);
				  }
				  skippedTests.remove(testName);
			  } else { // Failed
				  failedTests.add(testName);
				  passedTests.remove(testName);
				  skippedTests.remove(testName);
			  }
		  }
	}
}
