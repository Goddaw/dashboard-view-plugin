package hudson.plugins.view.dashboard.test;

import hudson.matrix.MatrixProject;
import hudson.maven.reporters.SurefireAggregatedReport;
import hudson.model.Job;
import hudson.model.Run;
import hudson.model.TopLevelItem;
import hudson.tasks.test.AbstractTestResultAction;
import hudson.tasks.test.TestResultProjectAction;

import java.util.Collection;

public class TestUtil {

   /**
    * Summarize the last test results from the passed set of jobs.  If a job
    * doesn't include any tests, add a 0 summary.
    *
    * @param jobs
    * @return
    */
   public static TestResultSummary getTestResultSummary(Collection<TopLevelItem> jobs) {
      TestResultSummary summary = new TestResultSummary();

      for (TopLevelItem item : jobs) {
         if (item instanceof MatrixProject) {
            MatrixProject mp = (MatrixProject) item;

            for (Job job : mp.getAllJobs()) {
               if (job != mp) { //getAllJobs includes the parent job too, so skip that
                  summarizeJob(job, summary);
               }
            }
         }
         else if (item instanceof Job) {
            Job job = (Job) item;
            summarizeJob(job, summary);
         }
      }

      return summary;
   }

   private static void summarizeJob(Job job, TestResultSummary summary) {
      boolean addBlank = true;
      TestResultProjectAction testResults = job.getAction(TestResultProjectAction.class);

      if (testResults != null) {
         AbstractTestResultAction tra = testResults.getLastTestResultAction();

         if (tra != null) {
            addBlank = false;
            summary.addTestResult(new TestResult(job, tra.getTotalCount(), tra.getFailCount(), tra.getSkipCount()));
         }
      } else {
         SurefireAggregatedReport surefireTestResults = job.getAction(SurefireAggregatedReport.class);
         if (surefireTestResults != null) {
            addBlank = false;
            summary.addTestResult(new TestResult(job, surefireTestResults.getTotalCount(), surefireTestResults.getFailCount(), surefireTestResults.getSkipCount()));
         }
      }

      if (addBlank) {
         summary.addTestResult(new TestResult(job, 0, 0, 0));
      }
   }

   public static TestResult getTestResult(Run run) {
	  TestResultAction tra = run.getAction(TestResultAction.class);
	  if(tra != null) {
		  hudson.tasks.junit.TestResult result = tra.getResult();
		  return new JUnitTestResult(run.getParent(), result);
	  }
	  
      AbstractTestResultAction atra = run.getAction(AbstractTestResultAction.class);
      if (atra != null) {
         return new TestResult(run.getParent(), atra.getTotalCount(), atra.getFailCount(), atra.getSkipCount());
      } 
      
      SurefireAggregatedReport surefireTestResults = run.getAction(SurefireAggregatedReport.class);
      if (surefireTestResults != null) {
         return new TestResult(run.getParent(), surefireTestResults.getTotalCount(), surefireTestResults.getFailCount(), surefireTestResults.getSkipCount());
      }
      
      return new TestResult(run.getParent(), 0, 0, 0);
   }
}
