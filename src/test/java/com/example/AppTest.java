package com.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

class AppTest {

    private App app;

    @BeforeEach
    void setUp() {
        app = new App();
    }

    @Test
    void testGreetingNotNull() {
        assertNotNull(app.getGreeting());
    }

    @Test
    void testGreetingContent() {
        assertEquals("Hello, World!", app.getGreeting());
    }

    @Test
    void testGreetingContainsHello() {
        assertTrue(app.getGreeting().contains("Hello"));
    }

    @Test
    void testGreetingContainsWorld() {
        assertTrue(app.getGreeting().contains("World"));
    }

    @Test
    void testGreetingIsNotEmpty() {
        assertFalse(app.getGreeting().isEmpty());
    }
}
