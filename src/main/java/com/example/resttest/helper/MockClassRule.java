package com.example.resttest.helper;

import org.junit.rules.MethodRule;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by Finn on 16-09-2017.
 */
public class MockClassRule implements TestRule, MethodRule
{
    // auto-wiring components
    @Autowired
    private MockHelper mockHelper;

    // variables declaration
    private static int proxyPort;

    // constructor
    public MockClassRule (int port)
    {
        try
        {
            mockHelper.setOptions(port);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public Statement apply(Statement statement, Description description)
    {
        return new Statement()
        {
            @Override
            public void evaluate() throws Throwable
            {
                mockHelper.mockServerStart();

                statement.evaluate();

                mockHelper.mockServerStop();
            }
        };
    }

    @Override
    public Statement apply(Statement statement, FrameworkMethod frameworkMethod, Object o)
    {
        return this.apply(statement, Description.EMPTY);
    }
}
