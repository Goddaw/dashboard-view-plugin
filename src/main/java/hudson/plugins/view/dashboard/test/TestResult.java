package hudson.plugins.view.dashboard.test;

import hudson.model.Job;

public class TestResult {

   private Job job;
   protected int tests;
   protected int success;
   protected int failed;
   protected int skipped;

   public TestResult(Job job, int tests, int failed, int skipped) {
      super();
      this.job = job;
      this.tests = tests;
      this.failed = failed;
      this.skipped = skipped;

      this.success = tests - failed - skipped;
   }

   public Job getJob() {
      return job;
   }

   public int getTests() {
      return tests;
   }

   public int getSuccess() {
      return success;
   }

   public double getSuccessPct() {
      return getTests() != 0 ? ((double) getSuccess() / getTests()) : 0d;
   }

   public int getFailed() {
      return failed;
   }

   public double getFailedPct() {
      return getTests() != 0 ? ((double) getFailed() / getTests()) : 0d;
   }

   public int getSkipped() {
      return skipped;
   }

   public double getSkippedPct() {
      return getTests() != 0 ? ((double) getSkipped() / getTests()) : 0d;
   }
}
