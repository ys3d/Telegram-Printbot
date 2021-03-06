package com.github.ys3d.printbot.printingJob;

import org.junit.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import javax.print.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.eq;

/**
 * Tests {@link PrintingJob}
 * @author Daniel Schild
 */
public class PrintingJobTest {

    @Test
    public void findPrintServiceEmptyResultTest() {
        try (MockedStatic<PrintServiceLookup> dummy = Mockito.mockStatic(PrintServiceLookup.class)) {
            dummy.when(() -> PrintServiceLookup.lookupPrintServices(null, null))
                    .thenReturn(new PrintService[0]);
            PrintingJob pj = new TestPrintingJob("PName");

            assertNull(pj.service);

            dummy.verify(() -> PrintServiceLookup.lookupPrintServices(eq(null), eq(null)));
        }
    }

    @Test
    public void findPrintServiceOnePrinterResultTest() {
        try (MockedStatic<PrintServiceLookup> dummy = Mockito.mockStatic(PrintServiceLookup.class)) {
            dummy.when(() -> PrintServiceLookup.lookupPrintServices(null, null))
                    .thenReturn(new PrintService[]{new TestPrintService("PName")});
            PrintingJob pj = new TestPrintingJob("PName");

            assertEquals(new TestPrintService("PName"), pj.service);

            dummy.verify(() -> PrintServiceLookup.lookupPrintServices(eq(null), eq(null)));
        }
    }

    @Test
    public void findPrintServiceManyPrinterResultTest() {
        PrintService[] ps = new PrintService[25];
        for(int i = 0; i < 25; i++) {
            ps[i] = new TestPrintService("PName" + i);
        }

        try (MockedStatic<PrintServiceLookup> dummy = Mockito.mockStatic(PrintServiceLookup.class)) {
            dummy.when(() -> PrintServiceLookup.lookupPrintServices(null, null))
                    .thenReturn(ps);
            PrintingJob pj = new TestPrintingJob("PName21");

            assertEquals(new TestPrintService("PName21"), pj.service);

            dummy.verify(() -> PrintServiceLookup.lookupPrintServices(eq(null), eq(null)));
        }
    }

    @Test
    public void initWithServiceTest() {
        PrintService ps = new TestPrintService("name");
        assertEquals(ps, new TestPrintingJob(ps).service);
    }

    private static class TestPrintingJob extends PrintingJob {
        protected TestPrintingJob(String printerName) {
            super(printerName);
        }

        protected TestPrintingJob(PrintService service) {
            super(service);
        }

        @Override
        public void execute(){
        }
    }
}
