package hudson.plugins.view.dashboard.test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import hudson.model.Job;
import hudson.tasks.junit.CaseResult;
import hudson.tasks.junit.PackageResult;
import hudson.tasks.junit.SuiteResult;

public class JUnitTestResult extends TestResult {

	private final hudson.tasks.junit.TestResult testResult;
	
	public JUnitTestResult(Job job, hudson.tasks.junit.TestResult testResult) {
		super(job, testResult.getTotalCount(), testResult.getFailCount(), testResult.getSkipCount());
		this.testResult = testResult;
	}
	
	public List<CaseResult> getAllTests()
	{
		List<CaseResult> allTests = new ArrayList<CaseResult>(); 
		for (SuiteResult s : testResult.getSuites()) {
			allTests.addAll(s.getCases());
		}
		return allTests;
	}

}
