package com.example.a300cem_assignment_movieapp;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MovieListAdapterTest {

    MovieListAdapter adapter;
    String[][] arr;

    @Before
    public void setup() {
        arr = new String[3][3];
        adapter = new MovieListAdapter(arr);
    }

    @Test
    public void evalutesExpression() {
        int arrLength = adapter.getCount();
        assertEquals(3, arrLength);
    }
}
