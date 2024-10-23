package com.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.*;

public class StockManagementTests {

    @Test
    public void testCanGetACorrectLocatorCode() {

        ExternalISBNDataService testWebService = new ExternalISBNDataService() {
            @Override
            public Book lookup(String isbn) {
                return new Book("0582827647", "Of Mice and Men", "John MiddleName Steinbeck");
            }
        };

        ExternalISBNDataService testDatabaseService = new ExternalISBNDataService() {
            @Override
            public Book lookup(String isbn) {
                return null;
            }
        };

        StockManager stockManager = new StockManager();
        stockManager.setDatabaseService(testDatabaseService);
        stockManager.setWebService(testWebService);
        String isbn = "0582827647";
        String locatorCode = stockManager.getLocatorCode(isbn);
        assertEquals("7647J4", locatorCode);
    }

    // This test makes sure that the database Is Used When Data Is Present (i.e. the Database service is called)
    @Test
    public void databaseIsUsedWhenDataIsPresent(){
        ExternalISBNDataService databaseService = mock(ExternalISBNDataService.class);
        ExternalISBNDataService webService = mock(ExternalISBNDataService.class);

        when(databaseService.lookup("0582827647"))
                .thenReturn(new Book("1234567890", "sample_title", "sample_author"));

        StockManager stockManager = new StockManager();
        stockManager.setDatabaseService(databaseService);
        stockManager.setWebService(webService);
        String isbn = "0582827647";
        String locatorCode = stockManager.getLocatorCode(isbn);

        verify(databaseService, times(1)).lookup("0582827647");
        verify(webService, never()).lookup(anyString());
    }

    // This test makes sure that the webService Is Used When Data Is Not Present In Database
    @Test
    public void webServiceIsUsedWhenDataIsNotPresentInDatabase(){
        fail("Not yet implemented");
    }
}
